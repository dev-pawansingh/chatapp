package com.pawansingh.chitchat.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.activities.ChatActivity
import com.pawansingh.chitchat.models.Users

class SearchUserAdapter(options: FirebaseRecyclerOptions<Users>) :
    FirebaseRecyclerAdapter<Users, SearchUserAdapter.UserViewHolder>(options) {

    inner class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.user_name)
        val userNumber = itemView.findViewById<TextView>(R.id.user_number)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_row,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
        model: Users
    ) {
        holder.userNumber.text = "+91 " + model.phoneNumber
        if(model.uid.equals(Utils.getUserId())){
            holder.userName.text = model.userName + " (You)"
        }else{
            holder.userName.text = model.userName
        }

        holder.itemView.setOnClickListener{
            // navigate to the chatting page
            val intent = Intent(holder.itemView.context,ChatActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("userName",model.userName)
            intent.putExtra(("userId"),model.uid)
            holder.itemView.context.startActivity(intent)
        }

    }
}