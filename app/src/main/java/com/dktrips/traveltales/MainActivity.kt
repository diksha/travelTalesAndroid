package com.dktrips.traveltales

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dktrips.traveltales.ui.mapbox.MapFragment
import com.dktrips.traveltales.fragments.AuthFragment
import com.dktrips.traveltales.viewmodel.AuthViewModel
import com.dktrips.traveltales.viewmodel.MainActivityViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.mapbox.mapboxsdk.Mapbox
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val createMapFragment: Boolean = true
    private val rcSignIn = 1
    private val viewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private val authViewModel by lazy {
        ViewModelProvider(this)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this);
        Mapbox.getInstance(this, getString(R.string.mapbox_access_token))

        checkUserAuthentication();
        viewModel.tripStatus.observe(this, Observer { _ ->
            Toast.makeText(applicationContext, "Status changed", Toast.LENGTH_LONG)
        })
        if (createMapFragment) {
            supportFragmentManager.beginTransaction().add(R.id.map_fragment_holder, MapFragment())
                .commit()

        }
    }

    private fun checkUserAuthentication() {
        authViewModel.checkIfUserIsLoggedIn()
        authViewModel.isLoggedIn.observe(this, { isLoggedIn ->
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            supportFragmentManager.fragments.forEach {
                fragmentTransaction.remove(it)
            }
            if (!isLoggedIn) {
                fragmentTransaction.add(R.id.main_content, AuthFragment.newInstance())
            }
            fragmentTransaction.commit()
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rcSignIn) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount: GoogleSignInAccount? =
                    task.getResult(ApiException::class.java)
                if (googleSignInAccount != null) {
                    getGoogleAuthCredential(googleSignInAccount)
                }
            } catch (e: ApiException) {
// TODO(dikshag) add error message
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
        Log.i("dikshag", "credentials received")
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        authViewModel.signInWithGoogle(googleAuthCredential)
        authViewModel.authenticatedUserLiveData?.observe(this) { authenticatedUser ->
            if (authenticatedUser != null) {

            }
//            if (authenticatedUser.isNew) {
////                createNewUser(authenticatedUser)
//            } else {
////                goToMainActivity(authenticatedUser)
//            }
        }
    }

}