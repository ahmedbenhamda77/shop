
package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LIFECYCLE", "onCreate")
        setContentView(R.layout.activity_main)

        val etFullName = findViewById<EditText>(R.id.etFullName)
        val btnHello = findViewById<Button>(R.id.btnHello)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        btnHello.setOnClickListener {
            val name = etFullName.text.toString().trim()

            if (name.isEmpty()) {
                etFullName.error = "Enter your full name"
            } else {
                Toast.makeText(
                    this,
                    "Bonjour $name !",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("LIFECYCLE", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LIFECYCLE", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LIFECYCLE", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LIFECYCLE", "onStop")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LIFECYCLE", "onRestart")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LIFECYCLE", "onDestroy")
    }
}
