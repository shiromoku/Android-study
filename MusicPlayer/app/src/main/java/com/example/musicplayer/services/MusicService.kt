package com.example.musicplayer.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import com.example.musicplayer.R


class MusicService : Service() {
    companion object {
        open val PLAY_MUSIC = 0x001
        open val PAUSE_MUSIC = 0x002
        open val STOP_MUSIC = 0x003
        open val NEXT_MUSIC = 0x004
        open val PRE_MUSIC = 0x005
    }

    var mp: MediaPlayer? = null
    lateinit var musicList: MutableList<Int>
    var nowPlay = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        val fields = R.raw::class.java.declaredFields
        for (i in fields) musicList.add(i.getInt(R.raw::class.java))

        if (mp == null) {
            mp = MediaPlayer.create(this, musicList[nowPlay])
            mp?.isLooping = false
        }
        //注册
    }

    override fun onDestroy() {
        super.onDestroy()
        mp?.stop()
        mp?.release()
        //取消注册
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val data = intent.extras
            if (data != null) {
                when (data.getInt("doWhat")) {
                    PLAY_MUSIC -> playMusic()
                    PAUSE_MUSIC -> pauseMusic()
                    STOP_MUSIC -> stopMusic()
                    NEXT_MUSIC -> nextMusic()
                    PRE_MUSIC -> preMUsic()
                }
            }
        }
        return START_NOT_STICKY
    }

    fun playMusic() {
        if (mp == null) return
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
        mp!!.stop()
        mp!!.seekTo(0)
        mp!!.prepare()
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