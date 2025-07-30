package com.pawansingh.chitchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

open class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance()
            .getReference("presence/${Utils.getUserId()}")
            .setValue(true)
    }

    override fun onPause() {
        super.onPause()
        FirebaseDatabase.getInstance()
            .getReference("presence/${Utils.getUserId()}")
            .setValue(false)
    }
}
