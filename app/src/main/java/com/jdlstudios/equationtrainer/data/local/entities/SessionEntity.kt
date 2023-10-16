package com.jdlstudios.equationtrainer.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jdlstudios.equationtrainer.data.models.Session

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int = 0,
    val difficulty: Int,
    val numberOfExercises: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val exp: Int,
    val time: Long,
    val date: String
) {
    fun toSession(): Session {
        return Session(
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
