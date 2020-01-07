package com.example.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.example.login.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth



  //  val newMember = member(userKey,email,"jiachen",password,11,"","")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sign_up)




      registerBtn.setOnClickListener{
       //   Toast.makeText(this,"cibai ",Toast.LENGTH_SHORT).show()

        register(it)



     }
      val textwatcher = object : TextWatcher {
          override
          fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2:Int) {

          }

          override
          fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
              val email  =emailText.text.isEmpty()
              val pass = passwordText.text.isEmpty()
              val name = nameText.text.isEmpty()

              val address = addressText.text.isEmpty()
              val btn = registerBtn
              if(email == false && pass == false && address == false && name == false){
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

      emailText.addTextChangedListener(textwatcher)
      passwordText.addTextChangedListener(textwatcher)
      nameText.addTextChangedListener(textwatcher)
      addressText.addTextChangedListener(textwatcher)



      //   firebase.child(userKey).setValue(newMember).addOnCompleteListener{
     //       Toast.makeText(this,"cibai ",Toast.LENGTH_SHORT).show()
    //    }

    }


    fun register(view: View){
        val name = nameText.text.toString()
        val email = emailText.text.toString()
        val password =passwordText.text.toString()
        val address = addressText.text.toString()

        val btn = registerBtn


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError(getString(R.string.input_error_email_invalid))
            emailText.requestFocus()
            return
        }

        if (passwordText.length() < 6) {
            passwordText.setError(getString(R.string.input_error_password_length))
           passwordText.requestFocus()
            return
        }

        if (nameText.length() >50) {
            nameText.setError(getString(R.string.input_error_name_invalid))
            nameText.requestFocus()
            return
        }

        progressBar.isVisible=true

        val firebase = FirebaseDatabase.getInstance().getReference("member")

        auth = FirebaseAuth.getInstance()


        val newMember = member(email,name,0,"",address)
      auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if (task.isSuccessful) {
               // val result = task.result
                // You can access the new user via result.getUser()
                // Additional user info profile *not* available via:
                // result.getAdditionalUserInfo().getProfile() == null
                // You can check if the user is new or existing:
                // result.getAdditionalUserInfo().isNewUser()

                val id= auth.currentUser?.uid.toString()

                firebase.child(id).setValue(newMember).addOnCompleteListener{task->
                    if(task.isSuccessful) {
                        Log.d("register", "Successfully signed in with email link!")
                        val intent = Intent(this, MainActivity::class.java)
                        // start your next activity
                        startActivity(intent)
                        Toast.makeText(this, "Successful register ", Toast.LENGTH_SHORT).show()

                    }
                    else{
                        Toast.makeText(this, "RealTime added failed", Toast.LENGTH_SHORT).show()

                        progressBar.isVisible=false
                    }
                }
            }

            else {
                Log.e("register", "Error signing in with email link", task.exception)
                Toast.makeText(this,"Sign in failed",Toast.LENGTH_SHORT).show()

                progressBar.isVisible=false
            }


        }
      //  val id = auth.currentUser?.uid.toString()




    }
}
