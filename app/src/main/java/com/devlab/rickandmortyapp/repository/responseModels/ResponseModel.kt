package com.devlab.rickandmortyapp.repository.responseModels

data class ResponseModel<T>(
    var results: T?,
    var info: InfoModel?
)