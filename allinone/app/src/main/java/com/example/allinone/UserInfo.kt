package com.example.allinone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

class UserInfo : AppCompatActivity() {
    private val REQUEST_LOGIN = 0x001
    companion object {
        val RESULT_LOGIN_SUCCESSFUL = 0x001
    }

    var isLogin = false
    lateinit var btnLogout:Button
    lateinit var rlUserInfo:RelativeLayout
    lateinit var ivUserAvatar: ImageView
    lateinit var tvUserName: TextView
    lateinit var spEdit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        rlUserInfo = findViewById(R.id.rl_user_info)
        ivUserAvatar = findViewById(R.id.iv_aui_user_avatar)
        tvUserName = findViewById(R.id.tv_user_name)

        spEdit = getSharedPreferences("allInOne", Context.MODE_PRIVATE).edit()

        btnLogout = findViewById(R.id.btn_logout)
        autoSwitchLogin()

    }

    private fun autoSwitchLogin() {
        val spReader = getSharedPreferences("allInOne", Context.MODE_PRIVATE)
        isLogin = spReader.getBoolean("isLogin", false)
        if (isLogin) {
            Log.e("TAG", "autoSwitchLogin: =============load user info==========", )
            btnLogout.visibility = View.VISIBLE
            rlUserInfo.setOnClickListener { }
            btnLogout.setOnClickListener {
                spEdit.putBoolean("isLogin", false)
                spEdit.apply()
                autoSwitchLogin()
            }
            // TODO: 2021/6/3 显示用户头像及昵称, 从sp拿数据
            val userName = spReader.getString("userName","")
            val userAvatar = spReader.getString("userAvatar","")
            Glide.with(this)
                .load(getString(R.string.baseUrl) + userAvatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivUserAvatar)
            tvUserName.text = userName
        } else {
            btnLogout.visibility = View.GONE
            rlUserInfo.setOnClickListener {
                val newActivity = Intent(this, Login::class.java)
                startActivityForResult(newActivity, REQUEST_LOGIN)
            }
            tvUserName.text = "点击头像登陆"
            Glide.with(this)
                .load(R.drawable.user_avatar_full_fill)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(ivUserAvatar)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_LOGIN -> {
                when(resultCode){
                    RESULT_LOGIN_SUCCESSFUL -> {
                        Log.e("TAG", "onActivityResult: ----------------------sucessful------------------", )
                        autoSwitchLogin()
                    }
                }
            }
        }
    }
}