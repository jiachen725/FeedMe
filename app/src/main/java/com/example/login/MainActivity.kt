package com.example.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.ActivityMainBinding

import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.login.ui.home.homeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setTitle("Welcome");
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.signupTextView.setOnClickListener {
            openSignUp(it)

        }
        binding.btnLogin.setOnClickListener(){
            LoginSuccess(it)
        }

        val textwatcher = object : TextWatcher {
            override
            fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

            }

            override
            fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val email  = binding.emailTextView.text.isEmpty()
                val pass = binding.passwordTextView.text.isEmpty()
                val btn = binding.btnLogin
                if(email == false && pass == false){
                    btn.setBackgroundColor(Color.parseColor("#486B00"))
                    btn.isEnabled = true

                }
                else{
                    btn.setBackgroundResource(R.color.darker_gray)
                    btn.isEnabled = false

                }
            }

            override
            fun afterTextChanged(editable: Editable?) {

            }
        }

        binding.passwordTextView.addTextChangedListener(textwatcher)
        binding.emailTextView.addTextChangedListener(textwatcher)


    }


    private fun openSignUp(view: View){
        val intent = Intent(this, SignUp::class.java)
        // start your next activity
        startActivity(intent)
    }

    private fun LoginSuccess(view: View){
        val btnlogin = binding.btnLogin
        val email =binding.emailTextView.text.toString()
        val password = binding.passwordTextView.text.toString()
        if(password.length < 6){
            Toast.makeText(this,"Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar2.isVisible=true
        btnlogin.isEnabled = false

        auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if (task.isSuccessful) {


                val currentUser = auth.currentUser

                val firebase = FirebaseDatabase.getInstance().getReference("member")
                currentUser?.let {
                    val uid = currentUser.uid

                    val rootRef = firebase.child(uid)
                    rootRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val member = dataSnapshot.getValue(member::class.java)

                            with(sharedPreferences.edit()) {
                                putString(getString(R.string.pref_name), member!!.username)
                                putString(getString(R.string.pref_email), member!!.memEmail)
                                putString(getString(R.string.pref_address), member!!.memAddress)
                                putString(getString(R.string.pref_pic), member!!.memPic)
                                putInt(getString(R.string.pref_point), member!!.memPoint)
                                apply()
                            }

                            val name = sharedPreferences.getString(getString(R.string.pref_name),"")
                            Log.e("DataBase", "DateBase Error"+name)

                        }

                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("DataBase", "DateBase Error")

                        }

                    }

                    )

                    Toast.makeText(this, "Welcome Back " , Toast.LENGTH_SHORT).show()

                }
                successLogin()

            }

            else {
                Log.e("register", "Error signing in with email link", task.exception)
                Toast.makeText(this,"Email or Password wrong", Toast.LENGTH_SHORT).show()


            }



            progressBar2.isVisible=false
            btnlogin.isEnabled = true

        }


    }
    private fun getUserProfile(mauth: FirebaseUser?) {



            // [END get_user_profile]

    }
    private fun successLogin(){
        val intent = Intent(this, HomePage::class.java)
        // start your next activity
        startActivity(intent)
    }
}
