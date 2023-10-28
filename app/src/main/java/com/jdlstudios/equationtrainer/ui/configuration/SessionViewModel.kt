package com.jdlstudios.equationtrainer.ui.configuration

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.jdlstudios.equationtrainer.data.providers.EquationProvider
import com.jdlstudios.equationtrainer.domain.models.EXP_INCREASE
import com.jdlstudios.equationtrainer.domain.models.Equation
import com.jdlstudios.equationtrainer.domain.models.Session
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
class SessionViewModel : ViewModel() {

    private val _uiSessionState = MutableStateFlow(Session())
    val uiSessionState: StateFlow<Session> = _uiSessionState.asStateFlow()

    private val _uiEquationState = MutableStateFlow(Equation())
    val uiEquationState: StateFlow<Equation> = _uiEquationState.asStateFlow()

    private var currentListExercises: MutableList<Equation> = mutableListOf()
    private var currentListSession: MutableList<Session> = mutableListOf()
    private lateinit var currentEquation: Equation

    private var listExercises: MutableList<Equation> = mutableListOf()

    var userAnswer by mutableStateOf("")
        private set

    var timeStart : Long = 0
    var timeEnd : Long = 0

    fun getListEquations(): List<Equation> {
        Log.d("asdasd", "LISTA get VIEWMODEL !! :  $currentListExercises")
        return currentListExercises
    }

    fun getListSession(): List<Session> {
        return currentListSession
    }

    private fun pickRandomEquation(): Equation {
        currentEquation = EquationProvider.generateRandomEquation()
        listExercises.add(currentEquation)
        return currentEquation
    }

    fun resetSession() {
        Log.d("Configuration", "dificultad : ${_uiSessionState.value.toFormattedString()}")
        listExercises.clear()
        _uiEquationState.value = pickRandomEquation()

        Log.d("asdasdasd", "update answer: ${_uiEquationState.value.equation}")
        Log.d("asdasd", "LISTA VIEWMODEL !! :  $listExercises")
        Log.d("asdasd", "LISTA get VIEWMODEL !! :  $currentListExercises")
        Log.d("asdasd", "SessionViewModel - resetSession : list clear, new equation")
    }

    fun cleanSession() {
        Log.d("asdasd", "lista de ecuacuones: $listExercises")
        _uiSessionState.value = Session()
        Log.d("qweqweqwe", "SESSION NEW: ${_uiSessionState.value.toFormattedString()}")
    }

    init {
        resetSession()
    }

    fun updateDifficulty(difficultyLevel: DifficultyLevel) {
        _uiSessionState.update {
            it.copy(
                difficulty = difficultyLevel.ordinal
            )
        }
    }

    fun updateGameOver(isGameOver: Boolean) {
        _uiSessionState.update {
            it.copy(
                isGameOver = isGameOver
            )
        }
    }

    fun updateTime(time: Int) {
        Log.d("asdasd", "updateTtime: $time")
        _uiEquationState.update {
            it.copy(
                time = time
            )
        }
    }

    fun updateNumberExercises(numberExercises: Int) {
        _uiSessionState.update {
            Log.d("asdasd", "update number exercises $it")
            it.copy(
                numberOfExercises = numberExercises
            )
        }
    }

    fun updateDateSession(date: String) {
        val currentCalendar = Calendar.getInstance()
        timeStart = currentCalendar.timeInMillis
        Log.d("qweqweqwe", "date 1: ${currentCalendar.timeInMillis}")
        _uiSessionState.update {
            it.copy(
                date = date
            )
        }
    }

    fun updateUserAnswer(answer: String) {
        Log.d("ExerciseEasy", "updateUserAnswer: $answer")
        Log.d("asdasd", "update answer: $answer")
        userAnswer = answer
        Log.d("ExerciseEasy", "updateUserAnswer : $userAnswer")
    }

    fun checkUserAnswer() {
        if (userAnswer.toInt() == currentEquation.answer) {
            val updatedExp = _uiSessionState.value.exp.plus(EXP_INCREASE)
            val updateAnswerCorrect = _uiSessionState.value.correctAnswers.plus(1)
            Log.d("ExerciseEasy", "updateUserAnswer: ${userAnswer.toInt()}")
            Log.d("ExerciseEasy", "updateUserAnswer : ${currentEquation.answer}")
            _uiEquationState.update {
                it.copy(
                    answer = currentEquation.answer,
                    answerUser = userAnswer.toInt(),
                    isCorrect = true
                )
            }
            Log.d(
                "ExerciseEasy",
                "updateUserAnswer bien: ${_uiEquationState.value.toFormattedString()}"
            )
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.equation}")
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.isCorrect}")
            currentListExercises.add(_uiEquationState.value)
            _uiSessionState.update {
                it.copy(
                    correctAnswers = updateAnswerCorrect
                )
            }
            updateSessionExp(updatedExp = updatedExp)

            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.equation}")
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.isCorrect}")
        } else {
            val updateAnswerIncorrect = _uiSessionState.value.incorrectAnswers.plus(1)
            _uiEquationState.update {
                it.copy(
                    answer = currentEquation.answer,
                    answerUser = userAnswer.toInt()
                )
            }
            currentListExercises.add(_uiEquationState.value)
            Log.d(
                "ExerciseEasy",
                "updateUserAnswer mal: ${_uiEquationState.value.toFormattedString()}"
            )
            _uiSessionState.update {
                it.copy(
                    incorrectAnswers = updateAnswerIncorrect
                )
            }
            updateSessionExp(_uiSessionState.value.exp)
        }
        Log.d("asdasd", "LISTA get VIEWMODEL --- CHECK !! :  $currentListExercises")
        //currentListExercises = listExercises
        updateUserAnswer("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateSessionExp(updatedExp: Int) {
        Log.d("asdasd", "size number : ${_uiSessionState.value.numberOfExercises}")
        Log.d("asdasd", "size list : ${listExercises.size}")
        if (listExercises.size == _uiSessionState.value.numberOfExercises) {
            _uiSessionState.update {
                it.copy(
                    exp = updatedExp,
                    isGameOver = true
                )
            }
            val currentCalendar = Calendar.getInstance()
            timeEnd = currentCalendar.timeInMillis
            val timeBetween = timeEnd - timeStart

            Log.d("qweqweqwe", "Time: $timeBetween")
            Log.d("qweqweqwe", "timeBetween en hora: ${milisegundosATiempo(timeBetween)} seconds")
            _uiSessionState.update {
                it.copy(
                    time = timeBetween
                )
            }
            Log.d("qweqweqwe", "Session ultima!!! : ${_uiSessionState.value.toFormattedString()}")
            currentListSession.add(_uiSessionState.value)
        } else {
            _uiSessionState.update {
                it.copy(
                    exp = updatedExp,
                    currentExerciseCount = it.currentExerciseCount.inc()
                )
            }
            //reinicia la ecuacion
            _uiEquationState.value = pickRandomEquation()
        }
    }

    fun skipEquation() {
        updateSessionExp(_uiSessionState.value.exp)
        updateUserAnswer("")
    }

    fun milisegundosATiempo(milisegundos: Long): String {
        val seconds = milisegundos / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
    }
    fun getCurrentDateTime(): String {
        val currentCalendar = Calendar.getInstance()
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)
        val currentSecond = currentCalendar.get(Calendar.SECOND)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth =
            currentCalendar.get(Calendar.MONTH) + 1 // Los meses en Calendar comienzan desde 0
        val currentYear = currentCalendar.get(Calendar.YEAR)

        return "Hora: $currentHour:$currentMinute:$currentSecond\nFecha: $currentDay/$currentMonth/$currentYear"
    }
}