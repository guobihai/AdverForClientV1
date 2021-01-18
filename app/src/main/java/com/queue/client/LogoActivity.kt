package com.queue.client

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_logo.*

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)
        imgDoctor.setOnClickListener { startTo() }
        imgNurse.setOnClickListener { startToNurse() }
    }

    private fun startTo() {
        startActivity(Intent(this@LogoActivity, MainActivity::class.java))
        finish()
    }

    private fun startToNurse() {
        startActivity(Intent(this@LogoActivity, NurseActivity::class.java))
        finish()
    }
}
