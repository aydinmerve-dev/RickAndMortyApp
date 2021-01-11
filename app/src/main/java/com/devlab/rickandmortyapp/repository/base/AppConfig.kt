package com.devlab.rickandmortyapp.repository.base

import com.devlab.rickandmortyapp.util.AppConstant.SERVICE_URL
import javax.inject.Inject

class AppConfig @Inject constructor() {
    /**
     * Service urls
     */
    fun serviceUrl(): String {
        return SERVICE_URL
    }
}