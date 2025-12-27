package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private var passwordVisible = false
    private var confirmPasswordVisible = false
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize database
        dbHelper = MyDatabaseHelper(this)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)

        val btnShowPassword = findViewById<ImageView>(R.id.btnShowPassword)
        val btnShowConfirmPassword = findViewById<ImageView>(R.id.btnShowConfirmPassword)

        val btnSignUp = findViewById<Button>(R.id.btnSignIn)
        val layoutSignIn = findViewById<LinearLayout>(R.id.layoutSignUp)

        // Toggle password visibility
        btnShowPassword.setOnClickListener {
            passwordVisible = !passwordVisible
            if (passwordVisible) {
                edtPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnShowPassword.setImageResource(R.drawable.ic_eye_open)
            } else {
                edtPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowPassword.setImageResource(R.drawable.ic_eye)
            }
            edtPassword.setSelection(edtPassword.text.length)
        }

        // Toggle confirm password visibility
        btnShowConfirmPassword.setOnClickListener {
            confirmPasswordVisible = !confirmPasswordVisible
            if (confirmPasswordVisible) {
                edtConfirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                btnShowConfirmPassword.setImageResource(R.drawable.ic_eye_open)
            } else {
                edtConfirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                btnShowConfirmPassword.setImageResource(R.drawable.ic_eye)
            }
            edtConfirmPassword.setSelection(edtConfirmPassword.text.length)
        }

        // Sign Up button
        btnSignUp.setOnClickListener {
            val name = edtName.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            // Validate inputs
            if (name.isEmpty()) {
                edtName.error = "Name is required"
                edtName.requestFocus()
                return@setOnClickListener
            }

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

            if (confirmPassword.isEmpty()) {
                edtConfirmPassword.error = "Please confirm your password"
                edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                edtConfirmPassword.error = "Passwords do not match"
                edtConfirmPassword.requestFocus()
                return@setOnClickListener
            }

            // Check if email already exists
            if (dbHelper.isEmailExists(email)) {
                edtEmail.error = "Email already registered"
                edtEmail.requestFocus()
                Toast.makeText(
                    this,
                    "This email is already registered. Please sign in.",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            // Register user
            val result = dbHelper.registerUser(email, password, name)

            if (result > 0) {
                // Registration successful
                Toast.makeText(
                    this,
                    "Registration successful! Please sign in.",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to Login
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("email", email) // Pre-fill email
                startActivity(intent)
                finish()
            } else {
                // Registration failed
                Toast.makeText(
                    this,
                    "Registration failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // "Already have an account? Sign In"
        layoutSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}