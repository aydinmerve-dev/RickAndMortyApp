package com.devlab.rickandmortyapp.repository

import com.devlab.rickandmortyapp.repository.responseModels.Character
import com.devlab.rickandmortyapp.repository.responseModels.ResponseModel
import com.devlab.rickandmortyapp.repository.responseModels.Episode
import com.devlab.rickandmortyapp.repository.services.IApiService
import io.reactivex.Observable
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val service: IApiService){

    fun searchCharacters(page: Int, name: String?, status: String?): Observable<ResponseModel<List<Character>>> {
        return service.searchCharacters(page, name, status)
    }

    fun getCharacter(characterId: Int): Observable<Character> {
        return service.getCharacter(characterId)
    }

    fun getEpisode(episodeId: Int): Observable<Episode> {
        return service.getEpisode(episodeId)
    }
}