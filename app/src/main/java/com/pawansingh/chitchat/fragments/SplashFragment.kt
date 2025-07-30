package com.pawansingh.chitchat.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.activities.ChatActivity
import com.pawansingh.chitchat.activities.MainActivity
import com.pawansingh.chitchat.databinding.FragmentSplashBinding
import com.pawansingh.chitchat.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)

        val otherUserId = arguments?.getString("userId")

        if(otherUserId.isNullOrBlank()){
            // User opened the app
            Handler(Looper.getMainLooper()).postDelayed({

                lifecycleScope.launch {
                    viewModel.isCurrentUser.collect{
                        if(it){
                            startActivity(Intent(requireContext(), MainActivity::class.java))
                            requireActivity().finishAffinity()
                        }else{
                            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                        }
                    }
                }
            },2500)
        }else{
            // User entered through notification
            val userName = arguments?.getString("userName") ?: "Unknown user"
            val intent = Intent(requireContext(), ChatActivity::class.java).apply {
                putExtra("userId", otherUserId)
                putExtra("userName", userName)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            }
            startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }

}