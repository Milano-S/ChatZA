package com.mil.chatza.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mil.chatza.domain.model.EmailAuthResult
import com.mil.chatza.domain.model.Failure
import com.mil.chatza.domain.model.FailureEmailAuth
import com.mil.chatza.domain.model.FailureLogin
import com.mil.chatza.domain.model.LoginResult
import com.mil.chatza.domain.model.SignUpResult
import com.mil.chatza.domain.model.Success
import com.mil.chatza.domain.model.SuccessEmailAuth
import com.mil.chatza.domain.model.SuccessLogin
import kotlinx.coroutines.tasks.await

private const val TAG = "AuthViewModel"

class AuthViewModel : ViewModel() {

    val auth = Firebase.auth

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
    var emailAuthException: LiveData<Exception> = _signUpException
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
