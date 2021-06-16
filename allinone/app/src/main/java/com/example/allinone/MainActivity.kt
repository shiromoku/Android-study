package com.example.allinone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.allinone.adapter.MainAdapter
import com.example.allinone.entity.Page
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class MainActivity : Activity()/*, View.OnKeyListener */ {
    val LOAD_DATA_DONE = 0x001
    val pageList: MutableList<Page> = mutableListOf()
    lateinit var lvMain: ListView
    lateinit var ivUserAvatar: ImageView
    val mainAdapter = MainAdapter(this, pageList)

    lateinit var mainHandler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPart()
        setListener()

        mainHandler = object : Handler(mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    LOAD_DATA_DONE -> {
                        runOnUiThread {
                            mainAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        loadData()

    }

    private fun loadData() {
        Thread {
            val url = getString(R.string.baseUrl) + getString(R.string.news)
            val client = OkHttpClient()
            val request = Request.Builder()
                .get()
                .url(url)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    e?.printStackTrace()
                    runOnUiThread {
                        val tv_connect_fail = findViewById<TextView>(R.id.tv_connect_fail)
                        tv_connect_fail.visibility = View.VISIBLE
                    }
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response) {
                    if (response.isSuccessful) {
                        val str = response.body().string()

                        val jsonArray = JSONArray(str)
                        for (i in 0 until jsonArray.length()) {
                            val a = jsonArray.getJSONObject(i)
                            val p = Page(a)
                            pageList.add(p)
                        }
                        runOnUiThread {
                            val tv_connect_fail = findViewById<TextView>(R.id.tv_connect_fail)
                            tv_connect_fail.visibility = View.GONE
                            mainAdapter.notifyDataSetChanged()
                        }
                    } else {
                        Log.e("TAG", "onResponse: -------------unSuccessful----------------")
                    }
                }
            })
        }.run()
    }

    private fun setListener() {
        lvMain.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val newActivityIntent = Intent(this, Web::class.java)
                val date = Bundle()
                date.putString("url", pageList[position].pageUrl)
                newActivityIntent.putExtras(date)
                startActivity(newActivityIntent)
            }
        ivUserAvatar.setOnClickListener {
            val newActivityIntent = Intent(this, UserInfo::class.java)
            startActivity(newActivityIntent)
        }
    }

    private fun initPart() {
        lvMain = findViewById(R.id.lv_main)
        ivUserAvatar = findViewById(R.id.iv_user_avatar)


        val sp = getSharedPreferences("allInOne", Context.MODE_PRIVATE)
        if (sp.getBoolean("isLogin", false)) {
            Glide.with(this)
                .load(sp.getString("userAvatar", ""))
                .into(ivUserAvatar)
        } else {
            Glide.with(this)
                .load(R.drawable.user_avatar_full_fill)
                .into(ivUserAvatar)
        }

        lvMain.adapter = mainAdapter

    }
}