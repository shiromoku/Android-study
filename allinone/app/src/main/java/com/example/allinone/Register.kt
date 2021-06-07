package com.example.allinone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.allinone.tools.EncryptTool
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class Register : AppCompatActivity() {
    var userName = ""
    var password = ""
    var confirmPassword = ""
    lateinit var tietUserName : TextInputEditText
    lateinit var tietPassword : TextInputEditText
    lateinit var tietConfirmPassword : TextInputEditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__register)

        val btnRegister = findViewById<Button>(R.id.btn_register)

        tietUserName = findViewById(R.id.tiet_user_name)
        tietPassword = findViewById(R.id.tiet_password)
        tietConfirmPassword = findViewById(R.id.tiet_confirm_password)


        tietUserName.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                val text = tietUserName.text.toString()
                if(text == ""){
                    tietUserName.error = "不能为空"
                    btnRegister.isEnabled = false
                }else btnRegister.isEnabled = true
            }
        }
        tietPassword.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                val text = tietPassword.text.toString()
                val confirmPassword = tietConfirmPassword.text.toString()
                if(text == ""){
                    tietPassword.error = "不能为空"
                    btnRegister.isEnabled = false
                }else if(confirmPassword != text){
                    tietConfirmPassword.error = "确认密码与密码不相同"
                    btnRegister.isEnabled = false
                }else
                {
                    btnRegister.isEnabled = true
                }
            }
        }
        tietConfirmPassword.setOnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                val text = tietPassword.text.toString()
                val confirmPassword = tietConfirmPassword.text.toString()
                if(text == ""){
                    tietConfirmPassword.error = "不能为空"
                    btnRegister.isEnabled = false
                }else if(confirmPassword != text){
                    tietConfirmPassword.error = "确认密码与密码不相同"
                    btnRegister.isEnabled = false
                }else
                {
                    btnRegister.isEnabled = true
                }
            }
        }

        btnRegister.setOnClickListener {
            userName = tietUserName.text.toString()
            password = tietPassword.text.toString()
            confirmPassword = tietConfirmPassword.text.toString()
            userName = "123456"
            password = "123456"
            Thread {
                val url = getString(R.string.baseUrl) + getString(R.string.register)
                val client = OkHttpClient()
                val formBody =
                    FormBody.Builder()
                        .add("userName", EncryptTool.md5(userName))
                        .add("password", EncryptTool.md5(password))
                        .add("confirmPassword", EncryptTool.md5(confirmPassword))
                        .build()
                val request = Request.Builder()
                    .post(formBody)
                    .url(url)
                    .build()
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        Log.e("TAG", "onFailure: ++++++++++++++++++++++++++++")
                    }

                    override fun onResponse(call: Call?, response: Response?) {

                        val body = response?.body()?.string() ?: ""
                        Log.e("TAG", "onResponse: ---------------$body----------")
                        if (body != "") {
                            val result = JSONObject(body)
                            when(result.getInt("resultCode")){
                                200 -> {
                                runOnUiThread {
                                    Toast.makeText(this@Register,"注册成功",Toast.LENGTH_SHORT).show()
                                }

                                }
                                450 -> {
                                    runOnUiThread {
                                        Toast.makeText(this@Register,"用户已存在",Toast.LENGTH_LONG).show()
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