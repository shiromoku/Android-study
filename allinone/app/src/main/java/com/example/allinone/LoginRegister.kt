package com.example.allinone

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class LoginRegister : AppCompatActivity() {
    val LOGIN_SUCCESSFUL = 0x001
    val LOGIN_FAILED = 0x002

    lateinit var tietUserName: TextInputEditText
    lateinit var tietPassword: TextInputEditText
    lateinit var btnLogin: Button

    lateinit var handler: Handler
    var userName = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register)
        tietUserName = findViewById(R.id.tiet_user_name)
        tietPassword = findViewById(R.id.tiet_password)
        btnLogin = findViewById(R.id.btn_login)

        handler = object : Handler(mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    LOGIN_SUCCESSFUL -> {
                        val data = msg.data
                        val spEdit = getSharedPreferences("allInOne", Context.MODE_PRIVATE).edit()
                        spEdit.putBoolean("isLogin", true)
                        spEdit.putString("userName", data.getString("userName"))
                        spEdit.putString("userAvatar", data.getString("userAvatar"))
                        spEdit.apply()
                        setResult(UserInfo.RESULT_LOGIN_SUCCESSFUL)
                        finish()
                    }
                    LOGIN_FAILED -> {
                        val spEdit = getSharedPreferences("allInOne", Context.MODE_PRIVATE).edit()
                        spEdit.putBoolean("isLogin", true)
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
                if (userName == "") {
                    tietUserName.error = "用户名不能为空"
                    btnLogin.isEnabled = false
                } else btnLogin.isEnabled = true
            }
        }
        tietPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                password = tietPassword.text.toString()
                if (password == "") {
                    tietPassword.error = "密码不能为空"
                    btnLogin.isEnabled = false
                } else btnLogin.isEnabled = true
            }
        }

        btnLogin.setOnClickListener {
            Thread {
                val url = getString(R.string.baseUrl) + getString(R.string.login)
                val client = OkHttpClient()
                val request = Request.Builder()
                    .get()
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        TODO("Not yet implemented")
                    }

                    override fun onResponse(call: Call?, response: Response?) {
//                        TODO("Not yet implemented")
                        val body = response?.body()?.string() ?: ""
                        if (body != "") {
                            val result = JSONObject(body)
                            if(result.getInt("resultCode") == 200){
                                val data = result.getJSONObject("data")
                                Log.e("TAG", "onResponse: -------------${data.getString("userName")}------------", )
                            }
                        }
                    }
                })
            }.run()
        }
    }

}