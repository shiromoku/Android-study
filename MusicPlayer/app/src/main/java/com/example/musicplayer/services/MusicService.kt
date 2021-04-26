package com.example.musicplayer.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import com.example.musicplayer.MainActivity
import com.example.musicplayer.R
import com.example.musicplayer.broadcastReceiver.MusicServiceBroadcastReceiver


class MusicService : Service() {

    lateinit var musicServiceReceiver: BroadcastReceiver

    var mp: MediaPlayer? = null
    lateinit var musicList: MutableList<Int>
    var nowPlay = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.e("TAG", "onCreate: ----------------on create----------------", )
        musicList = mutableListOf()

        val fields = R.raw::class.java.declaredFields
        for (i in fields) musicList.add(i.getInt(R.raw::class.java))

        if (mp == null) {
            mp = MediaPlayer.create(this, musicList[nowPlay])
            mp?.isLooping = false
        }


        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.musicplayer.musicbroadcast")
        musicServiceReceiver = MusicServiceBroadcastReceiver(this.packageName)
        //下面这一句：NetworkChangeReceiver 就会收到所有值为android.net.conn.CONNECTIVITY_CHANGE 的广播
        registerReceiver(musicServiceReceiver, intentFilter)
        //注册
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        mp?.release()
        //取消注册
        unregisterReceiver(musicServiceReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val extras = intent.extras
            val data = extras?.getBundle("data")
            if (data != null) {
                Log.e("TAG", "onStartCommand: --------------------start command----------------------", )
                when (data.getInt("doWhat")) {
                    MainActivity.START_SERVICE -> {
                        Log.e("TAG", "onStartCommand: ---------------------service start--------------------", )
                    }
                    MainActivity.PLAY_MUSIC -> playMusic()
                    MainActivity.PAUSE_MUSIC -> pauseMusic()
                    MainActivity.STOP_MUSIC -> stopMusic()
                    MainActivity.NEXT_MUSIC -> nextMusic()
                    MainActivity.PRE_MUSIC -> preMUsic()
                }
            }
        }
        return START_NOT_STICKY
    }

    fun playMusic() {
        if (mp == null) return
        Log.e("TAG", "playMusic: playMusic ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", )
        if (!mp!!.isPlaying) {
            mp!!.start()
        }
    }

    fun pauseMusic() {
        if (mp == null) return
        if (mp!!.isPlaying) {
            mp!!.pause()
        }
    }

    fun stopMusic() {
        if (mp == null) return
        Log.e("TAG", "stopMusic: ----------STOP MUSIC-----------------", )
        mp!!.stop()
        mp!!.seekTo(0)
//        mp!!.prepare()
    }

    fun nextMusic() {
        if (mp == null) return
        mp!!.release()
        nowPlay++
        nowPlay %= musicList.size
        mp = MediaPlayer.create(this, musicList[nowPlay])
    }

    fun preMUsic() {
        if (mp == null) return
        if (mp!!.currentPosition < 2000) {
            nowPlay--;
            if (nowPlay < 0) nowPlay = musicList.size - 1
            mp!!.release()
            mp = MediaPlayer.create(this, musicList[nowPlay])
        } else {
            mp!!.seekTo(0)
        }
    }
}