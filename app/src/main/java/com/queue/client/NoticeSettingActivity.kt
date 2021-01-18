package com.queue.client

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_setting_notice.*
import java.lang.Exception

class NoticeSettingActivity : BaseActivity() {
    private var type: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_notice)
        btnPmTitleComfirm.setOnClickListener { setPmInfo() }
        val array = this.resources.getStringArray(R.array.notice_items)
        spinnerTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                type = position
                etPmTitle.setText("")
            }

        }

        btnPmTitleDelete.setOnClickListener {
            val dialog = SweetAlertDialog(this@NoticeSettingActivity, SweetAlertDialog.WARNING_TYPE)
            dialog.setTitle(getString(R.string.rm_user_waring))
            dialog.setTitleText(String.format("%s%s", getString(R.string.if_rm_user), array[type]))
            dialog.setConfirmText(getString(R.string.comfirm))
            dialog.setCancelText(getString(R.string.cancle))
            dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    sweetAlertDialog!!.dismiss()
                    deleteNoticeInfo()
                }
            })
            dialog.show()
        }
    }


    private fun deleteNoticeInfo() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD34, ResData.ValueBean(CmdUtils.CMD34, "", type))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        etPmTitle.setText("")
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
     * 设置跑马灯信息
     */
    private fun setPmInfo() {
        val value = etPmTitle.text.toString().trim()
        if (TextUtils.isEmpty(value)) {
            return
        }
        if (value.length > 200) {
            toast(getString(R.string.input_limit))
            return
        }
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD10, ResData.ValueBean(CmdUtils.CMD10, value, type))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.isSuccess) {
                        etPmTitle.setText("")
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
