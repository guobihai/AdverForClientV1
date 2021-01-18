package com.queue.client

import android.os.Bundle
import android.text.TextUtils
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_setting_video.*
import java.lang.Exception

class SettingVideoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_video)

        btnNext.setOnClickListener {
            next()
        }
        btnLast.setOnClickListener {
            last()
        }
        btnStart.setOnClickListener { start() }



        btnNextImg.setOnClickListener {
            actionInfo(CmdUtils.CMD30)
        }
        btnLastImg.setOnClickListener {
            actionInfo(CmdUtils.CMD29)
        }
        btnStartImg.setOnClickListener { actionInfo(CmdUtils.CMD31) }


        btnAddVideoInfo.setOnClickListener {
            val dialog = SweetAlertDialog(this@SettingVideoActivity, SweetAlertDialog.WARNING_TYPE)
            dialog.setTitle(getString(R.string.rm_user_waring))
            dialog.setTitleText(resources.getString(R.string.add_vide_info_notice))
            dialog.setConfirmText(getString(R.string.comfirm))
            dialog.setCancelText(getString(R.string.cancle))
            dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog!!.dismiss()
                    addVideoInfo()
                }
            })
            dialog.show()
        }
    }

    fun next() {
        actionInfo(CmdUtils.CMD15)
    }

    fun last() {
        actionInfo(CmdUtils.CMD16)
    }

    fun start() {
        actionInfo(CmdUtils.CMD17)
    }

    fun actionInfo(cmd: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(cmd, ResData.ValueBean(cmd, ""))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        toast(getString(R.string.opr_user_success))
                    } else {
                        toast(getString(R.string.opr_user_faile))
                    }
                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
            }

        })
    }


    fun addVideoInfo() {
        val value = etVideoInfo.text.toString().trim()
        if (TextUtils.isEmpty(value) || !value.startsWith("http")) return
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD14, ResData.ValueBean(CmdUtils.CMD14, value))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        toast(getString(R.string.opr_user_success))
                    } else {
                        toast(getString(R.string.opr_user_faile))
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

