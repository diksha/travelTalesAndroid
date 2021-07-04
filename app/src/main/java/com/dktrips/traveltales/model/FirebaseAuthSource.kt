package com.dktrips.traveltales.model

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthSource {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
}