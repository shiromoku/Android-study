package com.example.allinone

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.allinone.tools.EncryptTool
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Login : AppCompatActivity() {
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
        setContentView(R.layout.activity_login)
        tietUserName = findViewById(R.id.tiet_user_name)
        tietPassword = findViewById(R.id.tiet_password)
        btnLogin = findViewById(R.id.btn_login)
        btnLogin.isEnabled = false

        handler = object : Handler(mainLooper) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    LOGIN_SUCCESSFUL -> {
                        val data = msg.data
                        val spEdit = getSharedPreferences("allInOne", Context.MODE_PRIVATE).edit()
                        spEdit.putBoolean("isLogin", true)
                        spEdit.putString("userName", userName)
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
        val tvRegister = findViewById<TextView>(R.id.tv_register)
        tvRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }


        tietUserName.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
                userName = tietUserName.text.toString()
                if (userName == "") {
                    tietUserName.error = "用户名不能为空"
                    btnLogin.isEnabled = false
                }
//            }
        }
        tietPassword.setOnFocusChangeListener { _, hasFocus ->
//            if (!hasFocus) {
                password = tietPassword.text.toString()
                if (password == "") {
                    tietPassword.error = "密码不能为空"
                    btnLogin.isEnabled = false
                } else btnLogin.isEnabled = true
//            }
        }

        tietUserName.doOnTextChanged { _, _, _, count ->
            if (count == 0) {
                tietUserName.error = "用户名不能为空"
                btnLogin.isEnabled = false
            }
        }

        tietPassword.doOnTextChanged { _, _, _, count ->
            if(count == 0){
                tietPassword.error = "密码不能为空"
                btnLogin.isEnabled = false
            } else btnLogin.isEnabled = true
        }

        btnLogin.setOnClickListener {
            userName = tietUserName.text.toString()
            password = tietPassword.text.toString()
//            userName = "123456"
//            password = "123456"
            Thread {
                val url = getString(R.string.baseUrl) + getString(R.string.login)
                val client = OkHttpClient()
                val formBody =
                    FormBody.Builder().add("userName", EncryptTool.md5(userName))
                        .add("password", EncryptTool.md5(password)).build()
                val request = Request.Builder()
                    .post(formBody)
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {}

                    override fun onResponse(call: Call?, response: Response?) {
                        val body = response?.body()?.string() ?: ""
                        if (body != "") {
                            val result = JSONObject(body)
                            when (result.getInt("resultCode")) {
                                200 -> {
                                    val data =
                                        JSONObject(result.getJSONArray("data").get(0).toString())
                                    val userAvatar = data.getString("userAvatar")
                                    val bundleData = Bundle().apply {
                                        putString("userAvatar", userAvatar)
                                    }
                                    val msg = Message().apply {
                                        what = LOGIN_SUCCESSFUL
                                        this.data = bundleData
                                    }
                                    runOnUiThread {
                                        Toast.makeText(this@Login, "登陆成功", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    handler.sendMessage(msg)
                                }
                                403 -> {
                                    runOnUiThread {
                                        Toast.makeText(this@Login, "用户名与密码不符", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            }

                        }
                    }
                })
            }.run()
        }
    }

}