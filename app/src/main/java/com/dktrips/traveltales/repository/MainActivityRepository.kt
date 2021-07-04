package com.dktrips.traveltales.repository

import com.dktrips.traveltales.model.TripStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class MainActivityRepository @Inject constructor(
) {

    val tripStatus = flow<TripStatus> {
        val ref = FirebaseFirestore.getInstance().collection("Trips")
        ref.add(hashMapOf("status" to 0, "userid" to "123"))
        val list = ref.get().await()
        list.documents
    }.flowOn(Dispatchers.IO)

}
