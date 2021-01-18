package com.queue.client

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.reflect.TypeToken
import com.queue.client.entry.*
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.views.RvDividerItemDecoration
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.irecyclerview.adapter.CommonRecycleViewAdapter
import com.smtlibrary.irecyclerview.adapter.OnItemClickListener
import com.smtlibrary.irecyclerview.adapter.ViewHolderHelper
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_nurse.*
import java.lang.Exception


/**
 * 护士管理界面
 */
class NurseActivity : BaseActivity() {


    var adapt: CommonRecycleViewAdapter<RoomsInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nurse)
        toobar.setTitle("")
        setSupportActionBar(toobar)
        toobar.setNavigationOnClickListener { finish() }
        swipeRefreshLayout.setColorSchemeColors(Color.GREEN, Color.BLUE, Color.RED)
        val mLinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val dividerItemDecoration = RvDividerItemDecoration(this@NurseActivity, RvDividerItemDecoration.VERTICAL_LIST);
        iRecycleview.layoutManager = mLinearLayoutManager
        iRecycleview.addItemDecoration(dividerItemDecoration)
        adapt = object : CommonRecycleViewAdapter<RoomsInfo>(this@NurseActivity, R.layout.item_view_layout) {
            override fun convert(helper: ViewHolderHelper?, t: RoomsInfo?, position: Int) {

                helper!!.setText(R.id.tvUserName, t!!.roomName)
                helper!!.setText(R.id.tvRoomTitle, t!!.roomTitle)
                helper!!.setText(R.id.tvRoomDesc, t!!.roomDesc)
                if (position % 2 == 0) {
                    helper!!.getView<LinearLayout>(R.id.titleLayout).setBackgroundResource(R.drawable.tv_content_bg)
                    helper!!.getView<TextView>(R.id.tvRoomTitle).setBackgroundResource(R.drawable.tv_order_bg)
                } else {
                    helper!!.getView<LinearLayout>(R.id.titleLayout).setBackgroundResource(R.drawable.tv_order_no_bg2)
//                    helper!!.getView<TextView>(R.id.tvRoomTitle).setBackgroundResource(R.drawable.tv_order_green_bg)
                }

            }
        }
        iRecycleview.adapter = adapt
        swipeRefreshLayout.setOnRefreshListener {
            loadOrderInfoData(true)
        }
        imgAddRooms.setOnClickListener {
            val intent = Intent(this@NurseActivity, AddMoreRoomsActivity::class.java)
            intent.putExtra("isAdd", true)
            startActivityForResult(intent, 1001)
        }
        adapt!!.setOnItemClickListener(object : OnItemClickListener<RoomsInfo> {
            override fun onItemLongClick(parent: ViewGroup?, view: View?, t: RoomsInfo?, position: Int): Boolean {
                val dialog = SweetAlertDialog(this@NurseActivity, SweetAlertDialog.WARNING_TYPE)
                dialog.setTitle(getString(R.string.rm_user_waring))
                dialog.setTitleText(String.format("%s%s", getString(R.string.if_rm_user), t!!.roomName))
                dialog.setConfirmText(getString(R.string.comfirm))
                dialog.setCancelText(getString(R.string.cancle))
                dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
                    override fun onClick(sweetAlertDialog: SweetAlertDialog?) {
                        sweetAlertDialog!!.dismiss()
                        deleteUser(t!!.id.toString())
                    }
                })
                dialog.show()
                return false
            }

            override fun onItemClick(parent: ViewGroup?, view: View?, t: RoomsInfo?, position: Int) {
                dialogList(t!!)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        loadOrderInfoData(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    /*
    *停止看诊
     */
    private fun setStopKanZhen() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD28, ResData.ValueBean(CmdUtils.CMD28, ""))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setNextUser=====", data)
                runOnUiThread {
                    try {
                        praseData(data)
                    } catch (e: Exception) {
                        toast(getString(R.string.opr_user_faile))
                        dismissProgressDialog()
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
     * 已经看诊
     */
    private fun setHashKanZhen(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD5, ResData.ValueBean(CmdUtils.CMD5, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setHashKanZhen=====", data)
                runOnUiThread {
                    try {
                        adapt!!.all.forEach { it ->
                            //                            if (it.id == (ID.toInt())) {
//                                adapt!!.remove(it)
//                            }
                        }
                        dismissProgressDialog()
                    } catch (e: Exception) {
                        loadOrderInfoData(false)
                        dismissProgressDialog()
                    }
                }
            }

            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    dismissProgressDialog()
                    toast(getString(R.string.net_work_error))
                }
            }

        })
    }

    private fun setNextPerson(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD6, ResData.ValueBean(CmdUtils.CMD6, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setNextUser=====", data)
                runOnUiThread {
                    try {
                        praseData(data)
                    } catch (e: Exception) {
//                        toast(getString(R.string.opr_user_faile))
                        loadOrderInfoData(false)
                        dismissProgressDialog()
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
     * 列表
     */
    private fun dialogList(t: RoomsInfo) {
        val items = resources.getStringArray(R.array.setting_array)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(String.format(getString(R.string.setting) + ":%s", t!!.roomName))
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(items) { p0: DialogInterface?, p1: Int ->
            p0!!.dismiss()
            when (p1) {
                0 -> {
                    if (t!!.type == 0) {
                        val intent = Intent(this@NurseActivity, AddRoomsActivity::class.java)
                        intent.putExtra("isAdd", false)
                        intent.putExtra("roomInfo", t!!)
                        startActivityForResult(intent, 1001)
                    } else {
                        toast("该房间已在使用中.")
                    }
                }
                1 -> {
                    jiechuUser(t!!.id.toString())
                }

            }
        }
        builder.create().show()
    }

    private fun setKanZhen(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD7, ResData.ValueBean(CmdUtils.CMD7, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setNextUser=====", data)
                runOnUiThread {
                    try {
                        praseData(data)
                    } catch (e: Exception) {
                        loadOrderInfoData(false)
                        dismissProgressDialog()
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
     * 加载列表
     */
    private fun loadOrderInfoData(isShow: Boolean) {
        if (isShow)
            showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData>(CmdUtils.CMD4, ResData())
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
            override fun onFaile(e: Exception?) {
                LogUtils.sysout("=======rev======", "网络超时异常")
                runOnUiThread {
                    swipeRefreshLayout.isRefreshing = false
                    dismissProgressDialog()
                }
            }

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev======", data)
                runOnUiThread {
                    dismissProgressDialog()
                    swipeRefreshLayout.isRefreshing = false
                    try {
                        if (!TextUtils.isEmpty(data)) {
                            val type = object : TypeToken<MessageInfo<List<RoomsInfo>>>() {}.type
                            val res = JsonUtils.deserialize<MessageInfo<List<RoomsInfo>>>(data, type)
                            adapt!!.clear()
                            if (res.tag.equals(CmdUtils.CMD4)) {
                                if (res.value.size > 0)
                                    adapt!!.addAll(res.value)
                                swipeRefreshLayout.isRefreshing = false
                                Toast.makeText(this@NurseActivity, R.string.onreflash, Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: Exception) {
                        loadOrderInfoData(false)
//                        Toast.makeText(this@NurseActivity, "同步数据失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    private fun praseData(data: String?) {
        dismissProgressDialog()
        if (!TextUtils.isEmpty(data)) {
            val type = object : TypeToken<MessageInfo<List<RoomsInfo>>>() {}.type
            val res = JsonUtils.deserialize<MessageInfo<List<RoomsInfo>>>(data, type)
            adapt!!.clear()
            if (res.tag.equals(CmdUtils.CMD4)) {
                if (res.value.size > 0)
                    adapt!!.addAll(res.value)
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@NurseActivity, R.string.onreflash, Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun deleteUser(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD2, ResData.ValueBean(CmdUtils.CMD2, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=delete=====", data)
                runOnUiThread {
                    try {
                        if (!TextUtils.isEmpty(data) && data!!.contains("0000")) {
                            toast(getString(R.string.opr_user_success))
                            adapt!!.all.forEach { it ->
                                if (it.id == (ID.toLong())) {
                                    adapt!!.remove(it)
                                }
                            }
                        } else {
                            toast(getString(R.string.opr_user_faile))
                        }
                        dismissProgressDialog()
                    } catch (e: Exception) {
                        dismissProgressDialog()
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
     * 修改信息
     */
    private fun updateUser(ID: String, name: String) {
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this@NurseActivity, "name is not null", Toast.LENGTH_SHORT).show()
            return
        }
        showProgressDialog(getString(R.string.add_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD22, ResData.ValueBean(name, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev======", data)
                runOnUiThread {
                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                    if (res.isSuccess) {
                        toast(getString(R.string.opr_user_success))
                        loadOrderInfoData(false)
                    } else {
                        dismissProgressDialog()
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
     * 设置优先人员
     */
    private fun setFirstUser(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD3, ResData.ValueBean(CmdUtils.CMD3, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=setUser=====", data)
                runOnUiThread {
                    try {
                        praseData(data)
                    } catch (e: Exception) {
//                        toast(getString(R.string.opr_user_faile))
                        loadOrderInfoData(false)
                        dismissProgressDialog()
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
     * 设置过号
     */
    private fun setUserGh(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD8, ResData.ValueBean(CmdUtils.CMD8, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=设置过号User=====", data)
                runOnUiThread {
                    //                    val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
//                    if (res.isSuccess) {
//                        toast(getString(R.string.opr_user_success))
//                        loadOrderInfoData(false)
//                    } else {
//                        dismissProgressDialog()
//                        toast(getString(R.string.opr_user_faile))
//                    }
                    try {
                        praseData(data)
                    } catch (e: Exception) {
//                        toast(getString(R.string.opr_user_faile))
                        loadOrderInfoData(false)
                        dismissProgressDialog()
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
     * 解除过号(设置为候诊人员)
     */
    private fun jiechuUser(ID: String) {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD21, ResData.ValueBean(CmdUtils.CMD21, ID))
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {

            override fun onCallBack(data: String?) {
                LogUtils.sysout("=======rev=解除User=====", data)
                runOnUiThread {
                    dismissProgressDialog()
                    if (!TextUtils.isEmpty(data) && data!!.contains("0000")) {
                        toast(getString(R.string.opr_user_success))
                        adapt!!.all.forEach { it ->
                            if (it.id == ID.toLong()) {
                                it.roomTitle = ""
                                it.roomDesc = ""
                            }
                        }
                        adapt!!.notifyDataSetChanged()
                        loadOrderInfoData(false)
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


    override fun onDestroy() {
        super.onDestroy()
    }

}

