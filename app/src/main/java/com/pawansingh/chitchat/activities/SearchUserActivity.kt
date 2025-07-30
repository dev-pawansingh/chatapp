package com.pawansingh.chitchat.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.pawansingh.chitchat.BaseActivity
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.adapters.SearchUserAdapter
import com.pawansingh.chitchat.databinding.ActivitySearchUserBinding
import com.pawansingh.chitchat.models.Users

class SearchUserActivity : BaseActivity() {

    private lateinit var binding : ActivitySearchUserBinding
    private lateinit var adapter : SearchUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.searchBtn.setOnClickListener{
            var searchText = binding.etSearch.text.toString()

            if(searchText.isEmpty()){
                binding.etSearch.error = "Please enter a name"
            }else{
                setupSearchRecyclerView(searchText)
            }
        }
    }

    private fun setupSearchRecyclerView(searchText: String) {
        val query = FirebaseDatabase.getInstance().getReference("AllUsers")
            .child("Users")
            .orderByChild("userName")
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")

        val option = FirebaseRecyclerOptions.Builder<Users>()
            .setQuery(query,Users::class.java)
            .build()

        adapter = SearchUserAdapter(option)
        binding.userList.layoutManager = LinearLayoutManager(this)
        binding.userList.adapter = adapter
        adapter.startListening()
    }

    override fun onStart() {
        super.onStart()
        if(::adapter.isInitialized) {
            adapter.startListening()
        }
    }

    override fun onStop() {
        super.onStop()
        if(::adapter.isInitialized) {
            adapter.stopListening()
        }
    }

}