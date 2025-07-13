package com.pawansingh.chitchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.databinding.FragmentOTPBinding
import com.pawansingh.chitchat.viewmodels.AuthViewModel
import kotlinx.coroutines.launch


class OTPFragment : Fragment() {

    lateinit var binding : FragmentOTPBinding
    lateinit var number : String
    private val viewModel : AuthViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOTPBinding.inflate(layoutInflater)

        getUserNumber()
        Toast.makeText(requireContext(),"Sending OTP...",Toast.LENGTH_SHORT).show()
        sendOTP()
        onLoginButtonClicked()
        onBackPressed()
        return binding.root
    }

    private fun onBackPressed() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_OTPFragment_to_loginFragment)
        }
    }

    private fun onLoginButtonClicked() {
        binding.btnContinue.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            val otp = binding.etOtp.text.toString()
            if (otp.length < 6 || otp.length > 6){
                Toast.makeText(requireContext(),"Enter a valid OTP",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(),"Verifying OTP...",Toast.LENGTH_SHORT).show()
                verifyOTP(otp)
            }

        }
    }

    private fun verifyOTP(otp: String) {
        viewModel.signInWithPhoneAuthCredential(requireActivity(),otp)
        lifecycleScope.launch {
            viewModel.isSignedIn.collect{
                if(it){
                    Toast.makeText(requireContext(),"Login Successful",Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                    val bundle = Bundle()
                    bundle.putString("number",number)
                    findNavController().navigate(R.id.action_OTPFragment_to_userDetail,bundle)
                }else if(!it){
                    Toast.makeText(requireContext(),"Login Failed",Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun sendOTP() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.apply {
            sendOTP(number,requireActivity())
            lifecycleScope.launch {
                otpSent.collect{
                    if(it){
                        Toast.makeText(requireContext(),"OTP Sent",Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }

    }

    private fun getUserNumber() {
        val bundle = arguments
        if(bundle != null){
            number = bundle.getString("number").toString()
            binding.number.text = "+91 $number"
        }
    }
}