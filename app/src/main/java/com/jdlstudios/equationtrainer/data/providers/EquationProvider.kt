package com.jdlstudios.equationtrainer.data.providers

import com.jdlstudios.equationtrainer.domain.models.Equation
import com.jdlstudios.equationtrainer.domain.models.EquationFractionTypeOne
import com.jdlstudios.equationtrainer.domain.utils.Utilities.fractionNumberRandom
import kotlin.math.absoluteValue


const val MAX_VALUE = 10

class EquationProvider {

    // Ecuacion simple: 3x + 3 = 7
    // Ecuación Lineal con Coeficientes Fraccionarios: (1/2)x + 2 = 5
    // Ecuación Lineal con Variables en Ambos Lados: 3x - 2 = 2x + 4
    companion object {
        fun generateRandomEquation(): Equation {
            val type = (1..2).random()
            val a = (1..MAX_VALUE).random()
            var b = (-10..MAX_VALUE).random()
            val x = (1..MAX_VALUE).random()

            val c = a * x + b
            val equation = when (type) {
                1 -> if (b < 0) "${a}x - ${b.absoluteValue} = $c" else "$b + ${a}x = $c"
                2 -> if (b < 0) "$c = ${a}x - ${b.absoluteValue}" else "$c = $b + ${a}x"
                3 -> "$c = $b + ${a}x"
                else -> "$b + ${a}x = $c"
            }

            return Equation(
                equation = equation,
                answer = x,
                answerUser = 0,
                time = 0,
                date = "",
                isCorrect = false
            )
        }

        fun generateRandomEquationFraction(): EquationFractionTypeOne {
            var equation: EquationFractionTypeOne

            do {
                val x = (1..10).random()
                val fractionPair = fractionNumberRandom()
                val numerator = fractionPair.first
                val denominator = fractionPair.second
                val independentTerm = (1..10).random()
                val result = ((numerator * x).toFloat() / denominator) + independentTerm

                equation = EquationFractionTypeOne(
                    partNumberFraction = fractionPair,
                    variable = x,
                    independentTerm = independentTerm,
                    result = result.toInt()
                )
            } while ((result % 1) != 0f)

            return equation
        }
    }

}