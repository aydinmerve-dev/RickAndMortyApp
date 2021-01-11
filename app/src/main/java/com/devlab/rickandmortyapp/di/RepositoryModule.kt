package com.devlab.rickandmortyapp.di

import com.devlab.rickandmortyapp.repository.CharacterRepository
import com.devlab.rickandmortyapp.repository.services.IApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesCharacterRepository(service: IApiService): CharacterRepository {
        return CharacterRepository(service)
    }
}