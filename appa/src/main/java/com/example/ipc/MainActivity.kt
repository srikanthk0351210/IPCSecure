package com.example.appa

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var client: SecureServiceClient
    private lateinit var resultView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = SecureServiceClient(this)
        resultView = findViewById(R.id.result_text)
        val sendButton = findViewById<Button>(R.id.send_button)

        client.bind()

        sendButton.setOnClickListener {
            client.sendRequest("Hello from App A") { response ->
                resultView.text = "Received: $response"
                Toast.makeText(this, "Response: $response", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        client.unbind()
    }
}