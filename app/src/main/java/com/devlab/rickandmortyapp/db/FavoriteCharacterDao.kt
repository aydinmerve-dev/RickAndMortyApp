package com.devlab.rickandmortyapp.db

import androidx.room.*
import io.reactivex.Flowable
import java.util.*
import java.util.concurrent.Flow

@Dao
interface FavoriteCharacterDao {

    @Query("SELECT * FROM FavoriteCharacter")
    fun getAllCharacterFavorites(): List<FavoriteCharacter>?

    @Query("SELECT * FROM FavoriteCharacter WHERE characterId=:id")
    fun getFavoriteByCharacterId(id: Int): FavoriteCharacter?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: FavoriteCharacter)

    @Update
    fun updateFavorite(favorite: FavoriteCharacter)

    @Delete
    fun deleteFavorite(favorite: FavoriteCharacter)

}