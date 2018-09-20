package com.example.river.challenge

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import okhttp3.OkHttpClient
import okhttp3.FormBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signUpBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener {
            var msg = Check()
            if(msg != null){
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            Thread {
                var msg = signIn()
                val jsonObject = JSONObject(msg)
                val result = jsonObject.getString("result")
                val memo = jsonObject.getString("memo")

                runOnUiThread {
                    Toast.makeText(this, memo, Toast.LENGTH_LONG).show()
                    if (result == "true"){
                        val intent = Intent(this, ContentActivity::class.java)
                        intent.putExtra("user", signInAcc.text.toString())
                        startActivity(intent)
//                        finish()
                    }
                }
            }.start()
        }
    }


    fun get(url: String): String? {
        val client: OkHttpClient = OkHttpClient()

        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()

        var msg = ""
        val body = response.body()
        if (body != null) {
            msg = body.string()
//            Toast.makeText(this,
//                    body.string(),
//                    Toast.LENGTH_LONG).show()
        } else {
            msg = "Null"
        }

        return msg
    }

    fun signIn(): String {

        val client = OkHttpClient()

        var msg = ""

        val formBody = FormBody.Builder()
                .add("login", "")
                .add("user_name", signInAcc.text.toString())
                .add("password", signInPwd.text.toString())
                .build()

        val request = Request.Builder()
                .url("http://192.168.43.99:7777/login.php")
                .post(formBody)
                .build()
        val response = client.newCall(request).execute()


        msg = response.body()!!.string()

        println(msg)

        return msg
    }

    fun Check(): String? {
        if (signInAcc.text.toString() == "" || signInPwd.text.toString() == "") {
            return "請勿輸入空白"
        }

        var regex = Regex("[A-Za-z0-9]*")

        if (!regex.matches(signInAcc.text)) {
            return "密碼格式錯誤"
        }

        return null
    }

    fun getSignupResult(jsonText: String) {
        try {
            val jsonObject = JSONObject(jsonText)
            val name = jsonObject.getString("result")
            val errorMessage = jsonObject.getString("")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

}
