package com.ataglance.walletglance.auth.presentation.viewmodel

import com.ataglance.walletglance.auth.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(
    private val auth: FirebaseAuth
) {

    var user = User()
    private val firebaseUser: FirebaseUser? = auth.currentUser

    fun createNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.result.user?.let {
                    user = User(uid = it.uid)
                }
            }
    }

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                task.result.user?.let {
                    user = User(uid = it.uid)
                }
            }
    }

    fun signOut() {
        auth.signOut()
        user = User()
    }

}