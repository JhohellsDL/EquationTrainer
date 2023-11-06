package com.jdlstudios.equationtrainer.domain.utils

import com.jdlstudios.equationtrainer.domain.models.EquationFractionTypeOne

object Utilities {
    fun fractionNumberRandom(): Pair<Int, Int> {
        var numerator: Int
        var denominator: Int

        do {
            numerator = (1..10).random()
            denominator = (2..10).random()
        } while (numerator == denominator)

        return Pair(numerator, denominator)
    }

    fun solutionEquation(
        equation: EquationFractionTypeOne
    ): String {

        var newR = equation.result - equation.independentTerm
        var text = ""
        val fraction: Pair<Int, Int> = equation.partNumberFraction
        val numerator = fraction.first
        val denominator = fraction.second
        val independentTerm = equation.independentTerm
        val result = equation.result

        //Restar el termino independiente a ambos lados de la ecuacion
        text += "ECUACION: ($numerator/$denominator)x + $independentTerm = $result\n"
        text += "\n - Restar ${equation.independentTerm} a ambos lados de la ecuacion\n"
        text += "\t\t(${equation.partNumberFraction.first}/${equation.partNumberFraction.second})x + ${equation.independentTerm} - (${equation.independentTerm}) = ${equation.result} - (${equation.independentTerm})\n"
        text += "\t\t(${equation.partNumberFraction.first}/${equation.partNumberFraction.second})x = $newR\n"

        //Multiplicar ambos lados de la ecuacion por el denominador
        text += "\t\t(${equation.partNumberFraction.first}/${equation.partNumberFraction.second})x = $newR\n"
        text += "\n - Multiplicar ambos lados de la ecuacion por ${equation.partNumberFraction.second}\n"
        text += "\t\t(${equation.partNumberFraction.first}/${equation.partNumberFraction.second})x * (${equation.partNumberFraction.second}) = $newR * (${equation.partNumberFraction.second})\n"
        text += "\t\t(${equation.partNumberFraction.first})x = $newR\n"

        newR *= equation.partNumberFraction.second

        //Dividir ambos lados de la ecuacion por el numerador
        text += "\t\t(${equation.partNumberFraction.first})x = $newR\n"
        text += "\n - Dividir ambos lados de la ecuacion entre ${equation.partNumberFraction.first}\n"
        text += "\t\t(${equation.partNumberFraction.first})x / (${equation.partNumberFraction.first}) = $newR / (${equation.partNumberFraction.first})\n"

        newR /= equation.partNumberFraction.first

        text += "\t\tx = $newR"

        //mostrar result
        return text
    }

}