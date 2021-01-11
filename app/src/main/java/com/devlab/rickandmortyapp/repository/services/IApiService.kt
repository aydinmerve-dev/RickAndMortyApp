package com.devlab.rickandmortyapp.repository.services

import com.devlab.rickandmortyapp.repository.responseModels.Character
import com.devlab.rickandmortyapp.repository.responseModels.ResponseModel
import com.devlab.rickandmortyapp.repository.responseModels.Episode
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IApiService {

    @GET("character")
    fun searchCharacters(
        @Query("page") page: Int,
        @Query("name") name: String?,
        @Query("status") status: String?
    ): Observable<ResponseModel<List<Character>>>

    @GET("character/{characterId}")
    fun getCharacter(
        @Path("characterId") characterId: Int
    ): Observable<Character>

    @GET("episode/{episodeId}")
    fun getEpisode(
        @Path("episodeId") episodeId: Int
    ): Observable<Episode>
}