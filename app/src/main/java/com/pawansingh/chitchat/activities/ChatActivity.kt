package com.pawansingh.chitchat.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.Timestamp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Query
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.adapters.ChatAdapter
import com.pawansingh.chitchat.adapters.SearchUserAdapter
import com.pawansingh.chitchat.databinding.ActivityChatBinding
import com.pawansingh.chitchat.models.ChatRoomModel
import com.pawansingh.chitchat.models.MessageModel
import com.pawansingh.chitchat.models.Users
import kotlin.String

class ChatActivity : AppCompatActivity() {

    lateinit var binding : ActivityChatBinding
    var chatRoomId : String = ""
    var user2Id : String = ""
    var adapter : ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()
        createGetChatRoom()
        setupMessageAdapter()
        binding.sendBtn.setOnClickListener {
            var message : String = binding.message.text.toString()
            if (message.isEmpty()){
                binding.message.error = "Enter a message before sending"
            }else{
                sendMessage(message)
            }
        }


    }

    fun setupMessageAdapter(){
        val query = FirebaseDatabase.getInstance().getReference("ChatRoom").child(chatRoomId).child("messages")
            .orderByChild("timeStamp/seconds")


        val option = FirebaseRecyclerOptions.Builder<MessageModel>()
            .setQuery(query, MessageModel::class.java)
            .build()

        adapter = ChatAdapter(option)
        binding.messageList.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.messageList.adapter = adapter
        adapter?.startListening()
    }

    fun sendMessage(message : String){
//        val chatRoom = ChatRoomModel(lastMessageTimeStamp = Timestamp.now() , lastMessageSenderId = Utils.getUserId())
//        FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").setValue(chatRoom)
//
//        val messageModel : MessageModel = MessageModel(message,Utils.getUserId(), Timestamp.now())
//        FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").child("messages").push().setValue(messageModel)
//        binding.message.setText("")

        val chatRoomRef = FirebaseDatabase.getInstance().getReference("ChatRoom").child(chatRoomId)

        // Update last message time and sender only
        chatRoomRef.child("lastMessageTimeStamp").setValue(System.currentTimeMillis())
        chatRoomRef.child("lastMessageSenderId").setValue(Utils.getUserId())

        val messageModel = MessageModel(message,Utils.getUserId(), System.currentTimeMillis())
        chatRoomRef.child("messages").push().setValue(messageModel)
        binding.message.setText("")

    }

    fun setupToolbar(){
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        val otherUser = intent.getStringExtra("userName")
        binding.toolbar.title = otherUser
    }

    fun createGetChatRoom(){
        user2Id = intent.getStringExtra("userId").toString()
        chatRoomId = getChatRoomId(Utils.getUserId(),user2Id)
        FirebaseDatabase.getInstance().getReference("ChatRoom").child(chatRoomId).get()
            .addOnSuccessListener { snapshot ->
                if(snapshot.exists()){
                    // chatroom exists
                }else{
                    // create new chatroom
                    val userIds = ArrayList<String>()
                    userIds.add(Utils.getUserId())
                    userIds.add(user2Id)
                    val chatRoom = ChatRoomModel(chatRoomId,userIds, System.currentTimeMillis(),"")
                    FirebaseDatabase.getInstance().getReference("ChatRoom").child("$chatRoomId").setValue(chatRoom)
                }

            }.addOnFailureListener {
            }

    }

    private fun getChatRoomId(u1 : String, u2 : String) : String{
        return if(u1.hashCode() < u2.hashCode()){
            u1 + "_" + u2
        }else{
            u2 + "_" + u1
        }
    }

}