package com.devlab.rickandmortyapp.util

import java.util.*

abstract class Mapper<T1, T2> {
    abstract fun map(value: T1): T2

    fun map(values: List<T1>): List<T2> {
        val returnValues: MutableList<T2> = ArrayList(values.size)
        for (value in values) {
            returnValues.add(map(value))
        }
        return returnValues
    }
}