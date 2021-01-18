package com.queue.client

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.RoomsInfo
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_add_more_rooms.*
import java.lang.Exception

class AddMoreRoomsActivity : BaseActivity() {
    private var isAdd: Boolean = false
    private var roomsInfo: RoomsInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_more_rooms)
        toobar.setTitle("")
        setSupportActionBar(toobar)
        isAdd = this.intent.getBooleanExtra("isAdd", true)
        toobar.setNavigationOnClickListener { finish() }
        btnLogin.setOnClickListener {
            val roomName = etRoomName.text.toString().trim()
            val roomTitle = etRoomName1.text.toString().trim()
            val roomDesc = etRoomName2.text.toString().trim()
            if (TextUtils.isEmpty(roomName)) {
//                Toast.makeText(this@AddMoreRoomsActivity, "房间名称不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (isAdd)
                addRoomInfo(roomName, roomTitle, roomDesc)
            else
                updateRoomInfo(roomsInfo!!.id, roomName, roomTitle, roomDesc)
        }
        btnCancel.setOnClickListener { finish() }
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
                    Toast.makeText(this@AddMoreRoomsActivity, "连接系统失败,请检测主机IP是否正确!!!", Toast.LENGTH_LONG).show()
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
