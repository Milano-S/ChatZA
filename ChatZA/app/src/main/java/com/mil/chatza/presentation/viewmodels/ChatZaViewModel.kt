package com.mil.chatza.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatZaViewModel : ViewModel() {

    private var _currentChatName = MutableLiveData<String>()
    var currentChatName : LiveData<String> = _currentChatName
    fun setCurrentChat(chatName : String){
        _currentChatName.value = chatName
    }

}