package com.jdlstudios.equationtrainer.domain.models

import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity

const val MAX_NO_OF_EXERCISES = 20
const val EXP_INCREASE = 10

data class Session(
    val difficulty: Int = 0,
    val numberOfExercises: Int = 0,
    val currentExerciseCount: Int = 1,
    val correctAnswers: Int = 0,
    val incorrectAnswers: Int = 0,
    val exp: Int = 0,
    val time: Long = 0,
    val date: String = "",
    val isGameOver: Boolean = false,
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

    fun toFormattedString(): String {
        return """
        Session Details:
        Difficulty: $difficulty
        Number of Exercises: $numberOfExercises
        Current Exercise Count: $currentExerciseCount
        Correct Answers: $correctAnswers
        Incorrect Answers: $incorrectAnswers
        Experience Points: $exp
        Time: $time
        Date: $date
        Game Over: $isGameOver
    """.trimIndent()
    }
}
