package com.example.login.ui.profile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.login.R
import com.example.login.databinding.ActivityEditPasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_password.*

class editPassword : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)
        supportActionBar?.title =("Change Your Password")
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_password)
        sharedPreferences = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        val textwatcher = object : TextWatcher {
            override
            fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

            }

            override
            fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val currPass= binding.currentPassword.text.isEmpty()

                val newPass = binding.newPassword.text.isEmpty()
                val btn = binding.changePasswordBtn
                if(currPass == false && newPass == false ){
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
        binding.currentPassword.addTextChangedListener(textwatcher)
        binding.newPassword.addTextChangedListener(textwatcher)
        binding.changePasswordBtn.setOnClickListener{

            reauthenticate()


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        this.finish()
        return true
    }
    private fun updatePassword() {
        // [START update_password]
        val user = FirebaseAuth.getInstance().currentUser
        val newPassword = newPassword.text.toString()
        val firebase = FirebaseDatabase.getInstance().getReference("member")
        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Password updated Successfully", Toast.LENGTH_SHORT).show()

                }
                else{
                    Toast.makeText(this, "Update Password failed", Toast.LENGTH_SHORT).show()
                }
            }
        // [END update_password]
    }
    private fun reauthenticate() {
        val password = binding.currentPassword.text.toString()
        val newPassword = binding.newPassword.text.toString()

        if (newPassword.length < 6) {
            binding.newPassword.setError(getString(R.string.input_error_password_length))
            binding.newPassword.requestFocus()
            return
        }
        // [START reauthenticate]
        val user = FirebaseAuth.getInstance().currentUser

        val email= sharedPreferences.getString(getString(R.string.pref_email),"")
        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential(email.toString(), password)

        // Prompt the user to re-provide their sign-in credentials
        user?.reauthenticate(credential)
            ?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    Log.d("Verify", "User re-authenticated.")
                    updatePassword()
                } else {
                    Log.e("Verify", "Error signing in with email link", task.exception)
                    Toast.makeText(this, "Wrong Current Password", Toast.LENGTH_SHORT).show()

                }

               }
        // [END reauthenticate]
    }
}
