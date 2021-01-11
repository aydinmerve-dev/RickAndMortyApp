package com.devlab.rickandmortyapp.repository.base

import com.devlab.rickandmortyapp.util.enums.ErrorType

class ErrorModel constructor(var errorDesc: String = "", var status: Int = 0, var type: ErrorType = ErrorType.NOT_FOUND) : Throwable()