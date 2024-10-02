package com.ataglance.walletglance.core.presentation.viewmodel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(
    private val auth: FirebaseAuth
) {

    val user: FirebaseUser? = auth.currentUser

    fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

            }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

            }
    }

    fun signOut() {
        auth.signOut()
    }

}