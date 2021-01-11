package com.devlab.rickandmortyapp.ui.feed

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.devlab.rickandmortyapp.base.BaseViewModel
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.domain.AddToFavorite
import com.devlab.rickandmortyapp.domain.DeleteFavorite
import com.devlab.rickandmortyapp.domain.GetCharacters
import com.devlab.rickandmortyapp.domain.models.CharacterModel
import com.devlab.rickandmortyapp.util.enums.ListMode

class CharacterFeedVM @ViewModelInject constructor(
    private val getCharacters: GetCharacters,
    private val addToFavorite: AddToFavorite,
    private val deleteFavorite: DeleteFavorite
) : BaseViewModel() {

    var isRetry = MutableLiveData<Boolean>()
    var hasShimmer = MutableLiveData<Boolean>()
    var characters = MutableLiveData<List<CharacterModel>>()
    var isRefreshing = MutableLiveData<Boolean>()
    var notFound = MutableLiveData<Boolean>()

    var listMode = MutableLiveData<ListMode>()

    var searchQuery:String? = null
    var searchStatus:String? = null

    override fun onViewCreated(savedInstanceState: Bundle?) {
        super.onViewCreated(savedInstanceState)

        searchCharacters()
    }

    fun searchCharacters() {
        hasShimmer.postValue(true)

        getCharacters.on(
            params = GetCharacters.Params(
                searchQuery,
                searchStatus,
                isClear = true
            ), success = {
                hasShimmer.postValue(false)

                if (it.isNullOrEmpty()) {
                    notFound.postValue(true)
                } else {
                    characters.postValue(it)
                    notFound.postValue(false)
                }
            }, error = {
                if (it.errorDesc == "404") {
                    notFound.postValue(true)
                    hasShimmer.postValue(false)
                } else
                    hasShimmer.postValue(true)
            })

    }

    fun onClickedRetry() {
        isRetry.postValue(false)
        getCharacters.retry()
    }

    fun onClickedRefresh() {
        isRetry.postValue(false)

        getCharacters.on(
            GetCharacters.Params(
                searchQuery,
                searchStatus,
                isClear = true
            ), success = {
                if (it.isNullOrEmpty()) {
                    notFound.postValue(true)
                } else {
                    characters.postValue(it)
                    notFound.postValue(false)
                }

                isRefreshing.postValue(false)
            }, error = {
                isRefreshing.postValue(false)
            })
    }

    fun isLastPage(): Boolean {
        return getCharacters.isLastPage
    }

    fun loadMoreItems() {
        showLoading()

        getCharacters.on(GetCharacters.Params(searchQuery, searchStatus), success = {
            hideLoading()
            isRetry.postValue(false)

            if (!it.isNullOrEmpty()) {
                characters.postValue(it)
            }
        }, error = {
            hideLoading()

            isRetry.postValue(true)
        })
    }

    fun isCharactersLoading(): Boolean {
        return getCharacters.isLoading
    }

    fun onClickedFavorite(item: CharacterModel) {
        isLoading.postValue(true)

        val favoriteCharacter = FavoriteCharacter(item.characterId!!, !item.favorite)
        if (item.favorite) {
            deleteFavorite.on(DeleteFavorite.Params(favoriteCharacter), success = {
                item.favorite = !item.favorite
                isLoading.postValue(false)
            }, error = {
                isLoading.postValue(false)
            })
        } else {
            addToFavorite.on(AddToFavorite.Params(favoriteCharacter), success = {
                item.favorite = !item.favorite
                isLoading.postValue(false)
            }, error = {
                isLoading.postValue(false)
            })
        }
    }

    fun onClickedLinearMode(){
        listMode.postValue(ListMode.LINEAR)
    }

    fun onClickedGridMode(){
        listMode.postValue(ListMode.GRID)
    }
}