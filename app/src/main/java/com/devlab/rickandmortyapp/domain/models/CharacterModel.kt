package com.devlab.rickandmortyapp.domain.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.DiffUtil
import com.devlab.rickandmortyapp.BR
import com.devlab.rickandmortyapp.util.enums.Gender

data class CharacterModel(
    var characterId: Int?
) : BaseObservable() {

    var name: String? = null
    var status: String? = null
    var imageUrl: String? = null
    var species: String? = null
    var originLocation: String? = null
    var lastKnownLocation: String? = null
    var gender: Gender? = null
    var episodeCount: Int? = 0
    var lastEpisodeId: Int? = null
    var lastSeenEpisodeName: String? = null
    var lastSeenEpisodeAirDate: String? = null   //Son görülen bölüm adı ve yayın tarihi

    @get:Bindable
    var favorite: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.favorite)
        }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterModel>() {
            override fun areItemsTheSame(
                oldItem: CharacterModel,
                newItem: CharacterModel
            ): Boolean {
                return oldItem.characterId == newItem.characterId
            }

            override fun areContentsTheSame(
                oldItem: CharacterModel,
                newItem: CharacterModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
