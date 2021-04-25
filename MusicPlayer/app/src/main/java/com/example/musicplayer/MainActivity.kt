package com.example.musicplayer

import android.content.Intent
import android.media.MediaParser
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.services.MusicService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent("com.example.musicplayer.musicservice")
        intent.setPackage(this.packageName)
        intent.extras?.putInt("doWhat",MusicService.PLAY_MUSIC)
    }
}