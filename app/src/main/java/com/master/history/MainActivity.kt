package com.master.history

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    val http = "http://10.0.110.134:8090/masterWeiBo/getHistory100?"
    var recyclerview: RecyclerView? = null
    var progressx: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        progressx = findViewById<ProgressBar>(R.id.progressx)
        val textView = findViewById<TextView>(R.id.title)
        val ic_back = findViewById<View>(R.id.ic_back)
        ic_back.setOnClickListener {
            finish()
        }
        textView.text = "近100次登录情况"
        recyclerview?.layoutManager = LinearLayoutManager(this)
        recyclerview?.addItemDecoration(DividerItemDecoration(this, LinearLayout.VERTICAL))

        requestData()
    }

    private fun requestData() {
        val user = intent.getStringExtra("user")
        val uri = URL(http + "user=" + user)
        Thread(Runnable {
            try {
                val connection = uri.openConnection() as HttpURLConnection
                val stringBuffer = StringBuffer()
                if (connection.responseCode == 200) {
                    val bufferedReader = BufferedReader(InputStreamReader(connection.inputStream))
                    for (line in bufferedReader.readLines()) {
                        stringBuffer.append(line)
                    }
                    bufferedReader.close()
                    runOnUiThread {
                        progressx?.visibility = View.INVISIBLE
                        showxx(stringBuffer.toString())
                    }
                } else {
                    runOnUiThread {
                        progressx?.visibility = View.INVISIBLE
                        val toast = Toast(this@MainActivity)
                        toast.setText("获取信息失败")
                        toast.show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressx?.visibility = View.INVISIBLE
                    val toast = Toast(this@MainActivity)
                    toast.setText("获取信息失败")
                    toast.show()
                }
            }
        }).start()

    }

    private fun showxx(toString: String) {
        Log.i("xxx", toString)
        val gson = Gson()
        val type = object : TypeToken<Base<List<Data>>>() {}.type
        val bean = gson.fromJson(toString, type) as Base<List<Data>>
        recyclerview?.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.xxx, parent, false)) {
                    override fun toString(): String {
                        return super.toString()
                    }
                }
            }

            override fun getItemCount(): Int {
                return bean?.data?.size ?: 0
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val data = bean?.data[position]
                val textview = holder.itemView.findViewById<TextView>(R.id.text)
                textview.text = "ip地址：${data?.ip}\n登陆时间：${data?.date}"
            }

        }
    }
}
