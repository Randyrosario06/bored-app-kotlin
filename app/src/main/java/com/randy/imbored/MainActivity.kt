package com.randy.imbored

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*

class MainActivity : AppCompatActivity() {
    var textView : TextView? = null
    var title : TextView? = null
    var type: TextView? = null
    var participants : TextView? = null
    var price : TextView? = null
    var btnGet :Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.txtWelcome)
        title = findViewById(R.id.title)
        type = findViewById(R.id.type)
        participants = findViewById(R.id.participants)
        price = findViewById(R.id.price)
        btnGet= findViewById(R.id.btnStopBoring)
        btnGet?.setOnClickListener {
            GetActivity()
        }
    }

    fun  GetActivity(){
        // Instantiate the cache
        val cache = DiskBasedCache(cacheDir, 1024 * 1024) // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        val network = BasicNetwork(HurlStack())

        // Instantiate the RequestQueue with the cache and network. Start the queue.
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val url = "https://www.boredapi.com/api/activity/"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                 { response ->
                    //textView?.text = response.getString("activity")
                    title?.text = response.getString("activity")
                    type?.text = response.getString("type")
                    participants?.text = response.getString("participants")
                    price?.text = response.getString("price")
                },
                Response.ErrorListener { error ->
                    textView?.text = "Ha ocurrido algo inesperado :(, intenta de nuevo"
                }
        )
        requestQueue.add(jsonObjectRequest)
    }
}