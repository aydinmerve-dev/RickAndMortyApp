package com.devlab.rickandmortyapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class FavoriteCharacter(@PrimaryKey var characterId: Int, var isFavorite: Boolean)