package com.pawansingh.chitchat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.util.Util
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.adapters.RecentChatAdapter
import com.pawansingh.chitchat.databinding.FragmentChatBinding
import com.pawansingh.chitchat.models.ChatRoomModel


class ChatFragment : Fragment() {

    lateinit var binding: FragmentChatBinding
    lateinit var adapter : RecentChatAdapter
    private val userNameMap = HashMap<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        fetchAllUserNames {
            setupSearchRecyclerView()
        }

        return binding.root
    }

    private fun fetchAllUserNames(onComplete: () -> Unit){
        FirebaseDatabase.getInstance().getReference("AllUsers/Users")
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (userSnapshot in dataSnapshot.children){
                    val userId = userSnapshot.key ?: continue
                    val userName = userSnapshot.child("userName").value as? String ?: "Unknown User"
                    userNameMap[userId] = userName
                }
                onComplete()
            }
    }

    private fun setupSearchRecyclerView() {
        FirebaseDatabase.getInstance().getReference("UserChatRooms")
            .child(Utils.getUserId())
            .get().addOnSuccessListener { dataSnapshot ->
                val chatRoomIds = mutableListOf<String>()
                for (roomSnapshot in dataSnapshot.children){
                    chatRoomIds.add(roomSnapshot.key!!)
                }

                val query = FirebaseDatabase.getInstance().getReference("ChatRoom")
                    .orderByChild("lastMessageTimeStamp")

                val option = FirebaseRecyclerOptions.Builder<ChatRoomModel>()
                    .setQuery(query, ChatRoomModel::class.java)
                    .build()

                adapter = RecentChatAdapter(option,userNameMap,chatRoomIds)
                binding.recentChats.layoutManager = LinearLayoutManager(context).apply {
                    reverseLayout = false
//                    stackFromEnd = true
                }
                binding.recentChats.adapter = adapter
                adapter.startListening()
            }
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

    override fun onResume() {
        super.onResume()
        if(::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }
}

