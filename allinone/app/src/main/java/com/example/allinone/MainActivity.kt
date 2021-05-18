package com.example.allinone

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.allinone.adapter.MainAdapter
import com.example.allinone.entity.Page
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    val LOAD_DATE_DONE = 0x001
    val pageList: MutableList<Page> = mutableListOf()
    lateinit var llMain : ListView

    lateinit var mainHandler : Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        llMain = findViewById(R.id.ll_main)

        val mainAdapter = MainAdapter(this,pageList)

        mainHandler = object : Handler(mainLooper){
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    LOAD_DATE_DONE -> {
                        runOnUiThread{
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        Thread{
            Log.e("TAG", "onCreate: ---------running----------", )
            val url = getString(R.string.baseUrl)
            val client = OkHttpClient()
            val request = Request.Builder()
                .get()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {}

                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    if (response.isSuccessful) {
                        // String str = response.body().string();
                        val str = response.body().string()
                        Log.e("TAG", "onResponse: ----------------$str-----------------", )
                    }else{
                        Log.e("TAG", "onResponse: -------------unSuccessful----------------", )
                    }
                }
            })
        }.run()

    }
}