package com.mil.chatza.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.core.utils.Consts.Companion.profileImages
import com.mil.chatza.domain.model.FailureGsUrl
import com.mil.chatza.domain.model.FailureImageUpload
import com.mil.chatza.domain.model.FailureUserUpload
import com.mil.chatza.domain.model.GsUrlResult
import com.mil.chatza.domain.model.SuccessGsUrl
import com.mil.chatza.domain.model.SuccessImageUpload
import com.mil.chatza.domain.model.SuccessUserUpload
import com.mil.chatza.domain.model.UploadImageResult
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

    //Upload Profile Image
    private var _imageUploadException = MutableLiveData<Exception>()
    var imageUploadException : LiveData<Exception> = _imageUploadException
    private var _imageUrl = MutableLiveData<String>()
    var imageUrl : LiveData<String> =  _imageUrl
    suspend fun uploadImageToFirebaseStorage(imageUri: Uri?) : UploadImageResult {
        if (imageUri == null){
            return SuccessImageUpload(true)
        }
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("$profileImages/${imageUri.lastPathSegment}")
        val uploadTask = imagesRef.putFile(imageUri)
        val imageUrl = imagesRef.getFile(imageUri)
        return try {
            uploadTask.await()
            _imageUrl.value = imagesRef.toString()
            SuccessImageUpload(true)
        }  catch (e : Exception){
            _imageUploadException.value = e
            FailureImageUpload(e)
        }
    }

    //GsUrl
    private var _gsImageUrlException = MutableLiveData<Exception>()
    var gsImageUrlException : LiveData<Exception> = _gsImageUrlException
    private var _gsImageUrl = MutableLiveData<String>()
    var gsImageUrl : LiveData<String> = _gsImageUrl
   /* private suspend fun getDownloadUrlFromGsUrl(gsUrl: String): GsUrlResult {
        return try {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReferenceFromUrl(gsUrl)
            val downloadUrl = storageRef.downloadUrl
            _gsImageUrl.value = downloadUrl.await().toString()
            return SuccessGsUrl(true)
        }catch (e : Exception){
            _gsImageUrlException.value = e
            FailureGsUrl(e)
        }
    }*/
   suspend fun getDownloadUrlFromGsUrl(gsUrl: String): String {
       val storage = FirebaseStorage.getInstance()
       val storageRef = storage.getReferenceFromUrl(gsUrl)
       val downloadUrl = storageRef.downloadUrl
       return downloadUrl.await().toString()
   }

    //Get Profile Details
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
    fun replaceEncodedColon(input: String): String {
        return input.replace("%3A", ":")
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
