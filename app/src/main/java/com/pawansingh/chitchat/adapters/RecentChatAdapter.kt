package com.pawansingh.chitchat.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.activities.ChatActivity
import com.pawansingh.chitchat.models.ChatRoomModel

class RecentChatAdapter(options: FirebaseRecyclerOptions<ChatRoomModel>,
    private val userNameMap : Map<String, String>,
    private val chatRoomIds : List<String>) :
    FirebaseRecyclerAdapter<ChatRoomModel, RecentChatAdapter.RecentChatViewHolder>(options) {

    inner class RecentChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val lastMessage = itemView.findViewById<TextView>(R.id.lastText)
        val time = itemView.findViewById<TextView>(R.id.time)
        val isOnline = itemView.findViewById<TextView>(R.id.isOnline)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecentChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recent_user_row,parent,false)
        return RecentChatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RecentChatViewHolder,
        position: Int,
        model: ChatRoomModel
    ) {
        //need to use filter
        if(!chatRoomIds.contains(model.chatRoomId)){
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0,0)
            return
        }else{
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val currentUserId = Utils.getUserId()
        val otherUserId = model.userIds.first{it != currentUserId}

        val userName = userNameMap[otherUserId]?: "Unknown User"
        holder.userName.text = userName

        FirebaseDatabase.getInstance().getReference("ChatRoom").child(model.chatRoomId)
            .child("messages")
            .limitToLast(1)
            .get()
            .addOnSuccessListener { dataSnapshot ->

                var isMessageByMe = model.lastMessageSenderId.equals(Utils.getUserId())

                if (dataSnapshot.exists()){
                    for (messageSnapshot in dataSnapshot.children){
                        val message = messageSnapshot.child("message").value as? String ?: ""
                        if(isMessageByMe){
                            holder.lastMessage.text = "You : $message"
                        }else{
                            holder.lastMessage.text = message
                        }
                    }

                }else{
                    holder.lastMessage.text = "Say Hii 👋"
                }
            }

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context,ChatActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userName",userName)
            intent.putExtra(("userId"),otherUserId)
            holder.itemView.context.startActivity(intent)
        }
        FirebaseDatabase.getInstance().getReference("presence")
            .child(otherUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val isUserOnline = snapshot.getValue(Boolean::class.java) ?: false
                    holder.isOnline.visibility = if (isUserOnline) View.VISIBLE else View.INVISIBLE
                }

                override fun onCancelled(error: DatabaseError) {
                    // Optionally log the error
                }
            })

        val timeInMillis = model.lastMessageTimeStamp
        val date = java.text.SimpleDateFormat("hh:mm a" , java.util.Locale.getDefault())
        holder.time.text = date.format(java.util.Date(-timeInMillis))
    }
}