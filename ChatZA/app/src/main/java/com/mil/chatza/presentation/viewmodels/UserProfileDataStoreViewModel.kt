package com.mil.chatza.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mil.chatza.domain.repository.UserProfileRepository
import com.mil.chatza.domain.model.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileDataStoreViewModel @Inject constructor(private val UserPro: UserProfileRepository) :
    ViewModel() {

    private val _userProfileInfo = MutableStateFlow(UserProfile()) // Initial state
    val userProfileInfo: StateFlow<UserProfile> = _userProfileInfo

    init {
        viewModelScope.launch {
            UserPro.getUserProfileBasicInfo().collect {
                _userProfileInfo.value = it
            }
        }
    }

    fun saveUserProfile(userprofile: UserProfile) = viewModelScope.launch {
        UserPro.saveUserProfileBasicInfo(userprofile)
    }

    fun deleteUserProfile(userprofile: UserProfile) = viewModelScope.launch {
        UserPro.deleteUserProfileBasicInfo(userProfile = userprofile)
    }

}