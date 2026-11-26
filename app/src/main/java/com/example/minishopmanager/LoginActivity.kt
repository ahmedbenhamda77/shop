
package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = DBHelper(this)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        val prefs = getSharedPreferences("MiniShopPrefs", MODE_PRIVATE)

        if (prefs.getBoolean("REMEMBER", false)) {
            etEmail.setText(prefs.getString("EMAIL", ""))
            cbRemember.isChecked = true
        }

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.checkUser(email, password)) {
                prefs.edit().apply {
                    putBoolean("REMEMBER", cbRemember.isChecked)
                    if (cbRemember.isChecked) {
                        putString("EMAIL", email)
                    } else {
                        remove("EMAIL")
                    }
                    apply()
                }

                val intent = Intent(this, CatalogueActivity::class.java)
                intent.putExtra("USERNAME", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
