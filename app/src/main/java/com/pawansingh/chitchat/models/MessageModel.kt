package com.pawansingh.chitchat.models

data class MessageModel(
    val message : String = "",
    val senderId : String = "",
    val timeStamp : Long = System.currentTimeMillis()
)