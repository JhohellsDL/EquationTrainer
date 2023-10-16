package com.jdlstudios.equationtrainer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.equationtrainer.data.models.Equation

@Entity(tableName = "equations")
data class EquationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val equation: String,
    val answer: String,
    val answerUser: String,
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