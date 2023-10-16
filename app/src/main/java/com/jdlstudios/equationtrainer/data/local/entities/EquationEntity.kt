package com.jdlstudios.equationtrainer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.equationtrainer.domain.models.Equation

@Entity(tableName = "equations")
data class EquationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val equation: String,
    val answer: Int,
    val answerUser: Int,
    val date: String,
    val isCorrect: Boolean
) {
    fun toEquation(): Equation {
        return  Equation(
            equation = equation,
            answer = answer,
            answerUser = answerUser,
            date = date,
            isCorrect = isCorrect
        )
    }
}