package com.devlab.rickandmortyapp.domain

import com.devlab.rickandmortyapp.base.BaseUseCase
import com.devlab.rickandmortyapp.db.FavoriteCharacter
import com.devlab.rickandmortyapp.domain.models.CharacterModel
import com.devlab.rickandmortyapp.repository.CharacterRepository
import com.devlab.rickandmortyapp.repository.FavoriteRepository
import com.devlab.rickandmortyapp.repository.base.ErrorModel
import com.devlab.rickandmortyapp.repository.responseModels.Character
import com.devlab.rickandmortyapp.repository.responseModels.Episode
import com.devlab.rickandmortyapp.util.AppConstant.SERVICE_URL
import com.devlab.rickandmortyapp.util.enums.Gender
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject


class GetCharacterDetail @Inject constructor(
    private val repository: CharacterRepository,
    private val favoriteRepository: FavoriteRepository
) : BaseUseCase<CharacterModel, GetCharacterDetail.Params>() {

    val character = CharacterModel(null)

    class Params(val characterId: Int)

    override fun on(params: Params?) {
        sendRequest {
            Observable.zip(
                repository.getCharacter(params!!.characterId).map(::convert2Character)
                    .flatMap { character ->
                        repository.getEpisode(character.lastEpisodeId!!).map(::convert2Episode)
                    },
                favoriteRepository.getFavoriteByCharacterId(params.characterId),
                BiFunction { t1, t2 ->
                    convert2Favorite(t2)
                }
            )

        }
    }

    private fun convert2Character(res: Character): CharacterModel {
        character.apply {
            characterId = res.id
            name = res.name
            imageUrl = res.image
            status = res.status
            species = res.species
            res.episode?.let { episodes ->
                episodeCount = episodes.size
                val episodeUrl: String = episodes[episodes.size - 1]
                val split = episodeUrl.split(SERVICE_URL + "episode/")
                lastEpisodeId = split[1].toInt()
            }
            gender = when (res.gender) {
                Gender.MALE.toString() -> Gender.MALE
                Gender.FEMALE.toString() -> Gender.FEMALE
                Gender.GENDERLESS.toString() -> Gender.GENDERLESS
                else -> Gender.UNKNOWN
            }

            res.origin?.let {
                originLocation = it.name
            }

            res.location?.let {
                lastKnownLocation = it.name
            }
        }

        return character
    }

    private fun convert2Episode(res: Episode) {
        character.lastSeenEpisodeName = res.name
        character.lastSeenEpisodeAirDate = res.air_date
    }

    private fun convert2Favorite(res: FavoriteCharacter): CharacterModel {
        character.favorite = res.isFavorite
        return character
    }
}