package com.example.musicplayer.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.musicplayer.MainActivity

class MusicServiceBroadcastReceiver(val packageName: String?) : BroadcastReceiver() {
    var newIntent:Intent = Intent("com.example.musicplayer.musicservice")

    init {
        newIntent.setPackage(this.packageName)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val toData = Bundle()
        val fromData = intent?.extras?.getBundle("data")
        if(fromData != null){
            toData.putInt("doWhat",fromData.getInt("doWhat"))
            fromData.getInt("seekTo").let {
                toData.putInt("seekTo", it)
            }
        }
        newIntent.putExtra("data",fromData)
        context.startService(newIntent)
    }
}