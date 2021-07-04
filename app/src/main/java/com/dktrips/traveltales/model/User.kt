package com.dktrips.traveltales.model

import com.google.firebase.firestore.Exclude

class User(val uid: String?, val  name: String?, val email: String?) {

    @Exclude
    var isAuthenticated = false

    @Exclude
    var isNew = false

    @Exclude
    var isCreated = false
}