package com.jdlstudios.equationtrainer.domain.utils

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

}