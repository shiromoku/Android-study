package com.example.allinone

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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

class MainActivity : FragmentActivity()/*, View.OnKeyListener */ {
    lateinit var frameLayout: FrameLayout
    lateinit var radioGroup: RadioGroup
    lateinit var radioButtonHome: RadioButton
    lateinit var radioButtonMine: RadioButton
    var fromFragment: Fragment? = null
    var position = 0
    val fragmentList = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initPart()
        setListener()
//        Thread.sleep(5000)
        radioGroup.check(R.id.rg_main)
    }

    private fun initFragmentList() {
        fragmentList.add(HomeActivity())
//        fragmentList.add(UserInfo())
    }

    private fun setListener() {
        radioGroup.setOnCheckedChangeListener(MainOnCheckedChangeListener())
    }

    inner class MainOnCheckedChangeListener: RadioGroup.OnCheckedChangeListener {
        override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
            when(checkedId){
                R.id.rb_home -> {
                    position = 0
                }
                R.id.rb_home -> {
                    position = 1
                }

                else -> position = 0
            }

            switchFragment(fromFragment,getFragment())
        }
    }

    private fun switchFragment(from:Fragment?,to:Fragment) {
        if(to != from){
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            from?.let { fragmentTransaction.hide(it) }


            try {
                if(!to.isAdded){
                    fragmentTransaction.add(R.id.main_FrameLayout,to)
                }else{
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


    private fun initPart() {
        frameLayout = findViewById(R.id.main_FrameLayout)
        radioGroup = findViewById(R.id.rg_main)
        radioButtonHome = findViewById(R.id.rb_home)
        radioButtonMine = findViewById(R.id.rb_mine)

        initFragmentList()
    }
}