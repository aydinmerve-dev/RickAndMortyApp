package com.devlab.rickandmortyapp.repository.responseModels

data class InfoModel(
    val count: Int?,
    val pages: Int,
    val next: String?,
    val prev: String?
)
