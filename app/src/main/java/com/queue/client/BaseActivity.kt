package com.queue.client

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.queue.client.tcp.SmartClientSocket
import com.queue.client.views.LoadingDialog
import com.smtlibrary.dialog.SweetAlertDialog
import com.smtlibrary.utils.LogUtils
import com.smtlibrary.utils.PreferenceUtils

open class BaseActivity : AppCompatActivity() {
    var clientSocket: SmartClientSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clientSocket =
            SmartClientSocket(PreferenceUtils.getString(this@BaseActivity, "IP", ""), 10002)
        LogUtils.LOG_DEBUG = BuildConfig.DEBUG
    }

    fun isSendFile(): Boolean {
        return false
    }

    var progressDialog: LoadingDialog? = null

    fun showProgressDialog(): LoadingDialog {
        progressDialog = LoadingDialog(this, "加载中...")
        progressDialog!!.show()
        return progressDialog!!
    }

    fun showProgressDialog(message: String): LoadingDialog {
        progressDialog = LoadingDialog(this, message)
        progressDialog!!.show()
        return progressDialog!!
    }

    fun dismissProgressDialog() {
        if (progressDialog != null) {
            // progressDialog.hide();会导致android.view.WindowLeaked
            progressDialog!!.close()
        }
    }

    fun toast(msg: String) {
        Toast.makeText(this@BaseActivity, msg, Toast.LENGTH_SHORT).show()
    }


    fun showErrorDialog(msg: String) {
        var dialog = SweetAlertDialog(this@BaseActivity, SweetAlertDialog.WARNING_TYPE)
        dialog.setTitle(getString(R.string.rm_user_waring))
        dialog.contentText = msg
        dialog.setConfirmClickListener(object : SweetAlertDialog.OnSweetClickListener {
            override fun onClick(sweetAlertDialog: SweetAlertDialog?) {

            }

        })
        dialog.show()
    }
}
