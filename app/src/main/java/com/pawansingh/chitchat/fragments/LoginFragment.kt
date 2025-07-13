package com.pawansingh.chitchat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {


    lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        binding.btnContinue.setOnClickListener {

            val number = binding.etNumber.text.toString()

            if(number.length == 0 || number.length != 10){
                Toast.makeText(requireContext(),"Enter a valid number",Toast.LENGTH_SHORT).show()
            }else{
                val bundle = Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_loginFragment_to_OTPFragment,bundle)
            }

        }
        return binding.root
    }

}