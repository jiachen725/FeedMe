package com.example.login.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.login.HomePage
import com.example.login.MainActivity
import com.example.login.R
import com.example.login.SignUp
import com.example.login.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*

class homeFragment : Fragment() {
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:  FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false)
        sharedPreferences = activity!!.getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        binding.pointTextView.text = sharedPreferences.getInt(getString(R.string.pref_point),0).toString()

        binding.btnSaveEnvirNow.setOnClickListener{
            open123(it)
        }

        return binding.root
    }

     fun open123(view: View){

        val intent = Intent(this.context, SignUp::class.java)
        // start your next activity
        startActivity(intent)
    }
    override fun onResume() {

        pointTextView.text = sharedPreferences.getInt(getString(R.string.pref_point),0).toString()

        super.onResume()
    }
}