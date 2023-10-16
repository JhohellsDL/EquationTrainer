package com.jdlstudios.equationtrainer.domain.models

import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity

data class Equation(
    val equation: String,
    val answer: Int,
    val answerUser: Int,
    val date: String,
    val isCorrect: Boolean
) {
    fun toEquationEntity(): EquationEntity {
        return EquationEntity(
            equation = equation,
            answer = answer,
            answerUser = answerUser,
            date = date,
            isCorrect = isCorrect
        )
    }
}
