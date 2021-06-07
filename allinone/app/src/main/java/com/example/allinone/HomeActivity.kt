package com.example.allinone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.allinone.adapter.MainAdapter
import com.example.allinone.entity.Page
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.lang.Exception

class HomeActivity : Fragment()/*, View.OnKeyListener */ {
    val LOAD_DATA_DONE = 0x001
    val pageList: MutableList<Page> = mutableListOf()
    lateinit var lvMain: ListView
    lateinit var ivUserAvatar: ImageView
    lateinit var mainAdapter :MainAdapter
    lateinit var myView: View

    lateinit var mainHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myView = View.inflate(activity, R.layout.activity_home, null)
        initPart()
        setListener()
        mainHandler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    LOAD_DATA_DONE -> {
//                        runOnUiThread {
                            mainAdapter.notifyDataSetChanged()
//                        }
                    }
                }
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
//                        runOnUiThread {
                                mainAdapter.notifyDataSetChanged()
//                        }
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
                val newActivityIntent = Intent(context, Web::class.java)
                val date = Bundle()
                date.putString("url", pageList[position].pageUrl)
                newActivityIntent.putExtras(date)
                startActivity(newActivityIntent)
            }
        ivUserAvatar.setOnClickListener {
            val newActivityIntent = Intent(context, UserInfo::class.java)
            startActivity(newActivityIntent)
        }
    }

    private fun initPart() {
        mainAdapter = MainAdapter(context, pageList)
        lvMain = myView.findViewById(R.id.lv_main)
        ivUserAvatar = myView.findViewById(R.id.iv_user_avatar)


        val sp = context!!.getSharedPreferences("allInOne", Context.MODE_PRIVATE)
        if(sp.getBoolean("isLogin",false)){
            val url = getString(R.string.baseUrl) + sp.getString("userAvatar",getString(R.string.default_avatar))
            Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivUserAvatar)
        }else{
        Glide.with(this)
            .load(R.drawable.user_avatar_full_fill)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(ivUserAvatar)
        }



        lvMain.adapter = mainAdapter

    }
}