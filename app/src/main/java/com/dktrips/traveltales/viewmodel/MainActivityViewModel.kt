package com.dktrips.traveltales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dktrips.traveltales.repository.MainActivityRepository
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainActivityRepository: MainActivityRepository): ViewModel() {
    val tripStatus = mainActivityRepository.tripStatus.asLiveData()
}