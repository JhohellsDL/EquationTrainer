package com.jdlstudios.equationtrainer.domain.models

import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity

data class Session(
    val difficulty: Int = 0,
    val numberOfExercises: Int = 0,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val exp: Int = 0,
    val time: Long = 0,
    val date: String = ""
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
