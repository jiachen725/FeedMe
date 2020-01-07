package com.example.login.ui.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.login.MainActivity
import com.example.login.R
import com.example.login.SignUp

import com.example.login.databinding.FragmentProfileBinding
import com.example.login.member
import com.firebase.ui.auth.data.model.User
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.lang.reflect.Member

class profileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var profileViewModel: profileViewModel
    private lateinit var mUser: User
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
     inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val binding: FragmentProfileBinding  = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false)

        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)
        val name = sharedPreferences.getString(getString(R.string.pref_name),"")
        Log.e("DataBase", "On create Error"+name)

        binding.profileName.text = name
        binding.btnLogout.setOnClickListener{
            logout(it)
        }
        binding.btnEditProfile.setOnClickListener{
            val intent = Intent(this.context, editProfile::class.java)
            // start your next activity
            startActivity(intent)
        }

        return binding.root
    }



    override fun onResume() {
        super.onResume()

        val name = sharedPreferences.getString(getString(R.string.pref_name),"")
      profileName.text = name

    }


    fun logout(view: View){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this.context, MainActivity::class.java)
        // start your next activity
        startActivity(intent)
    }

}