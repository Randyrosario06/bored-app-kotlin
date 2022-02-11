package com.randy.imbored

import android.app.DownloadManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    var textView : TextView? = null
    var title : TextView? = null
    var type: TextView? = null
    var participants : TextView? = null
    var price : TextView? = null
    var btnGet :Button? = null
    var imgType : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.txtWelcome)
        title = findViewById(R.id.title)
        type = findViewById(R.id.type)
        participants = findViewById(R.id.participants)
        price = findViewById(R.id.price)
        btnGet= findViewById(R.id.btnStopBoring)
        imgType = findViewById(R.id.imgType)
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
                     GetTypeImage(type?.text.toString())
                },
                Response.ErrorListener { error ->
                    textView?.text = "Ha ocurrido algo inesperado :(, intenta de nuevo"
                }
        )
        requestQueue.add(jsonObjectRequest)
    }

    fun GetTypeImage(type : String){
        var imageURL = ""
        val executor = Executors.newSingleThreadExecutor()

        // Once the executor parses the URL
        // and receives the image, handler will load it
        // in the ImageView
        val handler = Handler(Looper.getMainLooper())

        // Initializing the image
        var image: Bitmap? = null

        // Only for Background process (can take time depending on the Internet speed)
        executor.execute {

            // Image URL
            when(type){
                "education"->imageURL = "https://images.unsplash.com/photo-1497633762265-9d179a990aa6?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZWR1Y2F0aW9ufGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60"
                "recreational"-> imageURL = "https://images.unsplash.com/photo-1572983390686-9f04d1fb480c?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8cmVjcmVhdGlvbmFsfGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=800&q=60"
                "social"-> imageURL = "https://images.unsplash.com/photo-1536329583941-14287ec6fc4e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=800&q=60"
                else->imageURL = "https://images.unsplash.com/photo-1606103920295-9a091573f160?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8Ym9yZWR8ZW58MHx8MHx8&auto=format&fit=crop&w=800&q=60"
            }

            // Tries to get the image and post it in the ImageView
            // with the help of Handler
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)

                // Only for making changes in UI
                handler.post {
                    imgType?.setImageBitmap(image)
                }
            }

            // If the URL doesnot point to
            // image or any other kind of failure
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}