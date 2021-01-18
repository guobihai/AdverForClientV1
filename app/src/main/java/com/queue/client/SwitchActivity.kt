package com.queue.client

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_switch.*
import java.lang.Exception

class SwitchActivity : BaseActivity() {

    var radioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch)

//        switchGroup.setOnCheckedChangeListener { group, checkedId ->
//            radioId = group.checkedRadioButtonId
//        }

        raVideoSetting.setOnClickListener {
            setVideoOrImageInfo(0)
        }

        raImgSetting.setOnClickListener {
            setVideoOrImageInfo(1)
        }


        switchVoice.setOnClickListener {
            val check = switchVoice.isChecked
            setSwitchVoice(check)
        }

//        switchPrint.setOnCheckedChangeListener { buttonView, isChecked ->
//            PreferenceUtils.putBoolean(this@SwitchActivity, "isPrint", isChecked)
//            val check = switchPrint.isChecked
//            setSwitchVoice(check)
//        }
        loadSwitchInfo()

        btnSetdelayTime.setOnClickListener {
            val delaytTime = et_delay.text.toString().trim()
            if (TextUtils.isEmpty(delaytTime)) {
                return@setOnClickListener
            }
            val time = delaytTime.toInt()
            if (time < 2) {
                toast(getString(R.string.second_time))
                return@setOnClickListener
            }
            if(time >99){
                toast(getString(R.string.second_err_time))
                return@setOnClickListener
            }
            setDelayTime(time * 1000)
        }
        btnRightSetdelayTime.setOnClickListener {
            val delaytTime = et_rightdelay.text.toString().trim()
            if (TextUtils.isEmpty(delaytTime)) {
                return@setOnClickListener
            }
            val time = delaytTime.toInt()
            if (time < 2) {
                toast(getString(R.string.second_time))
                return@setOnClickListener
            }
            if(time >99){
                toast(getString(R.string.second_err_time))
                return@setOnClickListener
            }
            setRightDelayTime(time * 1000)
        }


        btnDeleteFile.setOnClickListener {
            deleteALlFile()
        }
    }

    fun deleteALlFile() {
        AlertDialog.Builder(this@SwitchActivity).setTitle("警告")
            .setMessage("確認清除所有文件？")
            .setPositiveButton("確認") { dialog, switch ->
                deleteClearData()
                dialog.dismiss()

            }
            .setNegativeButton("取消", null).show()

    }
    fun deleteClearData() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD40, ResData.ValueBean(CmdUtils.CMD40, ""))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                runOnUiThread {
                    try {
                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        dismissProgressDialog()
                        if (res.isSuccess) {
                            toast(getString(R.string.opr_user_success))
                        } else {
                            toast(getString(R.string.opr_user_faile))
                        }
                    } catch (e: Exception) {
                    }
                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
            }

        })
    }
    fun loadSwitchInfo() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD13, ResData.ValueBean(CmdUtils.CMD13, ""))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=loadSwitchInfo=====", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    dismissProgressDialog()
                    if (res.value != null) {
                        toast(getString(R.string.opr_user_success))
                        val isCheckedVideo = res.value.code.toBoolean()
                        if (isCheckedVideo) {
                            switchGroup.check(R.id.raVideoSetting)
                        } else {
                            switchGroup.check(R.id.raImgSetting)
                        }
                        val isChecked = res.value.msg.toBoolean()
                        switchVoice.isChecked = isChecked

                        val time = res.value.time.toString()
                        if (!TextUtils.isEmpty(time)) {
                            val nTime = (time.toInt() / 1000).toString()
                            et_delay.setText(nTime)
                        }
                        val rightTime = res.value.righttime.toString()
                        if (!TextUtils.isEmpty(rightTime)) {
                            val nTime = (rightTime.toInt() / 1000).toString()
                            et_rightdelay.setText(nTime)
                        }
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

    /**
     * 设置多媒体切换
     */
    fun setVideoOrImageInfo(type: Int) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD25, ResData.ValueBean(CmdUtils.CMD25, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setVideoOrImageInfo=====", data)
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


    /**
     * 是否播放语音
     */
    fun setSwitchVoice(type: Boolean) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD11, ResData.ValueBean(CmdUtils.CMD11, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setSwitchVoice=====", data)
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

    /**
     * 是否显示菜单
     */
    fun setShowMenu(type: Boolean) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD32, ResData.ValueBean(CmdUtils.CMD32, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowMenu=====", data)
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

    fun setDelayTime(type: Int) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD27, ResData.ValueBean(CmdUtils.CMD27, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowMenu=====", data)
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

    fun setRightDelayTime(type: Int) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD38, ResData.ValueBean(CmdUtils.CMD38, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowMenu=====", data)
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
}
