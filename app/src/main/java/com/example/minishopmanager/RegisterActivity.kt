
package com.example.minishopmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DBHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        btnRegister.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Adresse email invalide"
                return@setOnClickListener
            }

            if (password.length < 6) {
                etPassword.error = "6 caractères minimum"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                etConfirmPassword.error = "Les mots de passe ne correspondent pas"
                return@setOnClickListener
            }

            val success = dbHelper.registerUser(name, email, password)

            if (success) {
                Toast.makeText(this, "Compte créé avec succès !", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("EMAIL", email)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Cet email est déjà utilisé", Toast.LENGTH_SHORT).show()
            }
        }

        tvLogin.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
