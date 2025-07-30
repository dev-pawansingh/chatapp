package com.pawansingh.chitchat.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.pawansingh.chitchat.BaseActivity
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.databinding.ActivityMainBinding
import com.pawansingh.chitchat.fragments.ChatFragment
import com.pawansingh.chitchat.fragments.ProfileFragment

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction().replace(binding.frameLayout.id,ChatFragment()).commit()
        }

        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.chats -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id,ChatFragment()).commit()
                    true
                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction().replace(binding.frameLayout.id,ProfileFragment()).commit()
                    true
                }
                else -> false

            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.search -> {
                    // To be implemented
                    startActivity(Intent(this,SearchUserActivity::class.java))
                    true
                }
                else -> false
            }
        }
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val presenceRef = FirebaseDatabase.getInstance().getReference("presence/$userId")
        presenceRef.setValue(true)
        presenceRef.onDisconnect().setValue(false)
        getToken()
    }

    private fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->

            // eyFLiZ1kTXCC5ky5GH5h8W:APA91bHblgcopEEe1rdOBG4xRUTxhuQJHulVUu4I4Q5MqydXBk_4VhI7-ySeUMQopc9LcL8XjWFOmFp-wZarKOpMVx4sDH6UrQwm1qDvyQzlw5uS84kISdM
            val token = task.result
            FirebaseDatabase.getInstance().getReference().child("AllUsers/Users/${Utils.getUserId()}")
                .child("token").setValue(token)
        })
    }
}