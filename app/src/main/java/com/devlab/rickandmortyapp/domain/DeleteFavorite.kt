package com.devlab.rickandmortyapp.domain

import com.devlab.rickandmortyapp.base.BaseUseCase
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.repository.FavoriteRepository
import com.devlab.rickandmortyapp.repository.base.ErrorModel
import javax.inject.Inject

class DeleteFavorite @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    BaseUseCase<Any, DeleteFavorite.Params>() {

    class Params(val favoriteCharacter: FavoriteCharacter)

    override fun on(params: Params?) {
        sendRequest {
            favoriteRepository.deleteFavoriteCharacter(params!!.favoriteCharacter)
        }
    }
}