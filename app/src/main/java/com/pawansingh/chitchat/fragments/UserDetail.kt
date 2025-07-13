package com.pawansingh.chitchat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.activities.MainActivity
import com.pawansingh.chitchat.databinding.FragmentUserDetailBinding
import com.pawansingh.chitchat.models.Users

class UserDetail : Fragment() {

    private lateinit var binding : FragmentUserDetailBinding
    lateinit var firestore : FirebaseFirestore

    var username = ""
    var userNumber = ""
    var userId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailBinding.inflate(layoutInflater)
        firestore = FirebaseFirestore.getInstance()

        getDetails()

        return binding.root
    }

    private fun getDetails() {
        binding.btnContinue.setOnClickListener {
            username = binding.etUsername.text.toString()
            if(username.length == 0){
                binding.etUsername.error = "User name cannot be empty"
            }else{
                val user = Users(userId,userNumber,username)
                FirebaseDatabase.getInstance().getReference("AllUsers").child("Users").child(userId).setValue(user)
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finishAffinity()
            }

        }
        val bundle = arguments
        userNumber = bundle!!.getString("number").toString()
        userId = Utils.getUserId()

    }


}