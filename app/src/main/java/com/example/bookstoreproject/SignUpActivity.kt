package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    private var passwordVisible = false
    private var confirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val edtEmail = findViewById<EditText>(R.id.edtEmail)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val edtConfirmPassword = findViewById<EditText>(R.id.edtConfirmPassword)

        val btnShowPassword = findViewById<ImageView>(R.id.btnShowPassword)
        val btnShowConfirmPassword = findViewById<ImageView>(R.id.btnShowConfirmPassword)

        val btnSignIn = findViewById<Button>(R.id.btnSignIn)
        val layoutSignUp = findViewById<LinearLayout>(R.id.layoutSignUp)

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

        // Sign Up button (prototype)
        btnSignIn.setOnClickListener {
            Toast.makeText(this, "Sign up clicked!", Toast.LENGTH_SHORT).show()

            // Example: move to MainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // "Already have an account? Sign In"
        layoutSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
