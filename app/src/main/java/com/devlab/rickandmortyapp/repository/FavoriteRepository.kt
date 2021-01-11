package com.devlab.rickandmortyapp.repository

import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.db.FavoriteCharacterDao
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.ArrayList
import java.util.concurrent.Callable
import javax.inject.Inject


class FavoriteRepository @Inject constructor(val dao: FavoriteCharacterDao) {

    fun getAllFavoriteCharacters(): Observable<List<FavoriteCharacter>> {
        return PublishSubject.create<List<FavoriteCharacter>> {
            val result = dao.getAllCharacterFavorites()
            result?.let { favoriteCharacters ->
                it.onNext(favoriteCharacters)
            } ?: run {
                it.onNext(ArrayList<FavoriteCharacter>())
            }
        }
    }

    fun getFavoriteByCharacterId(characterId: Int): Observable<FavoriteCharacter> {
        return PublishSubject.create<FavoriteCharacter> {
            val result = dao.getFavoriteByCharacterId(characterId)
            result?.let { favoriteCharacter ->
                it.onNext(favoriteCharacter)
            } ?: run {
                it.onNext(FavoriteCharacter(0, false))
            }
        }
    }

    fun addFavoriteCharacter(favorite: FavoriteCharacter): Observable<Any> {
        return Observable.fromCallable(Callable<Unit?> {
            dao.insertFavorite(favorite)
        })
    }

    fun deleteFavoriteCharacter(favorite: FavoriteCharacter): Observable<Any> {
        return Observable.fromCallable(Callable<Unit?> {
            dao.deleteFavorite(favorite)
        })
    }
}