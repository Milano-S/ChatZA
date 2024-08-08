package com.mil.chatza.domain.repository

import com.mil.chatza.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {

    suspend fun saveUserProfileBasicInfo(userProfile: UserProfile)

    suspend fun getUserProfileBasicInfo(): Flow<UserProfile>

    suspend fun deleteUserProfileBasicInfo(userProfile: UserProfile)

}