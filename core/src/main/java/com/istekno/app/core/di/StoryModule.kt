package com.istekno.app.core.di

import com.istekno.app.core.data.source.remote.network.auth.AuthClient
import com.istekno.app.core.data.source.remote.network.auth.AuthService
import com.istekno.app.core.data.source.remote.network.story.StoryClient
import com.istekno.app.core.data.source.remote.network.story.StoryService
import com.istekno.app.core.domain.repository.AuthRepository
import com.istekno.app.core.domain.repository.StoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StoryModule {

    @Singleton
    @Provides
    fun provideStoryRepository(): StoryRepository {
        return StoryRepository(StoryClient().retrofitClient)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(): AuthRepository {
        return AuthRepository(AuthClient().retrofitClientWithoutToken)
    }

    @Singleton
    @Provides
    fun provideStoryService(): StoryService {
        return StoryClient().retrofitClient
    }

    @Singleton
    @Provides
    fun provideAuthService(): AuthService {
        return AuthClient().retrofitClientWithoutToken
    }
}