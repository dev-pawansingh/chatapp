package com.pawansingh.chitchat.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.pawansingh.chitchat.R
import com.pawansingh.chitchat.Utils
import com.pawansingh.chitchat.models.MessageModel

class ChatAdapter(options: FirebaseRecyclerOptions<MessageModel>) :
    FirebaseRecyclerAdapter<MessageModel, ChatAdapter.ChatViewHolder>(options) {

    inner class ChatViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val leftHolder = itemView.findViewById<CardView>(R.id.leftHolder)
        val rightHolder = itemView.findViewById<CardView>(R.id.rightHolder)
        val leftMessage = itemView.findViewById<TextView>(R.id.leftChat)
        var rightMessage = itemView.findViewById<TextView>(R.id.rightChat)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_row,parent,false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int,
        model: MessageModel
    ) {
        if(model.senderId.equals(Utils.getUserId())){ // sender is me
            holder.leftHolder.visibility = View.GONE
            holder.rightHolder.visibility = View.VISIBLE
            holder.rightMessage.text = model.message
        }else{ // sender is person 2
            holder.leftHolder.visibility = View.VISIBLE
            holder.rightHolder.visibility = View.GONE
            holder.leftMessage.text = model.message

        }
    }
}