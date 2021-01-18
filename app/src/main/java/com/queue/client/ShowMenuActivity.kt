package com.queue.client

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.MotionEvent
import com.google.gson.JsonParser
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.utils.ConfirgUtils
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import com.smtlibrary.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_show_menu.*
import kotlinx.android.synthetic.main.activity_show_menu.switchPrint
import kotlinx.android.synthetic.main.activity_switch.*
import java.lang.Exception

class ShowMenuActivity : BaseActivity() {

//    var radioId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_menu)

//        switchPrint.setOnClickListener {
//            val check = switchPrint.isChecked
//            setShowMenu(check)
//        }
        switchPrint.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_UP) {
                if (ConfirgUtils.isShowMenu) {
                    val check = switchPrint.isChecked
                    switchPrint.isChecked = !check
                    setShowMenu(!check)
                } else {
                    showPsDialog(CmdUtils.CMD32)
                }
            }
            true
        }

        switchFullScreen.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_UP) {
                if (ConfirgUtils.isShowTitle) {
                    val check = switchFullScreen.isChecked
                    switchFullScreen.isChecked = !check
                    setShowFullScreen(!check)
                } else {
                    showPsDialog(CmdUtils.CMD35)
                }
            }
            true
        }

        switchNotice.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (ConfirgUtils.isShowNotice) {
                    val check = switchNotice.isChecked
                    switchNotice.isChecked = !check
                    setShowNoticeScreen(!check)
                } else {
                    showPsDialog(CmdUtils.CMD36)
                }
            }
            true
        }
        clearData.setOnClickListener {
            val dialog = SweetAlertDialog(this@ShowMenuActivity, SweetAlertDialog.WARNING_TYPE)
            dialog.setTitle(getString(R.string.rm_user_waring))
            dialog.contentText = "是否要清除"
            dialog.setConfirmText("確認")
            dialog.setCancelText("取消")
            dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                    setClearData()
                }

            })
            dialog.show()
        }
        loadSwitchInfo()

    }

    fun loadSwitchInfo() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD33, ResData.ValueBean(CmdUtils.CMD33, ""))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=loadSwitchInfo=====", data)
                runOnUiThread {
                    try {
//                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        val prase = JsonParser()
                        val obj = prase.parse(data).asJsonObject
                        val code = obj.get("code").asString
                        dismissProgressDialog()
                        if (!TextUtils.isEmpty(code) && code.equals("0000")) {
                            toast(getString(R.string.opr_user_success))
                            val isChecked = obj.get("showMenu").asString.toBoolean()
                            val isCheckedFullScreen = obj.get("showScreen").asString.toBoolean()
                            val isShowNotice = obj.get("showNotice").asString.toBoolean()
                            switchPrint.isChecked = isChecked
                            switchFullScreen.isChecked = isCheckedFullScreen
                            switchNotice.isChecked = isShowNotice
                        } else {
                            toast(getString(R.string.opr_user_faile))
                        }
                    } catch (e: Exception) {
                        dismissProgressDialog()
                    }
                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
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
                    try {
                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        dismissProgressDialog()
                        if (res.isSuccess) {
                            ConfirgUtils.isShowMenu = true
//                            PreferenceUtils.putBoolean(this@ShowMenuActivity, "isShowMenu", true)
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


    /**
     * 是否全屏
     */
    fun setShowFullScreen(type: Boolean) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD35, ResData.ValueBean(CmdUtils.CMD35, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowScreen=====", data)
                runOnUiThread {
                    try {
                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        dismissProgressDialog()
                        if (res.isSuccess) {
                            ConfirgUtils.isShowTitle = true
//                            PreferenceUtils.putBoolean(this@ShowMenuActivity, "isShowTitle", true)
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

    /**
     * 是否资讯
     */
    fun setShowNoticeScreen(type: Boolean) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD36, ResData.ValueBean(CmdUtils.CMD36, type.toString()))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowScreen=====", data)
                runOnUiThread {
                    try {
                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        dismissProgressDialog()
                        if (res.isSuccess) {
                            ConfirgUtils.isShowNotice = true
//                            PreferenceUtils.putBoolean(this@ShowMenuActivity, "isShowNotice", true)
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

    fun setClearData() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD37, ResData.ValueBean(CmdUtils.CMD37, ""))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setShowScreen=====", data)
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


    fun showPsDialog(type: String) {

        when (type) {
            CmdUtils.CMD35 -> {
                val check = switchFullScreen.isChecked
                switchFullScreen.isChecked = !check
                setShowFullScreen(!check)
            }
            CmdUtils.CMD36 -> {
                val check = switchNotice.isChecked
                switchNotice.isChecked = !check
                setShowNoticeScreen(!check)
            }
            CmdUtils.CMD32 -> {
                val check = switchPrint.isChecked
                switchPrint.isChecked = !check
                setShowMenu(!check)
            }

//        val psEdit = EditText(this@ShowMenuActivity)
//        psEdit.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
//        psEdit.setTransformationMethod(PasswordTransformationMethod())
//        AlertDialog.Builder(this@ShowMenuActivity).setTitle("這個功能不開放！")
//                .setView(psEdit)
//                .setPositiveButton("確認") { dialog, switch ->
//                    dialog.dismiss()
//                    val pass = psEdit.text.toString()
//                    when (type) {
//                        CmdUtils.CMD35 -> {
////                            if (pass.equals("2727")) {
//                                val check = switchFullScreen.isChecked
//                                switchFullScreen.isChecked = !check
//                                setShowFullScreen(!check)
////                            } else {
////                                toast("密碼錯誤")
////                            }
//                        }
//                        CmdUtils.CMD36 -> {
////                            if (pass.equals("3939")) {
//                                val check = switchNotice.isChecked
//                                switchNotice.isChecked = !check
//                                setShowNoticeScreen(!check)
////                            } else {
////                                toast("密碼錯誤")
////                            }
//                        }
//                        CmdUtils.CMD32 -> {
////                            if (pass.equals("1818")) {
//                                val check = switchPrint.isChecked
//                                switchPrint.isChecked = !check
//                                setShowMenu(!check)
////                            } else {
////                                toast("密碼錯誤")
////                            }
//                        }

//                    }


//                }
//                .setNegativeButton("取消", null).show()

//        }
        }
    }


    fun deleteALlFile() {
        AlertDialog.Builder(this@ShowMenuActivity).setTitle("警告")
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

}
