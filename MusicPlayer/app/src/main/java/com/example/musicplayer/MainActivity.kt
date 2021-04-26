package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.musicplayer.broadcastReceiver.MusicServiceBroadcastReceiver


class MainActivity : AppCompatActivity() {
    companion object {
        open val PLAY_MUSIC = 0x001
        open val PAUSE_MUSIC = 0x002
        open val STOP_MUSIC = 0x003
        open val NEXT_MUSIC = 0x004
        open val PRE_MUSIC = 0x005
    }

    lateinit var musicServiceReceiver: BroadcastReceiver
    val myIntent = Intent("com.example.musicplayer.musicservice")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myIntent.setPackage(this.packageName)
        myIntent.putExtra("data", Bundle().apply {
            putInt("doWhat", PLAY_MUSIC)
        })



        val test = findViewById<Button>(R.id.test)
        test.setOnClickListener {
            startService(myIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        //注册广播接收器
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.musicplayer.musicbroadcast")
        musicServiceReceiver = MusicServiceBroadcastReceiver(this.packageName)
        //下面这一句：NetworkChangeReceiver 就会收到所有值为android.net.conn.CONNECTIVITY_CHANGE 的广播
        registerReceiver(musicServiceReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        //注销广播接收器
        unregisterReceiver(musicServiceReceiver)
        stopService(myIntent)
    }
}