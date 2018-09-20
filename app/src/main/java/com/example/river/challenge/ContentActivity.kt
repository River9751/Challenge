package com.example.river.challenge

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content.*
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request


class ContentActivity : AppCompatActivity {
    constructor() : super()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.content)
        userText.text = "User: " + intent.getStringExtra("user")

        signout.setOnClickListener {
            Thread {
                signOut()
            }
            runOnUiThread {

                val intent = Intent(this, MainActivity::class.java)

                startActivity(intent)
            }

        }
    }

    fun signOut(): String {


        val client = OkHttpClient()

        var msg = ""

        val formBody = FormBody.Builder()
                .build()

        val request = Request.Builder()
                .url("http://192.168.43.99:7777/logout.php")
                .post(formBody)
                .build()
        val response = client.newCall(request).execute()


        msg = response.body()!!.string()

        println(msg)



        return msg
    }

}