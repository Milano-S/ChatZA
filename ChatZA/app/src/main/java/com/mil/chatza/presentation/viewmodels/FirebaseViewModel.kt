package com.mil.chatza.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mil.chatza.core.utils.Consts
import com.mil.chatza.core.utils.Consts.Companion.profileImages
import com.mil.chatza.domain.model.Chat
import com.mil.chatza.domain.model.FailureChatUpload
import com.mil.chatza.domain.model.FailureImageUpload
import com.mil.chatza.domain.model.FailureUserUpload
import com.mil.chatza.domain.model.Message
import com.mil.chatza.domain.model.SuccessChatUpload
import com.mil.chatza.domain.model.SuccessImageUpload
import com.mil.chatza.domain.model.SuccessUserUpload
import com.mil.chatza.domain.model.UploadChatResult
import com.mil.chatza.domain.model.UploadImageResult
import com.mil.chatza.domain.model.UploadUserResult
import com.mil.chatza.domain.model.UserProfile
import com.mil.chatza.domain.repository.UserProfileRepositoryImp.Companion.email
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseViewModel"
class FirebaseViewModel : ViewModel() {

    private val firebaseDb = Firebase.firestore
    private val firebaseUsers = firebaseDb.collection(Consts.users)
    private val firebaseChats = firebaseDb.collection(Consts.chats)

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

    //Upload Chat
    private var _chatUploadException = MutableLiveData<Exception>()
    var charUploadException : LiveData<Exception> = _userUploadException
    suspend fun uploadChat(chat : Chat) : UploadChatResult {
        return try {
            firebaseDb.collection(Consts.chats)
                .add(chat)
                .await()
            Log.i(TAG, chat.chatName)
            SuccessChatUpload(true)
        } catch (e : Exception){
            Log.i(TAG, e.message.toString())
            _chatUploadException.value = e
            FailureChatUpload(e)
        }
    }

    suspend fun deleteAccount(user: UserProfile) {
        val userDataPath = getUserId(user)
        firebaseUsers.document(userDataPath).get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(UserProfile::class.java)
                if (userData != null) {
                    //Delete User Profile
                    try {
                        firebaseUsers.document(userDataPath).delete()
                    } catch (e: Exception) {
                        Log.i(TAG, e.message.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                _editUserException.value = exception
                Log.i(TAG, exception.message.toString())
            }
    }

     fun deleteProfileImage(gsUrl: String) {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl(gsUrl)
        storageRef.delete()
            .addOnSuccessListener {
                Log.i(TAG, "Profile Image Delete Successful")
            }.addOnFailureListener { e ->
                Log.i(TAG, e.message.toString())
            }
    }

    //Edit User
    private var _editUserException = MutableLiveData<Exception>()
    var editUserException: LiveData<Exception> = _editUserException
    suspend fun editUserDetails(oldUserDetails: UserProfile, newUserDetails: UserProfile) {
        val userDataPath = getUserId(oldUserDetails)
        firebaseUsers.document(userDataPath).get()
            .addOnSuccessListener { document ->
                val userData = document.toObject(UserProfile::class.java)
                if (userData != null) {
                    //Edit User Details
                    try {
                        firebaseUsers.document(userDataPath).update(
                            "age", newUserDetails.age,
                            "gender", newUserDetails.gender,
                            "name", newUserDetails.name,
                            "profileImageUrl", newUserDetails.profileImageUrl,
                            "province", newUserDetails.province
                        )
                    } catch (e: Exception) {
                        Log.i(TAG, e.message.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                _editUserException.value = exception
                Log.i(TAG, exception.message.toString())
            }
    }

    private suspend fun getUserId(userEdit: UserProfile): String {
        var userId = ""
        val userList = firebaseUsers.get().await()
        userList.forEach { user ->
            val currentUser = user.toObject(UserProfile::class.java)
            if (currentUser.email == userEdit.email) {
                userId = user.id
            }
        }
        return userId
    }


    //Upload Profile Image
    private var _imageUploadException = MutableLiveData<Exception>()
    var imageUploadException: LiveData<Exception> = _imageUploadException
    private var _imageUrl = MutableLiveData<String>()
    var imageUrl: LiveData<String> = _imageUrl
    suspend fun uploadImageToFirebaseStorage(imageUri: Uri?): UploadImageResult {
        if (imageUri == null) {
            return SuccessImageUpload(true)
        }
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("$profileImages/${imageUri.lastPathSegment}")
        val uploadTask = imagesRef.putFile(imageUri)
        return try {
            uploadTask.await()
            _imageUrl.value = imagesRef.toString()
            SuccessImageUpload(true)
        } catch (e: Exception) {
            _imageUploadException.value = e
            FailureImageUpload(e)
        }
    }

    //GsUrl
    suspend fun getDownloadUrlFromGsUrl(gsUrl: String): String {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl(gsUrl)
        val downloadUrl = storageRef.downloadUrl
        return downloadUrl.await().toString()
    }

    //Get Profile Details
    private var _currentProfileDetails = MutableLiveData<UserProfile>()
    var currentProfileDetails: LiveData<UserProfile> = _currentProfileDetails
    private fun setCurrentProfileDetails(userDetails : UserProfile){
            _currentProfileDetails.value = userDetails
    }
    suspend fun getProfileDetails(email: String): UserProfile {
        var profileDetails = UserProfile()
        val userList = firebaseUsers.get().await()
        userList.forEach { user ->
            val currentUser = user.toObject(UserProfile::class.java)
            if (currentUser.email == email) {
                profileDetails = currentUser
                setCurrentProfileDetails(profileDetails)
            }
        }
        return profileDetails
    }

    //Get Chat Details
    suspend fun getChatDetails(chatName: String): Chat {
        var chatDetails = Chat()
        try {
            val chatList = firebaseChats.get().await()
            chatList.forEach { chat ->
                val currentChat = chat.toObject(Chat::class.java)
                Log.i(TAG, currentChat.chatName + " " + currentChat.lastMessage)
                if (currentChat.chatName == chatName) {
                    chatDetails = currentChat
                }
            }
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
        }
        return chatDetails
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
