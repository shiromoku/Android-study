package com.example.allinone

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.allinone.fragment.HomeFragment

class MainActivity : FragmentActivity() {
    lateinit var flMain: FrameLayout
    lateinit var rgMain: RadioGroup
    lateinit var rbHome: RadioButton
    lateinit var rbMine: RadioButton

    var fromFragment: Fragment? = null
    var position = 0
    private val fragmentList = mutableListOf<Fragment>()

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        Log.e("TAG", "onCreate: //////////+++++++++//////////", )
        return super.onCreateView(parent, name, context, attrs)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.e("TAG", "onCreate: ////////////////////////////////", )
        setContentView(R.layout.activity_main)
        initPart()
        setListener()
        rgMain.post {
            rgMain.check(R.id.rb_home)
        }
        super.onCreate(savedInstanceState, persistentState)
    }

    fun initPart() {
        flMain = findViewById(R.id.fl_main)
        rgMain = findViewById(R.id.rg_main)
        rbHome = findViewById(R.id.rb_home)
        rbMine = findViewById(R.id.rb_mine)
        initFragmentList()
    }

    fun setListener() {
        rgMain.setOnCheckedChangeListener(MainOnClickedChangeListener())
    }

    private fun initFragmentList() {
        Log.e("TAG", "initFragmentList: ----------------------", )
        fragmentList.add(HomeFragment())
    }

    inner class MainOnClickedChangeListener : RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when (checkedId) {
                R.id.rb_home -> position = 0
                R.id.rb_mine -> position = 1
                else -> position = 0
            }
            Log.e("TAG", "onCheckedChanged: -------------------------", )
            switchFragment(fromFragment, getFragment())
        }
    }

    private fun switchFragment(from: Fragment?, to: Fragment) {
        if (to != from) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            from?.let { fragmentTransaction.hide(it) }

            try {
                if (!to.isAdded) {
                    fragmentTransaction.add(R.id.fl_main, to)
                } else {
                    fragmentTransaction.show(to)
                }
            } catch (e: java.lang.IllegalStateException) {
            }

            fromFragment = to
            fragmentTransaction.commit()
        }
    }

    private fun getFragment(): Fragment {
        return fragmentList[position]
    }
}

/*
class MainActivity : Activity()*/
/*, View.OnKeyListener *//*
 {
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


        Glide.with(this)
            .load(R.drawable.user_avatar_full_fill)
            .into(ivUserAvatar)



        lvMain.adapter = mainAdapter

    }
}
*/
