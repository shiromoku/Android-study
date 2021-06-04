package com.example.allinone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserInfo : AppCompatActivity() {
    private val LOGIN = 0x001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        val rlUserInfo = findViewById<RelativeLayout>(R.id.rl_user_info)
        val spReader = getSharedPreferences("allInOne", Context.MODE_PRIVATE)
        val spEdit = getSharedPreferences("allInOne", Context.MODE_PRIVATE).edit()
        val isLogin = spReader.getBoolean("isLogin", false)
        val tvLogout = findViewById<TextView>(R.id.tv_logout)
        autoSwitchLogin(isLogin, tvLogout, rlUserInfo, spEdit)

    }

    private fun autoSwitchLogin(
        isLogin: Boolean,
        tvLogout: TextView,
        rlUserInfo: RelativeLayout,
        spEdit: SharedPreferences.Editor
    ) {
        if (isLogin) {
            tvLogout.visibility = View.VISIBLE
            rlUserInfo.setOnClickListener { }
            tvLogout.setOnClickListener {
                spEdit.putBoolean("isLogin", false)
                spEdit.apply()
            }
            // TODO: 2021/6/3 显示用户头像及昵称, 从sp拿数据
        } else {
            tvLogout.visibility = View.GONE
            rlUserInfo.setOnClickListener {
                val newActivity = Intent(this, LoginRegister::class.java)
                startActivityForResult(newActivity, LOGIN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            LOGIN -> {

            }
        }
    }
}