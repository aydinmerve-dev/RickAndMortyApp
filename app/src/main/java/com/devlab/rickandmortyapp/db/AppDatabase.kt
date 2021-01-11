package com.devlab.rickandmortyapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteCharacter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteCharacterDao(): FavoriteCharacterDao
}