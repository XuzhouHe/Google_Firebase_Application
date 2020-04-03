package com.example.cse438.cse438_assignment4.ViewModel


import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

/**
 * ViewModel for [com.google.firebase.example.fireeats.MainActivity].
 */

class MainActivityViewModel : ViewModel() {

    var isSigningIn: Boolean = false
    var currentUser: FirebaseUser? = null



}
