package top.cyixlq.msgalarm

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_listen.*
import top.cyixlq.msgalarm.bean.Msg
import top.cyixlq.msgalarm.bus.Bus
import top.cyixlq.msgalarm.bus.EventListener

class ListenActivity : AppCompatActivity() {

    companion object {
        fun start(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, ListenActivity::class.java))
        }
        private const val STR_LISTENED = "监听到了！！！"
        private const val STR_LISTENING = "正在监听。。。"
        private const val STR_EXIT_LISTEN = "退出监听"
        private const val STR_PROCEED_LISTEN = "继续监听"
    }

    private lateinit var eventListener: EventListener<Msg>
    private var mediaPlayer: MediaPlayer? = null
    private var isGet = false // 是否监听到了

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        mediaPlayer?.isLooping = true
        registerListener()
    }

    @SuppressLint("SetTextI18n")
    private fun registerListener() {
        eventListener = EventListener {
            tvInfo.text = "发送者号码：${it.pusher}\n短信内容：${it.content}"
            tvTitle.text = STR_LISTENED
            btnListen.text = STR_PROCEED_LISTEN
            isGet = true
            mediaPlayer?.start()
        }
        Bus.INSTANCE.with(Config.TAG, Msg::class.java).observe(eventListener)
    }

    fun stopOrStartListen(v: View) {
        if (isGet) {
            tvTitle.text = STR_LISTENING
            tvInfo.text = ""
            btnListen.text = STR_EXIT_LISTEN
            isGet = false
            mediaPlayer?.pause()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.release()
        mediaPlayer = null
        Bus.INSTANCE.with(Config.TAG, Msg::class.java).unObserve(eventListener)
    }
}
