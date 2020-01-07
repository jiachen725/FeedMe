package com.example.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class loading : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
    }


    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if(currentUser==null){

            val intent = Intent(this, MainActivity::class.java)
            // start your next activity
            startActivity(intent)

        }
        else {
            val intent = Intent(this, HomePage::class.java)
            // start your next activity
            startActivity(intent)
        }

    }
}
