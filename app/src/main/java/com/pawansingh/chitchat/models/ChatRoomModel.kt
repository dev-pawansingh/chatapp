package com.pawansingh.chitchat.models

data class ChatRoomModel(
    val chatRoomId: String = "",
    val userIds: List<String> = ArrayList<String>(),
    val lastMessageTimeStamp : Long = System.currentTimeMillis(),
    val lastMessageSenderId: String = ""
)
