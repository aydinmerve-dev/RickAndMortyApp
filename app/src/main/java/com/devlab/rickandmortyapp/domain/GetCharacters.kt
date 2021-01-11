package com.devlab.rickandmortyapp.domain

import com.devlab.rickandmortyapp.base.BaseUseCase
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.domain.models.CharacterModel
import com.devlab.rickandmortyapp.repository.responseModels.ResponseModel
import com.devlab.rickandmortyapp.repository.responseModels.Character
import com.devlab.rickandmortyapp.repository.CharacterRepository
import com.devlab.rickandmortyapp.repository.FavoriteRepository
import com.devlab.rickandmortyapp.repository.base.ErrorModel
import com.devlab.rickandmortyapp.util.Mapper
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetCharacters @Inject constructor(
    private val repository: CharacterRepository,
    private val favoriteRepository: FavoriteRepository
) :
    BaseUseCase<List<CharacterModel>, GetCharacters.Params>() {

    private var page = 1

    var isLastPage = false
    var isLoading = false

    var items = ArrayList<CharacterModel>()

    var favoriteCharacters = HashMap<Int, Boolean>()

    class Params(val name: String? = null, val status: String? = null, val isClear: Boolean = false)

    override fun on(params: Params?) {
        isLoading = true

        if (params != null && params.isClear) {
            items.clear()
            page = 1
        }

        sendRequest {
            if (params != null && params.isClear) {
                Observable.zip(
                    repository.searchCharacters(
                        page,
                        params?.name,
                        params?.status
                    ),
                    favoriteRepository.getAllFavoriteCharacters().map(::convert2Favorite),
                    BiFunction { t1, t2 ->
                        convert2Character(t1)
                    }
                )
            } else {
                repository.searchCharacters(
                    page,
                    params?.name,
                    params?.status
                ).map(::convert2Character)
            }
        }
    }

    private fun convert2Favorite(t2: List<FavoriteCharacter>): Unit {
        favoriteCharacters = HashMap<Int, Boolean>()
        if (t2.isNotEmpty()) {
            for (fav in t2) {
                favoriteCharacters[fav.characterId] = fav.isFavorite
            }
        }
    }

    private fun convert2Character(
        res: ResponseModel<List<Character>>
    ): List<CharacterModel> {
        isLoading = false
        if (res.info!!.next == null) {
            isLastPage = true
        }

        items.addAll(object : Mapper<Character, CharacterModel>() {
            override fun map(value: Character): CharacterModel {
                return CharacterModel(value.id).apply {
                    name = value.name
                    status = value.status
                    imageUrl = value.image
                    species = value.species
                    val fav = favoriteCharacters[value.id]
                    fav?.let {
                        favorite = it
                    }
                }
            }
        }.map(res.results!!))

        return items
    }

    override fun onSendSuccess(t: List<CharacterModel>) {
        if (t.isNotEmpty()) {
            page++
        }

        super.onSendSuccess(t)
    }

    override fun onSendError(error: ErrorModel) {
        super.onSendError(error)

        isLoading = false
    }

}