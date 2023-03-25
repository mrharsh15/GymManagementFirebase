package com.codewithme.gymmanagement.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codewithme.gymmanagement.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignupActivity : AppCompatActivity() {
    lateinit var loginText : TextView
    lateinit var btn_signup : Button
    private lateinit var auth: FirebaseAuth

    private lateinit var emailaddress:String
    private lateinit var passwordString:String

    lateinit var email:EditText
    lateinit var password:EditText
    lateinit var confirmPassword:EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        auth = Firebase.auth

        val loginText = findViewById<TextView>(R.id.loginText)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        confirmPassword = findViewById(R.id.signup_confirm_password)

        loginText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        })

        btn_signup = findViewById(R.id.signup_button)

        btn_signup.setOnClickListener {

            emailaddress = email.text.toString()
            passwordString = password.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailaddress, passwordString)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                    }
                }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("data",user)
        startActivity(intent)
    }
}