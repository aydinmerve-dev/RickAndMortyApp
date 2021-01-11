package com.devlab.rickandmortyapp.ui.detail

import com.devlab.rickandmortyapp.R
import com.devlab.rickandmortyapp.base.BaseActivity
import com.devlab.rickandmortyapp.databinding.ActivityCharacterDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailActivity : BaseActivity<ActivityCharacterDetailBinding, CharacterDetailVM>(){
    override fun getLayoutId(): Int {
        return R.layout.activity_character_detail
    }


}

