package com.queue.client

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.greendao.ctrls.HostCtrls
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.utils.IpUtils
import com.queue.client.utils.SystemUtil
import com.queue.client.views.LoadingDialog
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import com.smtlibrary.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.Exception

class AddHostActivity : AppCompatActivity() {

    var isDoctor: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            val ip = editIp.text.toString().trim()
            val no = editIpNo.text.toString().trim()
            val machineNo = editMachine.text.toString().trim()
            if (TextUtils.isEmpty(no)) {
//                editIpNo.setError("编号不能为空")
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(ip)) {
//                editIp.setError("IP地址不能为空")
                return@setOnClickListener
            }
            if (!IpUtils.isIP(ip)) {
//                Toast.makeText(this@AddHostActivity, "请输入正确的IP地址.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(machineNo)) {
//                Toast.makeText(this@AddHostActivity, "请输入正确的IP地址.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val res = HostCtrls.addHostInfo(ip, no,machineNo)
            if (res.toInt() != -1) {
                setResult(1001, null)
                finish()
            } else {
//                Toast.makeText(this@AddHostActivity, "添加主机失败.", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        if (!isWifiConnect()) {
            val dialog = SweetAlertDialog(this@AddHostActivity, SweetAlertDialog.WARNING_TYPE)
            dialog.setTitle(getString(R.string.rm_user_waring))
            dialog.titleText = "请打开WI-FI连接"
            dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog!!.dismiss()
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }

            })
            dialog.show()
        }
    }


    fun isWifiConnect(): Boolean {
        val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        return mWifiInfo.isConnected
    }


    private fun login(ip: String) {
        showProgressDialog()
        val clientSocket = SmartClientSocket(ip, 10002)
        val msg = MessageInfo<ResData>(CmdUtils.CMD0, ResData())
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    dismissProgressDialog()
                    Toast.makeText(this@AddHostActivity, "连接系统失败,请检测主机IP是否正确!!!", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev======", data)
                runOnUiThread {
                    dismissProgressDialog()
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    if (res.isSuccess) {
                        PreferenceUtils.putString(this@AddHostActivity, "mainTile", res.value.msg)
                        if (isDoctor)
                            startActivity(Intent(this@AddHostActivity, MemuActivity::class.java))
                        else
                            startActivity(Intent(this@AddHostActivity, MemuActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@AddHostActivity, "连接系统失败,请检测主机IP是否正确!!!", Toast.LENGTH_LONG).show()
                    }
                }
            }

        })
    }

    var progressDialog: LoadingDialog? = null

    fun showProgressDialog(): LoadingDialog {
        progressDialog = LoadingDialog(this, "连接中...")
        progressDialog!!.show()
        return progressDialog!!
    }


    fun dismissProgressDialog() {
        if (progressDialog != null) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog!!.close()
        }
    }

}
