package com.queue.client

import android.content.Intent
import android.os.Bundle
import com.queue.client.utils.ConfirgUtils
import kotlinx.android.synthetic.main.activity_memu.*

/**
 *
 */
class MemuActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memu)

        btnGuaHao.setOnClickListener { startActivity(Intent(this@MemuActivity, SendFileActivity::class.java)) }
        btnTj.setOnClickListener { startActivity(Intent(this@MemuActivity, ShowMenuActivity::class.java)) }
        btnVideo.setOnClickListener { startActivity(Intent(this@MemuActivity, SettingVideoActivity::class.java)) }
        btnSettingParam.setOnClickListener { startActivity(Intent(this@MemuActivity, SettingActivity::class.java)) }
        btnSettingNotice.setOnClickListener { startActivity(Intent(this@MemuActivity, NoticeSettingActivity::class.java)) }
        btnSettingSwitch.setOnClickListener { startActivity(Intent(this@MemuActivity, SwitchActivity::class.java)) }
    }

    override fun onDestroy() {
        ConfirgUtils.isShowMenu = false
        ConfirgUtils.isShowTitle = false
        ConfirgUtils.isShowNotice = false
        super.onDestroy()
    }

}
