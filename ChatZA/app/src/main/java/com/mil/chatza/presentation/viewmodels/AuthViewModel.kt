package com.mil.chatza.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mil.chatza.domain.model.EmailAuthResult
import com.mil.chatza.domain.model.Failure
import com.mil.chatza.domain.model.FailureEmailAuth
import com.mil.chatza.domain.model.FailureLogin
import com.mil.chatza.domain.model.LoginResult
import com.mil.chatza.domain.model.Response
import com.mil.chatza.domain.model.SignUpResult
import com.mil.chatza.domain.model.Success
import com.mil.chatza.domain.model.SuccessEmailAuth
import com.mil.chatza.domain.model.SuccessLogin
import com.mil.chatza.domain.repository.AuthRepository
import com.mil.chatza.domain.repository.OneTapSignInResponse
import com.mil.chatza.domain.repository.SignInWithGoogleResponse
import com.mil.chatza.domain.repository.SignOutResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "AuthViewModel"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    val oneTapClient: SignInClient
) : ViewModel() {

    val auth = Firebase.auth

    //Continue With Google
    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Response.Success(null))
        private set
    var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse>(Response.Success(false))
        private set
    fun oneTapSignIn(){
        viewModelScope.launch {
            oneTapSignInResponse = Response.Loading
            oneTapSignInResponse = repo.oneTapSignInWithGoogle()
        }
    }

    //Sign In With Google
    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        oneTapSignInResponse = Response.Loading
        signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
    }

    //Sign Out
    var signOutResponse by mutableStateOf<SignOutResponse>(Response.Success(false))
        private set
    fun signOut() = viewModelScope.launch {
        signOutResponse = Response.Loading
        signOutResponse = repo.signOut()
    }

    //Sign Up
    private var _signUpException = MutableLiveData<Exception>()
    var signUpException: LiveData<Exception> = _signUpException
    suspend fun signUp(
        email: String,
        password: String
    ): SignUpResult {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Success(true)
        } catch (e: Exception) {
            _signUpException.value = e
            Failure(e)
        }
    }

    //Login
    private var _loginException = MutableLiveData<Exception>()
    var loginException: LiveData<Exception> = _loginException
    suspend fun logIn(email: String, password: String): LoginResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            SuccessLogin(true)
        } catch (e: Exception) {
            _loginException.value = e
            FailureLogin(e)
        }
    }

    //Email Authentication
    private var _emailAuthException = MutableLiveData<Exception>()
    var emailAuthException: LiveData<Exception> = _emailAuthException
    suspend fun authenticateEmail(): EmailAuthResult {
        return try {
            auth.currentUser!!.sendEmailVerification().await()
            SuccessEmailAuth(true)
        } catch (e: Exception) {
            _emailAuthException.value = e
            FailureEmailAuth(e)
        }
    }

}
