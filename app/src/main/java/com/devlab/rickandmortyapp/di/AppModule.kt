package com.devlab.rickandmortyapp.di

import android.content.Context
import com.devlab.rickandmortyapp.repository.base.AppConfig
import com.devlab.rickandmortyapp.util.Strings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAppConfig(): AppConfig =
        AppConfig()

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providesStrings(@ApplicationContext context: Context): Strings = Strings(context)

}