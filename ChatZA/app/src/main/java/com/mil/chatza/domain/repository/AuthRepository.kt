package com.mil.chatza.domain.repository

import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.mil.chatza.domain.model.Response


typealias OneTapSignInResponse = Response<BeginSignInResult>
typealias SignInWithGoogleResponse = Response<Boolean>
typealias SignOutResponse = Response<Boolean>
typealias SendPasswordResetEmailResponse = Response<Boolean>

interface AuthRepository {

    val currentUser: FirebaseUser?
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): OneTapSignInResponse
    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): SignInWithGoogleResponse
    suspend fun signOut(): SignOutResponse
    suspend fun sendPasswordResetEmail(email: String): SendPasswordResetEmailResponse

}