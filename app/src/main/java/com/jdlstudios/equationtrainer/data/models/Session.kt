package com.jdlstudios.equationtrainer.data.models

import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity

data class Session(
    val difficulty: Int,
    val numberOfExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val exp: Int,
    val time: Long,
    val date: String
) {
    fun toSessionEntity(): SessionEntity {
        return SessionEntity(
            difficulty = difficulty,
            numberOfExercises = numberOfExercises,
            correctAnswers = correctAnswers,
            incorrectAnswers = incorrectAnswers,
            exp = exp,
            time = time,
            date = date
        )
    }
}
