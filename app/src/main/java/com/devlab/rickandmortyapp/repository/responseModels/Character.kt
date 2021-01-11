package com.devlab.rickandmortyapp.repository.responseModels

data class Character(
    val id: Int,
    val name: String,
    val status: String?,
    val image: String?,
    val species: String?,
    val gender: String,
    val origin:Location?,
    val location: Location?,
    val episode: List<String>? = null
)