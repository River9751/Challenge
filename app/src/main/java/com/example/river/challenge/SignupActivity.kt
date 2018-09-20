package com.example.river.challenge

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.river.challenge.R.layout.signup
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.signup.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class SignupActivity : AppCompatActivity {
    constructor() : super()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.signup)

        cancel.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        signUpBtnConfirm.setOnClickListener {
            var msg = Check()
            if(msg != null){
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            Thread {
                val msg = signUp()
                runOnUiThread {
                    val jsonObject = JSONObject(msg)
                    val result = jsonObject.getString("result")
                    val memo = jsonObject.getString("memo")

                    Toast.makeText(this, memo, Toast.LENGTH_LONG).show()
                    if (result == "true"){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }.start()
        }
    }

    fun signUp(): String {


        val client = OkHttpClient()

        var msg = ""

        val formBody = FormBody.Builder()
                .add("signup", "")
                .add("user_name", signUpAcc.text.toString())
                .add("password", signUpPwd.text.toString())
                .add("password_confirmation", signUpConfirm.text.toString())
                .build()

        val request = Request.Builder()
                .url("http://192.168.43.99:7777/signup.php")
                .post(formBody)
                .build()
        val response = client.newCall(request).execute()


        msg = response.body()!!.string()

        println(msg)



        return msg
    }

    fun Check(): String? {
        if (signUpAcc.text.toString() == "" || signUpPwd.text.toString() == "" || signUpConfirm.text.toString() == "") {
            return "請勿輸入空白"
        }

        if (signUpPwd.text.toString() != signUpConfirm.text.toString()) {
            return "密碼與確認密碼不一致"
        }

        var regex = Regex("[A-Za-z0-9]*")

        if (!regex.matches(signUpAcc.text.toString())) {
            return "密碼格式錯誤"
        }

        return null
    }
}