package com.mil.chatza.domain.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mil.chatza.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

const val Datastore_Name = "User_Profile"
val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = Datastore_Name)

@Singleton
class UserProfileRepositoryImp @Inject constructor(
    private val context: Context
) : UserProfileRepository {

    companion object {
        val id = stringPreferencesKey("id")
        val dateCreated = stringPreferencesKey("dateCreated")
        val email = stringPreferencesKey("email")
        val name = stringPreferencesKey("name")
        val age = stringPreferencesKey("age")
        val gender = stringPreferencesKey("gender")
        val province = stringPreferencesKey("province")
    }

    override suspend fun saveUserProfileBasicInfo(userProfile: UserProfile) {
        context.datastore.edit { userPro ->
            userPro[dateCreated] = userProfile.dateCreated
            userPro[email] = userProfile.email
            userPro[name] = userProfile.name
            userPro[age] = userProfile.age
            userPro[gender] = userProfile.gender
            userPro[province] = userProfile.province
        }
    }

    override suspend fun getUserProfileBasicInfo(): Flow<UserProfile> =
        context.datastore.data.map { userPro ->
            UserProfile(
                dateCreated = userPro[dateCreated] ?: "",
                email = userPro[email] ?: "",
                name = userPro[name] ?: "",
                age = userPro[age] ?: "",
                gender = userPro[gender] ?: "",
                province = userPro[id] ?: "",
            )
        }

    override suspend fun deleteUserProfileBasicInfo(userProfile: UserProfile) {
        context.datastore.edit { it.clear() }
    }

}