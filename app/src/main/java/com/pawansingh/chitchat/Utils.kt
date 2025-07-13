package com.pawansingh.chitchat

import com.google.firebase.auth.FirebaseAuth

object Utils {

    private var firebaseAuthInstance : FirebaseAuth? = null
    fun getFirebaseAuthInstance() : FirebaseAuth{
        if(firebaseAuthInstance == null){
            firebaseAuthInstance = FirebaseAuth.getInstance()
        }
        return firebaseAuthInstance!!

    }

    fun getUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }
}