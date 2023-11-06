package com.jdlstudios.equationtrainer.domain.models

data class EquationFractionTypeOne(
    val partNumberFraction: Pair<Int, Int> = Pair(0, 0),
    val variable: Int = 0,
    val independentTerm: Int = 0,
    val result: Int = 0
) {

    fun toEquation(): Equation {
        return Equation(
            equation = "(${partNumberFraction.first}/${partNumberFraction.second})x + $independentTerm = $result",
            answer = variable,
            answerUser = 0,
            time = 0L,
            date = "",
            isCorrect = false
        )
    }

    fun toStringFraction(): String {
        return "(${partNumberFraction.first}/${partNumberFraction.second})x + $independentTerm = $result"
    }
}