package com.devlab.rickandmortyapp.di

import android.content.Context
import androidx.room.Room
import com.devlab.rickandmortyapp.db.AppDatabase
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.db.FavoriteCharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideFavoriteCharacterDao(appDatabase: AppDatabase): FavoriteCharacterDao {
        return appDatabase.favoriteCharacterDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "RickAndMorty"
        ).build()
    }
}