package com.jdlstudios.equationtrainer.domain.models

import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity

data class EquationFractionTypeOne(
    val partNumberFraction: Pair<Int, Int>,
    val variable: Int,
    val independentTerm: Int,
    val result: Int
) {

    fun toStringFraction(): String {
        return "(${partNumberFraction.first}/${partNumberFraction.second})x + $independentTerm = $result"
    }
}