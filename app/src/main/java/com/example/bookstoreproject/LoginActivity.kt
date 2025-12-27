package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private var passwordVisible = false
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is already logged in
        if (UserSessionManager.isLoggedIn(this)) {
            navigateToMain()
            return
        }

        setContentView(R.layout.activity_login)

        // Initialize database
        dbHelper = MyDatabaseHelper(this)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
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
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            // Validate inputs
            if (email.isEmpty()) {
                edtEmail.error = "Email is required"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.error = "Please enter a valid email"
                edtEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edtPassword.error = "Password is required"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6) {
                edtPassword.error = "Password must be at least 6 characters"
                edtPassword.requestFocus()
                return@setOnClickListener
            }

            // Attempt login
            if (dbHelper.loginUser(email, password)) {
                // Login successful - get user info
                val user = dbHelper.getUserByEmail(email)

                if (user != null) {
                    // Save login session with userId
                    UserSessionManager.saveLoginSession(this, user.id, user.email, user.name)

                    Toast.makeText(this, "Welcome back, ${user.name ?: "User"}!", Toast.LENGTH_SHORT).show()

                    // Navigate to MainActivity
                    navigateToMain()
                } else {
                    Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Login failed
                Toast.makeText(
                    this,
                    "Invalid email or password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        layoutSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}