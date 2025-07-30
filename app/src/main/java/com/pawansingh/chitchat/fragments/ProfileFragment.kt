package com.pawansingh.chitchat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.activities.AuthActivity
import com.pawansingh.chitchat.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    lateinit var binding : FragmentProfileBinding
    private var userId = Utils.getUserId()
    private var dbRef = FirebaseDatabase.getInstance().getReference("AllUsers/Users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        fetchUserDetails()
        binding.btnChangeName.setOnClickListener {
            binding.etHolder.visibility = View.VISIBLE
            binding.btnContinue.visibility = View.VISIBLE
            binding.btnChangeName.visibility = View.GONE
            binding.etUsername.requestFocus()

            binding.btnContinue.setOnClickListener {
                val newName = binding.etUsername.text.toString()
                if(newName.isEmpty()){
                    binding.etUsername.error = "User name cannot be empty"
                }else{
                    dbRef.child(userId).child("userName").setValue(newName)
                    binding.userName.text = newName
                    binding.etHolder.visibility = View.INVISIBLE
                    binding.btnContinue.visibility = View.GONE
                    binding.btnChangeName.visibility = View.VISIBLE
                }
            }
        }
        binding.logout.setOnClickListener {
            Utils.getFirebaseAuthInstance().signOut()
            startActivity(Intent(context, AuthActivity::class.java))
            activity?.finishAffinity()
        }

        return binding.root
    }

    private fun fetchUserDetails() {
        dbRef.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userName = snapshot.child("userName").value
                val phoneNumber = snapshot.child("phoneNumber").value

                binding.userName.text = userName.toString()
                binding.phNumber.text = "+91 $phoneNumber"
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }


}