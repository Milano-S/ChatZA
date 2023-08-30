package com.mil.chatza.presentation.viewmodels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "FirebaseViewModel"

class FirebaseViewModel : ViewModel() {

    private val firebaseDb = Firebase.firestore
    private val firebaseUsers = firebaseDb.collection(Consts.users)
    private val firebaseChats = firebaseDb.collection(Consts.chats)

    suspend fun getAllChats(): List<Chat> {
        val chats = mutableListOf<Chat>()
        val chatDocuments = firebaseChats.get().await()
        chatDocuments.forEach { chatDocument ->
            val currentChat = chatDocument.toObject(Chat::class.java)
            chats.add(currentChat)
        }
        return chats
    }

    suspend fun doesChatExist(chatName: String): Boolean {
        val chatList = firebaseChats.get().await()
        chatList.forEach { chat ->
            val chatData = chat.toObject(Chat::class.java)
            if (chatData.chatName == chatName) {
                return true
            }
        }
        return false
    }

    suspend fun doesFriendChatExist(friend1: String, friend2: String): Boolean {
        val chatList = firebaseChats.get().await()
        chatList.forEach { chat ->
            val chatData = chat.toObject(Chat::class.java)
            if (chatData.chatName.contains(friend1) && chatData.chatName.contains(friend2)) {
                return true
            }
        }
        return false
    }


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

    //Upload Friend Chat
    private var _friendChatUploadException = MutableLiveData<Exception>()
    var friendChatUploadException : LiveData<Exception> = _userUploadException
    suspend fun uploadFriendChat(chat: Chat): UploadChatResult {
        chat.isFriendChat = true
        return try {
            firebaseDb.collection(Consts.chats)
                .add(chat)
                .await()
            Log.i(TAG, chat.chatName)
            SuccessChatUpload(true)
        } catch (e: Exception) {
            Log.i(TAG, e.message.toString())
            _chatUploadException.value = e
            FailureChatUpload(e)
        }
    }

    //Upload Chat
    private var _chatUploadException = MutableLiveData<Exception>()
    var chatUploadException: LiveData<Exception> = _userUploadException
    suspend fun uploadChat(chat: Chat): UploadChatResult {
        return try {
            firebaseDb.collection(Consts.chats)
                .add(chat)
                .await()
            Log.i(TAG, chat.chatName)
            SuccessChatUpload(true)
        } catch (e: Exception) {
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
                            "province", newUserDetails.province,
                            "chatGroups", newUserDetails.chatGroups
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

    //Send Message
    suspend fun sendMessage(chatDetails: Chat, newMessage: Message) {
        val chatDataPath = getChatId(chatDetails)
        val messageList = (chatDetails.messages as MutableList).apply { add(newMessage) }
        firebaseChats.document(chatDataPath).get()
            .addOnSuccessListener { document ->
                val chatData = document.toObject(Chat::class.java)
                if (chatData != null) {
                    //Edit User Details
                    try {
                        firebaseChats.document(chatDataPath).update(
                            "messages", messageList
                        )
                    } catch (e: Exception) {
                        Log.i(TAG, e.message.toString())
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.i(TAG, e.message.toString())
            }
    }

    //Edit Chat Details
    suspend fun joinChatGroup(chatDetails: Chat, userDetails: UserProfile?) {
        val chatDataPath = getChatId(chatDetails)
        val participantList = (chatDetails.participants as MutableList).apply { add(userDetails!!) }
        firebaseChats.document(chatDataPath).get()
            .addOnSuccessListener { document ->
                val chatData = document.toObject(Chat::class.java)
                if (chatData != null) {
                    //Edit User Details
                    try {
                        firebaseChats.document(chatDataPath).update("participants", participantList)
                        val newUserGroupList =
                            (userDetails!!.chatGroups as MutableList).apply { add(chatDetails.chatName) }
                        viewModelScope.launch {
                            editUserDetails(
                                oldUserDetails = userDetails,
                                newUserDetails = UserProfile(
                                    dateCreated = userDetails.dateCreated,
                                    email = userDetails.email,
                                    name = userDetails.name,
                                    age = userDetails.age,
                                    gender = userDetails.gender,
                                    province = userDetails.province,
                                    profileImageUrl = userDetails.profileImageUrl,
                                    chatGroups = newUserGroupList,
                                )
                            )
                        }
                    } catch (e: Exception) {
                        Log.i(TAG, e.message.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
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

    private suspend fun getChatId(chatEdit: Chat): String {
        var chatId = ""
        val chatList = firebaseChats.get().await()
        chatList.forEach { chat ->
            val currentChat = chat.toObject(Chat::class.java)
            if (currentChat.chatName == chatEdit.chatName) {
                chatId = chat.id
            }
        }
        return chatId
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
    private fun setCurrentProfileDetails(userDetails: UserProfile) {
        _currentProfileDetails.value = userDetails
    }

    suspend fun getProfileDetailsFromName(userName: String): UserProfile {
        var profileDetails = UserProfile()
        val userList = firebaseUsers.get().await()
        userList.forEach { user ->
            val currentUser = user.toObject(UserProfile::class.java)
            if (currentUser.name == userName) {
                profileDetails = currentUser
                setCurrentProfileDetails(profileDetails)
            }
        }
        return profileDetails
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
    private val _currentChatDetails = MutableLiveData<Chat>()
    var currentChatDetails: LiveData<Chat> = _currentChatDetails
    suspend fun getChatDetails(chatName: String): Chat {
        var chatDetails = Chat()
        try {
            val chatList = firebaseChats.get().await()
            chatList.forEach { chat ->
                val currentChat = chat.toObject(Chat::class.java)
                Log.i(TAG, currentChat.chatName)
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
