package com.example.allinone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.google.android.material.textfield.TextInputEditText

class LoginRegister : AppCompatActivity() {
    val LOGIN_SUCCESSFUL = 0x001
    val LOGIN_FAILED = 0x002

    lateinit var tietUserName: TextInputEditText
    lateinit var tietPassword: TextInputEditText

    lateinit var handler: Handler
    var userName = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        tietUserName = findViewById(R.id.tiet_user_name)
        tietPassword = findViewById(R.id.tiet_password)

        handler = object:Handler(mainLooper){
            override fun handleMessage(msg: Message) {
                when(msg.what){
                    LOGIN_SUCCESSFUL -> {
                        val data = msg.data
                        val spEdit = getSharedPreferences("allInOne",Context.MODE_PRIVATE).edit()
                        spEdit.putBoolean("isLogin",true)
                        spEdit.putString("userName",data.getString("userName"))
                        spEdit.putString("userAvatar",data.getString("userAvatar"))
                        spEdit.apply()
                        setResult(UserInfo.RESULT_LOGIN_SUCCESSFUL)
                        finish()
                    }
                    LOGIN_FAILED -> {
                        val spEdit = getSharedPreferences("allInOne",Context.MODE_PRIVATE).edit()
                        spEdit.putBoolean("isLogin",true)
                        spEdit.apply()
                    }
                }
            }
        }

        setListener()
//        setResult()
//        finish()
    }

    private fun setListener() {
        tietUserName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                userName = tietUserName.text.toString()
                if (userName == "") tietUserName.error = "用户名不能为空"
            }
        }
        tietPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                password = tietPassword.text.toString()
                if (password == "") tietPassword.error = "密码不能为空"
            }
        }
    }

}