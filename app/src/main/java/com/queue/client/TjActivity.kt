package com.queue.client

import android.os.Bundle
import android.text.TextUtils
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResData
import com.queue.client.entry.TjInfo
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.utils.Tool
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_tj.*
import java.lang.Exception
import java.util.*

class TjActivity : BaseActivity() {

    //    var timePickerView: TimePickerView? = null
    var selectTime: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tj)
        selectTime = Tool.getSystemTime()
        tvClearData.setOnClickListener {

            val dialog = SweetAlertDialog(this@TjActivity, SweetAlertDialog.WARNING_TYPE)
            dialog.setTitle(getString(R.string.rm_user_waring))
            dialog.setTitleText(getString(R.string.opr_clear_data))
            dialog.setConfirmText(getString(R.string.comfirm))
            dialog.setCancelText(getString(R.string.cancle))
            dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog!!.dismiss()
                    cleardata()
                }
            })
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadTjInfo()
    }


    fun cleardata() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD26, ResData.ValueBean(CmdUtils.CMD26, selectTime))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=clear Data=====", data)
                runOnUiThread {
                    dismissProgressDialog()
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            tvAllPerson.setText("0")
                            tvYk.setText("0")
                            tvWk.setText("0")
                        } catch (e: Exception) {
                        }
                    }

                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    toast(getString(R.string.net_work_error))
                }
            }

        })
    }

    fun loadTjInfo() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD24, ResData.ValueBean(CmdUtils.CMD24, selectTime))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=loadData=====", data)
                runOnUiThread {
                    dismissProgressDialog()
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            val info = JsonUtils.deserialize<TjInfo>(data, TjInfo::class.java)
                            tvAllPerson.setText(info.value.tjAll.toString())
                            tvYk.setText(info.value.tjYk.toString())
                            tvWk.setText(info.value.tjWk.toString())
                        } catch (e: Exception) {
                        }
                    }

                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    toast(getString(R.string.net_work_error))
                }
            }

        })
    }
}
