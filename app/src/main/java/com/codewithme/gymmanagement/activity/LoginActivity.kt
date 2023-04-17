package com.codewithme.gymmanagement.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codewithme.gymmanagement.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {
    private lateinit var email : EditText
    private lateinit var password : EditText
    private lateinit var btn_login:Button
    private lateinit var auth: FirebaseAuth

    private lateinit var emailString : String
    private lateinit var passwordString : String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.login_email)
        password = findViewById(R.id.login_password)


        val signupText = findViewById<TextView>(R.id.signupText)
        signupText.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        })

        btn_login = findViewById(R.id.loginButton)

        btn_login.setOnClickListener {
            emailString = email.text.toString().trim()
            passwordString = password.text.toString().trim()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Welcome", Toast.LENGTH_LONG).show()
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
//        intent.putExtra("data",user)
        startActivity(intent)
        finish()

    }

    override fun onAuthStateChanged(user: FirebaseAuth) {
        if(user.currentUser != null){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)

    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }
}