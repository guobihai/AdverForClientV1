package com.queue.client

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mingyuechunqiu.mediapicker.data.bean.MediaInfo
import com.mingyuechunqiu.mediapicker.data.config.MediaPickerConfig
import com.mingyuechunqiu.mediapicker.data.config.MediaPickerThemeConfig
import com.mingyuechunqiu.mediapicker.data.constants.Constants
import com.mingyuechunqiu.mediapicker.data.constants.MediaPickerType
import com.mingyuechunqiu.mediapicker.feature.picker.MediaPicker
import com.queue.client.entry.MessageInfo
import com.queue.client.entry.ResCode
import com.queue.client.entry.ResData
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.utils.CmdUtils
import com.smtlibrary.utils.*
import kotlinx.android.synthetic.main.activity_send_file_layout.*
import kotlinx.android.synthetic.main.activity_setting_notice.*
import kotlinx.android.synthetic.main.toolbar.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.io.File
import java.lang.Exception
import java.util.*

/**
 * @author guobihai
 * createDate：2020/11/28 13:52
 * desc：发送文件
 *
 */
@RuntimePermissions
class SendFileActivity : BaseActivity() {

    private var mFilePath: String? = null
    private var upType: Int = 0
    private var upLeft: Int = 0
    var clientFileSocket: SmartClientSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_file_layout)
        toolBar.setNavigationOnClickListener { finish() }
        tvTitle.setText("請選擇要發送的內容")
        clientFileSocket =
            SmartClientSocket(PreferenceUtils.getString(this@SendFileActivity, "IP", ""), 10003)
        requireSdCardWithPermissionCheck()



        radiogroup.setOnCheckedChangeListener { group, checkedId ->
            val checkId = radiogroup.checkedRadioButtonId
            when (checkId) {
                R.id.leftRadio -> {
                    videotRadio.visibility = View.VISIBLE
                    upLeft = 0
                }
                R.id.rightRadio -> {
                    videotRadio.visibility = View.GONE
                    meidogroup.check(R.id.imgRadio)
                    upLeft = 1
                    upType = 0
                }
            }
            settingInfo()
        }
        meidogroup.setOnCheckedChangeListener { group, checkedId ->
            val checkId = meidogroup.checkedRadioButtonId
            when (checkId) {
                R.id.imgRadio -> {
                    upType = 0
                }
                R.id.videotRadio -> {
                    upType = 1
                }
            }
            settingInfo()
        }

        imageSelect.setOnClickListener { v ->
            MediaPicker.init(this)
                .setMediaPickerConfig(
                    MediaPickerConfig.Builder()
                        .setThemeConfig(MediaPickerThemeConfig.Builder().buildDarkTheme())
                        .setMediaPickerType(if (upType == 0) MediaPickerType.TYPE_IMAGE else MediaPickerType.TYPE_VIDEO)
                        .setMaxSelectMediaCount(1)
                        .setStartPreviewByThird(true)
                        .setColumnCount(3)
                        .setFilterLimitSuffixType(true)
                        .setFilterLimitMedia(true)
                        .build()
                )
                .pick()
        }
        btnSend.setOnClickListener { v ->
            if (TextUtils.isEmpty(mFilePath)) {
                Toast.makeText(this@SendFileActivity, "請選擇要發送的內容", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            showProgressDialog("正在发送...")
            mFilePath?.let {
                if (it.endsWith(".mp4")) {
                    sendFile()
                } else {
                    CompressImageUtils.compressImage(
                        this@SendFileActivity,
                        File(mFilePath),
                        object : CompressImageUtils.onStringValueInterface {
                            override fun onFaile(errMsg: String?) {
                                Toast.makeText(this@SendFileActivity, "压缩图片失败", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            override fun onFileCallBack(file: File?) {
                                mFilePath = file?.absolutePath
                                sendFile()
                            }

                        })
                }
            }

        }
        settingInfo()
    }

    /**
     * 发送文件
     */
    private fun sendFile() {
        clientFileSocket?.let {
            it.sendFile(mFilePath, object : SmartClientSocket.CallBackInterface {
                override fun onCallBack(data: String?) {
                    runOnUiThread {
                        Toast.makeText(this@SendFileActivity, "發送成功", Toast.LENGTH_SHORT).show()
                        dismissProgressDialog()
                        mFilePath = null
                        imageSelect.setImageResource(R.drawable.ic_add_circle_outline_black_24dp)
                    }
                }

                override fun onFaile(e: Exception?) {
                    runOnUiThread {
                        dismissProgressDialog()
                    }
                }

            })
        }
    }

    private fun settingInfo() {
        showProgressDialog(getString(R.string.rm_person))
        val msg = MessageInfo<ResData.ValueBean>(
            CmdUtils.CMD39,
            ResData.ValueBean(CmdUtils.CMD39, upLeft.toString(), upType)
        )
        LogUtils.sysout("======msg======", JsonUtils.serialize(msg))
        clientSocket!!.loadData(
            JsonUtils.serialize(msg),
            object : SmartClientSocket.CallBackInterface {
                override fun onCallBack(data: String?) {
                    LogUtils.sysout("=======rev=设置上传类型=====", data)
                    runOnUiThread {
                        val res = JsonUtils.deserialize<ResCode>(data, ResCode::class.java)
                        dismissProgressDialog()
                        if (res.isSuccess) {
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

    @NeedsPermission(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun requireSdCard() {
        System.out.println("==============")
    }

    @OnPermissionDenied(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    fun reject() {

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == Constants.MP_REQUEST_START_MEDIA_PICKER && resultCode == RESULT_OK) {
            val list: ArrayList<MediaInfo> =
                data.getParcelableArrayListExtra(Constants.EXTRA_PICKED_MEDIA_LIST)
            for (info in list) {
                Log.d(
                    "份",
                    info.title + "   fds    " + info.name + " " + info.filePath + " " +
                            info.size + " " + info.duration + " " + info.bucketId + " "
                            + info.bucketName
                )
                mFilePath = info.filePath
                if (info.filePath.endsWith(".mp4")) {
                    imageSelect.setImageResource(R.drawable.ic_baseline_videocam_24)
                    return
                }
                imageSelect.setImageBitmap(BitmapUtil.getBitmap(info.filePath))
            }
        }
    }
}
