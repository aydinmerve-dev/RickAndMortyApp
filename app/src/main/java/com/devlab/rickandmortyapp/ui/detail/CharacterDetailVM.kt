package com.devlab.rickandmortyapp.ui.detail

import android.app.Application
import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.devlab.rickandmortyapp.base.BaseViewModel
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.domain.AddToFavorite
import com.devlab.rickandmortyapp.domain.DeleteFavorite
import com.devlab.rickandmortyapp.domain.GetCharacterDetail
import com.devlab.rickandmortyapp.domain.models.CharacterModel

class CharacterDetailVM @ViewModelInject constructor(
    private val getCharacterDetail: GetCharacterDetail,
    private val addToFavorite: AddToFavorite, private val deleteFavorite: DeleteFavorite
) : BaseViewModel() {

    var character = MutableLiveData<CharacterModel>()

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)

        arguments?.let {
            val characterId = it.getInt("characterId")

            getCharacterDetail.on(GetCharacterDetail.Params(characterId), success = { characterModel ->
                character.postValue(characterModel)
            })
        }
    }

    fun onClickedFavorite() {
        isLoading.postValue(true)

        val favoriteCharacter = FavoriteCharacter(character.value!!.characterId!!, !character.value!!.favorite)
        if (character.value!!.favorite) {
            deleteFavorite.on(DeleteFavorite.Params(favoriteCharacter), success = {
                character.value!!.favorite = !character.value!!.favorite
                isLoading.postValue(false)
            }, error = {
                isLoading.postValue(false)
            })
        } else {
            addToFavorite.on(AddToFavorite.Params(favoriteCharacter), success = {
                character.value!!.favorite = !character.value!!.favorite
                isLoading.postValue(false)
            }, error = {
                isLoading.postValue(false)
            })
        }
    }
}

