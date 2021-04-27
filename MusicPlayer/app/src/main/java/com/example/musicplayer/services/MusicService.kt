package com.example.musicplayer.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Bundle
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
        musicList = mutableListOf()

        val fields = R.raw::class.java.declaredFields
        for (i in fields) musicList.add(i.getInt(R.raw::class.java))

        if (mp == null) {
            mp = MediaPlayer.create(this, musicList[nowPlay])
            mp?.isLooping = false
        }


        //注册
        val intentFilter = IntentFilter()
        intentFilter.addAction("com.example.musicplayer.musicbroadcast")
        musicServiceReceiver = MusicServiceBroadcastReceiver(this.packageName)
        registerReceiver(musicServiceReceiver, intentFilter)
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
                when (data.getInt("doWhat")) {
                    MainActivity.START_SERVICE -> {
                        var musicName = resources.getResourceName(musicList[nowPlay])
                        musicName = musicName.substring(musicName.lastIndexOf('/') + 1)
                        sendMainBroadcast(MainActivity.UPDATE_UI, musicName, "0:05", "0:00", false)

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
        if (mp == null) {
            mp = MediaPlayer.create(this, musicList[nowPlay])
            mp?.isLooping = false
        }
        if (!mp!!.isPlaying) {
            mp!!.start()
        }

        updateMainUI(mp!!.isPlaying)

    }

    fun pauseMusic() {
        if (mp == null) return
        if (mp!!.isPlaying) {
            mp!!.pause()
        }
        updateMainUI(mp!!.isPlaying)
    }

    fun stopMusic() {
        if (mp == null) return
        mp!!.stop()
//        mp!!.seekTo(0)
        mp = null
        updateMainUI(false)
//        mp!!.prepare()
    }

    fun nextMusic() {
        if (mp == null) return
        val oldMp = mp;
        nowPlay++
        nowPlay %= musicList.size
        mp = MediaPlayer.create(this, musicList[nowPlay])
        var musicName = resources.getResourceName(musicList[nowPlay])
        musicName = musicName.substring(musicName.lastIndexOf('/') + 1)
        if (oldMp != null && mp != null) {
            if (oldMp.isPlaying) {
                oldMp.stop()
                oldMp.release()
                mp!!.start()
                updateMainUI(mp!!.isPlaying)

            } else {
                updateMainUI(mp!!.isPlaying)
            }
        }
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
        updateMainUI(mp!!.isPlaying)
    }

    private fun updateMainUI(isPlaying: Boolean) {
        var musicName = resources.getResourceName(musicList[nowPlay])
        musicName = musicName.substring(musicName.lastIndexOf('/') + 1)
        val totalTimeInt = mp?.duration?.div(1000)
        val totalTimeMin = totalTimeInt?.div(60)
        val totalTimeSec = totalTimeInt?.rem(60)
        val nowTimeInt = mp?.currentPosition?.div(1000)
        val nowTimeMin = nowTimeInt?.div(60)
        val nowTimeSec = nowTimeInt?.rem(60)

        sendMainBroadcast(
            MainActivity.UPDATE_UI,
            musicName,
            "$totalTimeMin:$totalTimeSec",
            "$nowTimeMin:$nowTimeSec",
            isPlaying
        )
    }

    fun sendMainBroadcast(
        doWhat: Int,
        musicName: String,
        totalTime: String,
        nowTime: String,
        isPlaying: Boolean
    ) {
        val broadcastInten = Intent()
        broadcastInten.action = "com.example.musicplayer.mainbroadcast"
        val totalTimeInt = mp?.duration?.div(1000)?:0
        val nowTimeInt = mp?.currentPosition?.div(1000)?:0
        broadcastInten.putExtra("data", Bundle().apply {
            putInt("doWhat", doWhat)
            putString("musicName", musicName)
            putString("totalTime", totalTime)
            putString("nowTime", nowTime)
            putBoolean("isPlaying", isPlaying)
            putInt("totalTimeInt",totalTimeInt)
            putInt("nowTimeInt",nowTimeInt)
        })
        sendBroadcast(broadcastInten)
    }
}