package com.example.firstapi

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var button: Button
    private lateinit var textView: TextView
    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)

        button.setOnClickListener { getPokemonImageURL() }
    }

    private fun getPokemonImageURL() {
        val randomPokemonId = (1..898).random()
        val url = "https://pokeapi.co/api/v2/pokemon/$randomPokemonId/"

        client.get(url, null, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                Log.d("Pokemon", "Response successful")
                val imageUrl = response?.getJSONObject("sprites")?.getString("front_default")
                imageUrl?.let { Glide.with(this@MainActivity).load(it).into(imageView) }
                val name = response?.getString("name")
                val height = response?.getInt("height")
                val weight = response?.getInt("weight")
                textView.text = "Name: $name\nHeight: $height\nWeight: $weight"
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                Log.d("Pokemon Error", "Error response code: $statusCode")
                button.isEnabled = true
            }
        })
    }
}
