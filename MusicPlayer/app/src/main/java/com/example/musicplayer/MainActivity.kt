package com.example.musicplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    companion object {
        val PLAY_MUSIC = 0x001
        val PAUSE_MUSIC = 0x002
        val NEXT_MUSIC = 0x004
        val PRE_MUSIC = 0x005
        val START_SERVICE = 0x006
        val UPDATE_UI = 0x007
        val SEEK_MUSIC = 0x008
        val STOP_TIMER = 0x009
    }

    lateinit var mainBroadcastReceiver: BroadcastReceiver
    lateinit var tvMusicInfo:TextView
    lateinit var sbLocation:SeekBar
    lateinit var tvTime:TextView
    lateinit var btnPreMusic:Button
    lateinit var btnPlayOrPause:Button
    lateinit var btnNextMusic:Button

    val myIntent = Intent("com.example.musicplayer.musicservice")
    var isPlaying: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPart()
        setListener()


    }

    private fun setListener() {
        btnPreMusic.setOnClickListener {
            val broadcastInten = Intent()
            broadcastInten.action = "com.example.musicplayer.musicbroadcast"
            broadcastInten.putExtra("data",Bundle().apply {
                putInt("doWhat", PRE_MUSIC)
            })
            sendBroadcast(broadcastInten)
        }
        btnPlayOrPause.setOnClickListener {
            val broadcastInten = Intent()
            broadcastInten.action = "com.example.musicplayer.musicbroadcast"
            if(isPlaying){
                broadcastInten.putExtra("data",Bundle().apply {
                    putInt("doWhat", PAUSE_MUSIC)
                })
            }else{
                broadcastInten.putExtra("data",Bundle().apply {
                    putInt("doWhat", PLAY_MUSIC)
                })
            }
            sendBroadcast(broadcastInten)
        }
        btnNextMusic.setOnClickListener {
            val broadcastInten = Intent()
            broadcastInten.action = "com.example.musicplayer.musicbroadcast"
            broadcastInten.putExtra("data",Bundle().apply {
                putInt("doWhat", NEXT_MUSIC)
            })
            sendBroadcast(broadcastInten)
        }

        sbLocation.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
//                    sendServicesBroadcast(PLAY_MUSIC,null)
                    sendServicesBroadcast(SEEK_MUSIC,progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                sendServicesBroadcast(PAUSE_MUSIC,null)
//                sendServicesBroadcast(PAUSE_MUSIC,null)
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                sendServicesBroadcast(SEEK_MUSIC,sbLocation.progress)
//                sendServicesBroadcast(PLAY_MUSIC,null)
            }
        })
    }

    private fun initPart() {
        tvMusicInfo = findViewById(R.id.tv_music_info)
        sbLocation = findViewById(R.id.sb_location)
        tvTime = findViewById(R.id.tv_time)
        btnPreMusic = findViewById(R.id.btn_pre_music)
        btnPlayOrPause = findViewById(R.id.btn_play_or_pause)
        btnNextMusic = findViewById(R.id.btn_next_music)


        myIntent.setPackage(this.packageName)
        myIntent.putExtra("data", Bundle().apply {
            putInt("doWhat", START_SERVICE)
        })
        startService(myIntent)
    }

    override fun onResume() {
        super.onResume()
//        //注册广播接收器
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.musicplayer.mainbroadcast")
        mainBroadcastReceiver = MainBroadcastReceiver()
        registerReceiver(mainBroadcastReceiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()
        //注销广播接收器
        unregisterReceiver(mainBroadcastReceiver)
        stopService(myIntent)
    }


    inner class MainBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(intent?.extras != null){
                val data = intent.extras!!.getBundle("data")
                when(data?.getInt("doWhat")){
                    UPDATE_UI -> {
                        val musicName = data.getString("musicName") ?: "N/A"
                        val totalTime = data.getString("totalTime") ?: "N/A"
                        val nowTime = data.getString("nowTime") ?: "N/A"
                        isPlaying = data.getBoolean("isPlaying")
                        val  totalTimeInt = data.getInt("totalTimeInt")
                        val nowTimeInt = data.getInt("nowTimeInt")
                        updateUI(musicName,totalTime,nowTime,totalTimeInt,nowTimeInt)
                    }
                }
            }
        }
    }

    fun updateUI(musicName: String, totalTime: String, nowTime: String,totalTimeInt:Int,nowTimeInt: Int) {
        tvMusicInfo.text = musicName
        val timeText = "$nowTime/$totalTime"
        tvTime.text = timeText
        if(isPlaying){
            btnPlayOrPause.text = "暂停"
        }else{
            btnPlayOrPause.text = "播放"
        }
        sbLocation.progress = nowTimeInt
        sbLocation.max = totalTimeInt
    }

    fun sendServicesBroadcast(doWhat: Int,seekTo :Int?){
        val broadcastInten = Intent()
        broadcastInten.action = "com.example.musicplayer.musicbroadcast"
        broadcastInten.putExtra("data",Bundle().apply {
            putInt("doWhat", doWhat)
            seekTo?.let { putInt("seekTo", it) }
        })
        sendBroadcast(broadcastInten)
    }
}