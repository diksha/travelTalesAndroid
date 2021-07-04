package com.dktrips.traveltales.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dktrips.traveltales.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton

class AuthFragment : Fragment() {

    companion object {
        fun newInstance(): AuthFragment {
            return AuthFragment()
        }
    }

    private var googleSignInClient: GoogleSignInClient? = null
    private var googleSignInButton: SignInButton? = null
    private val rcSignIn = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signin, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            initSignInButton()
            initGoogleSignInClient(it)
        }
    }

    private fun initSignInButton() {
        googleSignInButton = activity?.findViewById<SignInButton>(R.id.sign_in_button)
        googleSignInButton?.setOnClickListener { v: View? -> signIn() }
    }


    private fun initGoogleSignInClient(activity: Activity) {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, googleSignInOptions)
    }

    private fun signIn() {
        val signInIntent: Intent? = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, rcSignIn)
    }
}