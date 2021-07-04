package com.dktrips.traveltales.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dktrips.traveltales.model.User
import com.dktrips.traveltales.repository.AuthRepository
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    var authenticatedUserLiveData: LiveData<User?>? = null
    var isLoggedIn : LiveData<Boolean> = MutableLiveData<Boolean>(false)

    fun signInWithGoogle(googleAuthCredential: AuthCredential?) {
        authRepository.firebaseSignInWithGoogle(googleAuthCredential)
        authenticatedUserLiveData = authRepository.userInfo.asLiveData()
    }

    fun checkIfUserIsLoggedIn() {
        isLoggedIn = authRepository.isUserLoggedIn().asLiveData()
    }

}