package com.queue.client

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.entry.RoomsInfo
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
import kotlinx.android.synthetic.main.activity_add_rooms.*
import java.lang.Exception

class AddRoomsActivity : BaseActivity() {
    private var isAdd: Boolean = false
    private var roomsInfo: RoomsInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_rooms)
        toobar.setTitle("")
        setSupportActionBar(toobar)
        isAdd = this.intent.getBooleanExtra("isAdd", true)
        toobar.setNavigationOnClickListener { finish() }
        btnLogin.setOnClickListener {
            val roomName = etRoomName.text.toString().trim()
            val roomTitle = etRoomTitle.text.toString().trim()
            val roomDesc = etRoomDesc.text.toString().trim()
            if (TextUtils.isEmpty(roomName)) {
//                Toast.makeText(this@AddRoomsActivity, "房間名稱不能為空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isAdd)
                addRoomInfo(roomName, roomTitle, roomDesc)
            else
                updateRoomInfo(roomsInfo!!.id, roomName, roomTitle, roomDesc)
        }
        btnCancel.setOnClickListener { finish() }
        if (!isAdd) {
            roomsInfo = this.intent.getSerializableExtra("roomInfo") as RoomsInfo
            etRoomName.setText(roomsInfo!!.roomName)
            etRoomTitle.setText(roomsInfo!!.roomTitle)
            etRoomDesc.setText(roomsInfo!!.roomDesc)
        }
        btnLogin.setText(if (isAdd) "添加" else getString(R.string.btn_setting))
    }


    private fun addRoomInfo(roomName: String, roomTitle: String, roomDesc: String) {
        showProgressDialog()
        val roomIndfo = RoomsInfo(roomName, roomTitle, roomDesc, 0)
        val msg = MessageInfo<RoomsInfo>(CmdUtils.CMD1, roomIndfo)
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    dismissProgressDialog()
                    Toast.makeText(this@AddRoomsActivity, "連接系統失敗,請檢測主機IP是否正確!!!", Toast.LENGTH_LONG).show()
                }

            }

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev======", data)
                runOnUiThread {
                    dismissProgressDialog()
                    if (!TextUtils.isEmpty(data) && data!!.contains("0000")) {
                        toast(getString(R.string.opr_user_success))
                        finish()
                    } else
                        toast(getString(R.string.opr_user_faile))
                    dismissProgressDialog()
                }
            }

        })
    }


    /**
     * 修改为在用房间
     */
    private fun updateRoomInfo(ID: Long, roomName: String, roomTitle: String, roomDesc: String) {
        showProgressDialog(getString(R.string.rm_person))
        val roomIndfo = RoomsInfo(roomName, roomTitle, roomDesc, 1)
        roomIndfo.id = ID
        val msg = MessageInfo<RoomsInfo>(CmdUtils.CMD7, roomIndfo)
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setNextUser=====", data)
                runOnUiThread {
                    if (!TextUtils.isEmpty(data) && data!!.contains("0000")) {
                        toast(getString(R.string.opr_user_success))
                        finish()
                    } else
                        toast(getString(R.string.opr_user_faile))
                    dismissProgressDialog()
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
