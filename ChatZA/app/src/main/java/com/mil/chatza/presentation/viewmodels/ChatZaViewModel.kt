package com.mil.chatza.presentation.viewmodels

import android.telecom.Call.Details
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.auth.User
import com.mil.chatza.domain.model.UserProfile

class ChatZaViewModel : ViewModel() {

    private var _currentChatName = MutableLiveData<String>()
    var currentChatName : LiveData<String> = _currentChatName
    fun setCurrentChat(chatName : String){
        _currentChatName.value = chatName
    }

    private var _currentUserDetails = MutableLiveData<UserProfile>()
    var currentUserDetails: LiveData<UserProfile> = _currentUserDetails
    fun setCurrentUserDetails(userDetails: UserProfile?){
        _currentUserDetails.value = userDetails
    }

}