package com.ataglance.walletglance.auth.presentation.viewmodel

import com.ataglance.walletglance.auth.domain.User
import com.ataglance.walletglance.core.data.model.UserRemotePreferences
import com.ataglance.walletglance.core.mapper.toMap
import com.ataglance.walletglance.core.mapper.toUserRemotePreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class AuthViewModel(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    var user = User()

    private fun setUser(user: FirebaseUser) {
        this.user = User(uid = user.uid)
    }

    fun getUserId(): String? {
        return user.uid
    }

    private val userFirestoreRef: DocumentReference?
        get() = user.uid?.let { firestore.collection("usersPreferences").document(it) }

    suspend fun createNewUser(email: String, password: String, lang: String): Boolean {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        result.user?.let(::setUser) ?: return false

        val userPreferences = UserRemotePreferences(
            language = lang,
            subscription = user.subscription
        )

        userFirestoreRef?.set(userPreferences.toMap(), SetOptions.merge())?.await()
        return true
    }

    suspend fun signIn(email: String, password: String): UserRemotePreferences? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.let(::setUser) ?: return null

        return userFirestoreRef?.get()?.await()?.data?.toUserRemotePreferences()
    }

    fun signOut() {
        auth.signOut()
        user = User()
    }

}