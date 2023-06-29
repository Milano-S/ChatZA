package com.mil.chatza.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.domain.model.FailureUserUpload
import com.mil.chatza.domain.model.SuccessUserUpload
import com.mil.chatza.domain.model.UploadUserResult
import com.mil.chatza.domain.model.UserProfile
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseViewModel"

class FirebaseViewModel : ViewModel() {

    private val firebaseDb = Firebase.firestore
    private val firebaseUsers = firebaseDb.collection(Consts.users)

    //Upload User
    private var _userUploadException = MutableLiveData<Exception>()
    var userUploadException: LiveData<Exception> = _userUploadException
    suspend fun uploadUser(user: UserProfile): UploadUserResult {
        return try {
            firebaseDb.collection(Consts.users)
                .add(user)
                .await()
            SuccessUserUpload(true)
        } catch (e: Exception) {
            _userUploadException.value = e
            FailureUserUpload(e)
        }
    }

    suspend fun getProfileDetails(email: String) : UserProfile {
        var profileDetails = UserProfile()
        val userList = firebaseUsers.get().await()
        userList.forEach { user ->
            val currentUser = user.toObject(UserProfile::class.java)
            if (currentUser.email == email) {
                profileDetails = currentUser
            }
        }
        return profileDetails
    }

    //Check if Email in Use
    suspend fun isEmailInUse(email: String): Boolean {
        var isEmailInUse = false
        val userList = firebaseUsers.get().await()
        userList.forEach { userProfile ->
            val currentUser = userProfile.toObject(UserProfile::class.java)
            if (currentUser.email == email) {
                isEmailInUse = true
            }
        }
        return isEmailInUse
    }

}

