package com.jdlstudios.equationtrainer.domain.models

import com.jdlstudios.equationtrainer.data.local.entities.EquationEntity

data class Equation(
    val equation: String = "",
    val answer: Int = 0,
    val answerUser: Int = 0,
    val time: Long = 0,
    val date: String = "",
    val isCorrect: Boolean = false
) {
    fun toEquationEntity(): EquationEntity {
        return EquationEntity(
            equation = equation,
            answer = answer,
            answerUser = answerUser,
            time = time,
            date = date,
            isCorrect = isCorrect
        )
    }

    fun toFormattedString(): String {
        return """
        Equation Details:
        Equation: $equation
        Answer: $answer
        Answer User: $answerUser
        Time: $time
        Date: $date
        Is Correct: $isCorrect
    """.trimIndent()
    }
}
