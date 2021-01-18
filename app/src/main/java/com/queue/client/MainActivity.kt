package com.queue.client

import android.os.Bundle
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.queue.client.utils.SystemUtil
import com.smtlibrary.utils.JsonUtils
import com.smtlibrary.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtils.LOG_DEBUG = BuildConfig.DEBUG
        LogUtils.sysout("======ip:", SystemUtil.getLocalHostIp(this))

        btnLogin.setOnClickListener {

        }

        btnGet.setOnClickListener {
            val msg = MessageInfo<ResData.ValueBean>(CmdUtils.CMD4, ResData.ValueBean(CmdUtils.CMD4,""))
            clientSocket!!.loadData(JsonUtils.serialize(msg), object : SmartClientSocket.CallBackInterface {
                override fun onFaile(e: Exception?) {
                    LogUtils.sysout("=======rev======", "网络超时异常")
                }

                override fun onCallBack(data: String?) {
                    LogUtils.sysout("=======rev======", data)
                }

            })
        }
    }


}
