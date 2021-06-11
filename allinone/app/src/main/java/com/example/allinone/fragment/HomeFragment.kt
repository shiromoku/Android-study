package com.example.allinone.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.allinone.R
import com.example.allinone.UserInfo
import com.example.allinone.Web
import com.example.allinone.adapter.MainAdapter
import com.example.allinone.entity.Page
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class HomeFragment : Fragment() {
    lateinit var lvMain :ListView
    lateinit var mainHandler: Handler

    val LOAD_DATA_DONE = 0x001
    val pageList: MutableList<Page> = mutableListOf()
    val mainAdapter = MainAdapter(context, pageList)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context,R.layout.home_fragment,null)
        lvMain = view.findViewById(R.id.lv_main)
        mainHandler = object : Handler(context!!.mainLooper) {
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
        setListener()
        lvMain.adapter = mainAdapter
        return view;
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
//        ivUserAvatar.setOnClickListener {
//            val newActivityIntent = Intent(context, UserInfo::class.java)
//            startActivity(newActivityIntent)
//        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
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
}