package com.dktrips.traveltales.repository

import android.util.Log
import com.dktrips.traveltales.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class AuthRepository @Inject constructor() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreRef = FirebaseFirestore.getInstance()

    var userInfo = MutableStateFlow<User?>(null)
    var isLoggedIn = MutableStateFlow<Boolean>(false)

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?): MutableStateFlow<User?> {
        val coroutineScope = CoroutineScope(Job() + Dispatchers.IO)
        coroutineScope.launch {
            firebaseAuth.signInWithCredential(googleAuthCredential!!)
                .addOnCompleteListener { authTask: Task<AuthResult> ->
                    if (authTask.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        if (firebaseUser != null) {
                            createInDbIfNotExists(firebaseUser)
                            isLoggedIn.tryEmit(true)
                        }
                    } else {
//                    logErrorMessage(authTask.exception!!.message)
                    }
                }
        }
        return userInfo
    }

    private fun createInDbIfNotExists(firebaseUser: FirebaseUser) {
        val doc = firestoreRef.collection("Users").document(firebaseUser.uid)
        doc.get().addOnSuccessListener {
            if (!it.exists()) {
                val newUser = User(firebaseUser.uid, firebaseUser.displayName, firebaseUser.email)
                doc.set(newUser).addOnSuccessListener {
                    userInfo.tryEmit(newUser)
                }
            } else {
                userInfo.tryEmit(
                    User(
                        it.get("uid").toString(),
                        it.get("name").toString(),
                        it.get("email").toString()
                    )
                )
            }
        }
    }

    fun isUserLoggedIn(): MutableStateFlow<Boolean> {
        val firebaseUser = firebaseAuth.currentUser
        isLoggedIn.tryEmit(firebaseUser!=null)
        return isLoggedIn
    }
}