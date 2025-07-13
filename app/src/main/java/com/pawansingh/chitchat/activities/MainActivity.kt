package com.pawansingh.chitchat.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.databinding.ActivityMainBinding
import com.pawansingh.chitchat.fragments.ChatFragment
import com.pawansingh.chitchat.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

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
    }
}