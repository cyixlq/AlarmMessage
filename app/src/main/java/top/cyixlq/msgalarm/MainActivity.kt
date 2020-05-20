package top.cyixlq.msgalarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showDialog()
    }

    private fun showDialog() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val isReadSMS = (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED)
        val isReceiveSMS = (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED)
        if (isReadSMS && isReceiveSMS) return
        AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("为了APP能正常运行，在授权提醒时请允许APP获取接收短信和读取短信的权限！")
                .setPositiveButton("我知道了") { _, _ ->
                    requestPermissions(
                            arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS),
                            100)
                }.show()
    }

    fun startListen(v: View) {
        val type = when {
            rbContent.isChecked -> Config.TYPE_AS_CONTENT
            rbPhoneAndContent.isChecked -> Config.TYPE_AS_PHONE_AND_CONTENT
            else -> Config.TYPE_AS_PHONE
        }
        val phone = edtPhone.text.trim().toString()
        val key = edtKey.text.trim().toString()
        if (type == Config.TYPE_AS_PHONE && phone.isEmpty()) {
            showToast("您必须填写要监听的电话号码")
            return
        }
        if (type == Config.TYPE_AS_CONTENT && key.isEmpty()) {
            showToast("您必须填写要监听的短信内容关键字")
            return
        }
        if (type == Config.TYPE_AS_PHONE_AND_CONTENT && (key.isEmpty() || phone.isEmpty())) {
            showToast("您必须要填写要监听的电话号码和要监听的短信内容关键字")
            return
        }
        Config.setType(type)
        if (phone.isNotEmpty()) {
            Config.setPhone(phone)
        }
        if (key.isNotEmpty()) {
            Config.setKeyword(key)
        }
        ListenActivity.start(this)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "您拒绝了读取短信或者接收短信的权限，APP将无法正常使用！", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
