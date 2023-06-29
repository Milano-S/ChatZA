package com.mil.chatza.domain.repository.di

import android.content.Context
import com.mil.chatza.domain.repository.UserProfileRepository
import com.mil.chatza.domain.repository.UserProfileRepositoryImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideDataStoreRepo(@ApplicationContext context: Context): UserProfileRepository = UserProfileRepositoryImp(context)


}