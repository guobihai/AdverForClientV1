package com.queue.client

import android.os.Bundle
import android.text.TextUtils
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import com.smtlibrary.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_setting.*
import java.lang.Exception

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        btnTitleComfirm.setOnClickListener {
            settingTitle()
        }
        btnOrderTitleComfirm.setOnClickListener { settingOrderTitle() }

    }


    /**
     * 设置标题
     */
    private fun settingTitle() {
        val value = etTitle.text.toString().trim()
        if (TextUtils.isEmpty(value)) {
            return
        }
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD9, ResData.ValueBean(CmdUtils.CMD9, value))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        PreferenceUtils.putString(this@SettingActivity, "mainTile", value)
                        etTitle.setText("")
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

    /**
     * 设置标题
     */
    private fun settingOrderTitle() {
        val value = etOrderTitle.text.toString().trim()
        if (TextUtils.isEmpty(value)) {
            return
        }
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD12, ResData.ValueBean(CmdUtils.CMD12, value))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        etOrderTitle.setText("")
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


}
