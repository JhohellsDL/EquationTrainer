package com.jdlstudios.equationtrainer.domain.usecases

import com.jdlstudios.equationtrainer.data.providers.EquationProvider
import com.jdlstudios.equationtrainer.domain.models.Equation

class GenerateListEquationsUseCase {
    fun generateListEquations(quantity: Int): List<Equation> {
        return (0 until quantity).map { EquationProvider.generateRandomEquation() }.toList()
    }
}