package com.queue.client

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.queue.client.entry.HostNo
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.greendao.ctrls.HostCtrls
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.views.LoadingDialog
import com.queue.client.views.RvDividerItemDecoration
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.irecyclerview.adapter.CommonRecycleViewAdapter
import com.smtlibrary.irecyclerview.adapter.ViewHolderHelper
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import com.smtlibrary.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_list_host.*
import java.lang.Exception

class ListHostActivity : AppCompatActivity() {

    var adapter: CommonRecycleViewAdapter<HostNo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_host)

        val list = HostCtrls.loadListHostInfo()
        val mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = RvDividerItemDecoration(this@ListHostActivity, RvDividerItemDecoration.VERTICAL_LIST);
        iRecycleview.layoutManager = mLinearLayoutManager
        iRecycleview.addItemDecoration(dividerItemDecoration)
        adapter = object : CommonRecycleViewAdapter<HostNo>(this@ListHostActivity, R.layout.item_host_layout, list) {
            override fun convert(helper: ViewHolderHelper?, t: HostNo?, position: Int) {
                helper!!.setText(R.id.tvIpHost, String.format("%s", t!!.ip))
                helper!!.setText(R.id.tvIpHostNo, t!!.showNo)
                val btnConnect = helper!!.getView<TextView>(R.id.btnConnect)
                val btnShowNo = helper!!.getView<TextView>(R.id.tvIpHostNo)
                if (position % 2 == 0) btnConnect.setBackgroundResource(R.drawable.btn_login_bg) else btnConnect.setBackgroundResource(R.drawable.btn_login_nomal_bg)
                if (position % 2 == 0) btnShowNo.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark)) else btnShowNo.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
                btnConnect.setOnClickListener {
                    //                    startActivity(Intent(this@ListHostActivity, MemuActivity::class.java))
                    login(t!!.ip,t!!.remark)
                }
                helper!!.getView<RelativeLayout>(R.id.hostLayout).setOnLongClickListener {
                    deleteHostInfo(t!!)
                    false
                }
            }
        }
        iRecycleview.adapter = adapter
        imgAdd.setOnClickListener { startActivityForResult(Intent(this@ListHostActivity, AddHostActivity::class.java), 1) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1001) {
            val list = HostCtrls.loadListHostInfo()
            adapter!!.clear()
            adapter!!.addAll(list)
        }
    }


    fun deleteHostInfo(t: HostNo) {
        val dialog = SweetAlertDialog(this@ListHostActivity, SweetAlertDialog.WARNING_TYPE)
        dialog.setTitle(getString(R.string.rm_user_waring))
        dialog.titleText = String.format(resources.getString(R.string.delete_mac), t!!.id)
        dialog.setConfirmText("删除")
        dialog.setCancelText("取消")
        dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
            override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                sweetAlertDialog!!.dismiss()
                HostCtrls.deleteHostInfoById(t!!.id)
                adapter!!.remove(t!!)
            }

        })
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
        if (!isWifiConnect()) {
            val dialog = SweetAlertDialog(this@ListHostActivity, SweetAlertDialog.WARNING_TYPE)
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


    private fun login(ip: String, machineNo: String) {
        if(TextUtils.isEmpty(machineNo)){
            Toast.makeText(this@ListHostActivity,getString(R.string.ipmachine),Toast.LENGTH_LONG).show()
            return
        }
        showProgressDialog()
        PreferenceUtils.putString(this@ListHostActivity, "IP", ip)
        val clientSocket = SmartClientSocket(ip, 10002)
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD0, ResData.ValueBean(CmdUtils.CMD0,machineNo))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    dismissProgressDialog()
//                    Toast.makeText(this@ListHostActivity, getString(R.string.error_msg), Toast.LENGTH_LONG).show()
                }

            }

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev======", data)
                runOnUiThread {
                    dismissProgressDialog()
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    if (res.isSuccess) {
                        PreferenceUtils.putString(this@ListHostActivity, "mainTile", res.value.msg)
                        startActivity(Intent(this@ListHostActivity, MemuActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@ListHostActivity, getString(R.string.error_msg), Toast.LENGTH_LONG).show()
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
