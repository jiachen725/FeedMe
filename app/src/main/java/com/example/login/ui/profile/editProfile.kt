package com.example.login.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.login.HomePage
import com.example.login.R
import com.example.login.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class editProfile : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title =("Edit Profile");
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)
        val prefname = sharedPreferences.getString(getString(R.string.pref_name),"")
        val prefaddress = sharedPreferences.getString(getString(R.string.pref_address),"")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.email.text =  sharedPreferences.getString(getString(R.string.pref_email),"")
        binding.name.setText(String.format("%s", prefname))
        binding.address.setText(String.format("%s", prefaddress))

        val textwatcher = object : TextWatcher {
            override
            fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

            }

            override
            fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val name= binding.name.text.toString()

                val address = binding.address.text.toString()
                val btn = binding.SaveBtn
                if(name.equals(prefname)==false ||  address.equals(prefaddress)==false ) {
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
        binding.name.addTextChangedListener(textwatcher)
        binding.address.addTextChangedListener(textwatcher)
        binding.SaveBtn.setOnClickListener{

        }
        binding.editPasswrod.setOnClickListener{
            editPassword(it)
        }
        binding.SaveBtn.setOnClickListener{
            updateProfile(it)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }


    private fun editPassword(view: View){
        val intent = Intent(this, editPassword::class.java)
        // start your next activity
        startActivity(intent)
    }

    private fun updateProfile(view: View){
        val username = binding.name.text.toString()
        val address = binding.address.text.toString()
        if (username.isEmpty()) {
            binding.name.setError(getString(R.string.input_error_name_invalid))
            binding.name.requestFocus()
            return
        }
        if (address.isEmpty()) {
                    binding.address.setError("Invalid address enter")
            binding.address.requestFocus()
            return
        }




        val firebase = FirebaseDatabase.getInstance().getReference("member")

        auth = FirebaseAuth.getInstance()

        val id= auth.currentUser?.uid.toString()

        firebase.child(id).child("username").setValue(username).addOnCompleteListener{task->
            if(task.isSuccessful) {
                Log.d("register", "Successfully update name")
                firebase.child(id).child("memAddress").setValue(address).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        with(sharedPreferences.edit()) {
                            putString(getString(R.string.pref_name), username)
                            putString(getString(R.string.pref_address), address)
                            apply()
                        }
                        this.finish()
                        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "Update address failed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            else{
                Toast.makeText(this, "Update name failed", Toast.LENGTH_SHORT).show()

            }
        }
    }

}
