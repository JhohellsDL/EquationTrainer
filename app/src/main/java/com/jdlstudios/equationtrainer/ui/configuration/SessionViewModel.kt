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
import com.jdlstudios.equationtrainer.domain.models.EquationFractionTypeOne
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

    private val _uiEquationFractionState = MutableStateFlow(EquationFractionTypeOne())
    val uiEquationFractionState: StateFlow<EquationFractionTypeOne> =
        _uiEquationFractionState.asStateFlow()

    private var currentListExercises: MutableList<Equation> = mutableListOf()
    private var currentListSession: MutableList<Session> = mutableListOf()
    private lateinit var currentEquation: Equation

    private var listExercises: MutableList<Equation> = mutableListOf()

    var userAnswer by mutableStateOf("")
        private set

    private var timeStart: Long = 0
    private var timeEnd: Long = 0

    fun getListEquations(): List<Equation> {
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

    private fun pickRandomEquationFraction(): Equation {
        val equationFraction = EquationProvider.generateRandomEquationFraction()
        _uiEquationFractionState.value = equationFraction
        currentEquation = equationFraction.toEquation()
        listExercises.add(currentEquation)
        return currentEquation
    }

    fun resetSession() {
        listExercises.clear()
        selectEquationBasedOnDifficulty()
    }

    fun cleanSession() {
        _uiSessionState.value = Session()
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

    private fun updateTimeEquation(time: Long) {
        _uiEquationState.update {
            it.copy(
                time = time
            )
        }
    }

    private fun updateDateEquation(date: String) {
        _uiEquationState.update {
            it.copy(
                date = date
            )
        }
    }

    fun updateNumberExercises(numberExercises: Int) {
        Log.d("qweqweqwe", "numberexercises update: $numberExercises")
        _uiSessionState.update {
            it.copy(
                numberOfExercises = numberExercises
            )
        }
    }

    fun updateDateSession(date: String) {
        val currentCalendar = Calendar.getInstance()
        timeStart = currentCalendar.timeInMillis
        _uiSessionState.update {
            it.copy(
                date = date
            )
        }
    }

    fun updateUserAnswer(answer: String) {
        userAnswer = answer
    }

    fun checkUserAnswer() {
        if (userAnswer.toInt() == currentEquation.answer) {
            val updatedExp = _uiSessionState.value.exp.plus(EXP_INCREASE)
            val updateAnswerCorrect = _uiSessionState.value.correctAnswers.plus(1)
            _uiEquationState.update {
                it.copy(
                    answer = currentEquation.answer,
                    answerUser = userAnswer.toInt(),
                    isCorrect = true
                )
            }
            val currentCalendar = Calendar.getInstance()
            timeEnd = currentCalendar.timeInMillis
            val timeBetween = timeEnd - timeStart
            updateTimeEquation(timeBetween)
            updateDateEquation(getCurrentDateTime())

            currentListExercises.add(_uiEquationState.value)
            _uiSessionState.update {
                it.copy(
                    correctAnswers = updateAnswerCorrect
                )
            }
            updateSessionExp(updatedExp = updatedExp)
        } else {
            val updateAnswerIncorrect = _uiSessionState.value.incorrectAnswers.plus(1)
            _uiEquationState.update {
                it.copy(
                    answer = currentEquation.answer,
                    answerUser = userAnswer.toInt()
                )
            }
            _uiSessionState.update {
                it.copy(
                    incorrectAnswers = updateAnswerIncorrect
                )
            }
            val currentCalendar = Calendar.getInstance()
            timeEnd = currentCalendar.timeInMillis
            val timeBetween = timeEnd - timeStart
            updateTimeEquation(timeBetween)
            updateDateEquation(getCurrentDateTime())

            currentListExercises.add(_uiEquationState.value)
            updateSessionExp(_uiSessionState.value.exp)
        }

        updateUserAnswer("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateSessionExp(updatedExp: Int) {
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

            _uiSessionState.update {
                it.copy(
                    time = timeBetween
                )
            }

            currentListSession.add(_uiSessionState.value)
        } else {
            _uiSessionState.update {
                it.copy(
                    exp = updatedExp,
                    currentExerciseCount = it.currentExerciseCount.inc()
                )
            }
            selectEquationBasedOnDifficulty()
        }
    }

    private fun selectEquationBasedOnDifficulty() {
        when (_uiSessionState.value.difficulty) {
            0 -> {
                _uiEquationState.value = pickRandomEquation()
            }

            1 -> {
                _uiEquationState.value = pickRandomEquationFraction()
            }

            2 -> {}
            3 -> {}
        }
    }

    fun skipEquation() {
        updateSessionExp(_uiSessionState.value.exp)
        updateUserAnswer("")
    }

    private fun getCurrentDateTime(): String {
        val currentCalendar = Calendar.getInstance()
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)
        val currentSecond = currentCalendar.get(Calendar.SECOND)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth =
            currentCalendar.get(Calendar.MONTH) + 1
        val currentYear = currentCalendar.get(Calendar.YEAR)

        return "Hora: $currentHour:$currentMinute:$currentSecond\nFecha: $currentDay/$currentMonth/$currentYear"
    }
}