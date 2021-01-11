package com.devlab.rickandmortyapp.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devlab.rickandmortyapp.R
import com.devlab.rickandmortyapp.databinding.ItemCharacterBinding
import com.devlab.rickandmortyapp.domain.models.CharacterModel

abstract class CharacterAdapter : ListAdapter<CharacterModel, CharacterAdapter.CharacterHolder>(CharacterModel.DIFF_CALLBACK) {

    abstract fun onClickedItem(item: CharacterModel)

    abstract fun onClickedFavorite(item: CharacterModel)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val binding: ItemCharacterBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_character, parent, false
        )

        return CharacterHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        val item = getItem(position)

        holder.binding!!.character = item
        holder.binding!!.executePendingBindings()

        with(holder.binding!!) {
            containerCharacter.setOnClickListener {
                onClickedItem(item)
            }

            actionFavorite.setOnClickListener{
                onClickedFavorite(item)
            }
        }
    }

    class CharacterHolder(vi: View) : RecyclerView.ViewHolder(vi) {
        var binding: ItemCharacterBinding? = DataBindingUtil.bind(vi)
    }
}