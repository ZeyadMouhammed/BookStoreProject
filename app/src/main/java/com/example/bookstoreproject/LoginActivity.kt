package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnShowPassword = findViewById<ImageView>(R.id.btnShowPassword)
        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val layoutSignUp = findViewById<LinearLayout>(R.id.layoutSignUp)

        btnShowPassword.setOnClickListener {
            passwordVisible = !passwordVisible

            if (passwordVisible) {
                edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnShowPassword.setImageResource(R.drawable.ic_eye_open)
            } else {
                edtPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowPassword.setImageResource(R.drawable.ic_eye)
            }

            edtPassword.setSelection(edtPassword.text.length)
        }

        btnSignIn.setOnClickListener {
            Toast.makeText(this, "Signing in...", Toast.LENGTH_SHORT).show()

            // Navigate to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Optional: close LoginActivity so user can't go back
            finish()
        }

        layoutSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}
