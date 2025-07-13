package com.pawansingh.chitchat.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.pawansingh.chitchat.Utils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val _verificationId = MutableStateFlow<String?>(null)

    private val _otpSent = MutableStateFlow<Boolean>(false)
    val otpSent = _otpSent

    private val _isSignedIn = MutableStateFlow<Boolean>(false)
    val isSignedIn = _isSignedIn

    private val _isCurrentUser = MutableStateFlow<Boolean>(false)
    val isCurrentUser = _isCurrentUser

    init {
        Utils.getFirebaseAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }

    fun sendOTP(phoneNumber : String, activity : Activity){
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getFirebaseAuthInstance())
            .setPhoneNumber("+91$phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun signInWithPhoneAuthCredential(activity: Activity, otp : String) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        Utils.getFirebaseAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    _isSignedIn.value = true
                } else {

                }
            }
    }
}