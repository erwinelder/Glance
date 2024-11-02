package com.ataglance.walletglance.auth.presentation.viewmodel

import com.ataglance.walletglance.auth.domain.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val auth: FirebaseAuth
) {

    var user = User()

    private fun setUser(user: FirebaseUser) {
        this.user = User(uid = user.uid)
    }

    suspend fun createNewUser(email: String, password: String) {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let(::setUser)
    }

    suspend fun signIn(email: String, password: String) {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.let(::setUser)
    }

    fun signOut() {
        auth.signOut()
        user = User()
    }

}