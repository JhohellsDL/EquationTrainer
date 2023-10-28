package com.jdlstudios.equationtrainer.ui.configuration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import kotlinx.coroutines.flow.zip

class SessionViewModel : ViewModel() {

    private val _uiSessionState = MutableStateFlow(Session())
    val uiSessionState: StateFlow<Session> = _uiSessionState.asStateFlow()

    private val _uiEquationState = MutableStateFlow(Equation())
    val uiEquationState: StateFlow<Equation> = _uiEquationState.asStateFlow()

    private var currentListExercises: MutableList<Equation> = mutableListOf()
    private lateinit var currentEquation: Equation

    private var listExercises: MutableList<Equation> = mutableListOf()

    var userAnswer by mutableStateOf("")
        private set

    fun getListEquations(): List<Equation>{
        Log.d("asdasd", "LISTA get VIEWMODEL !! :  $currentListExercises")
        return currentListExercises
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

    fun updateUserAnswer(answer: String) {
        Log.d("asdasd", "update answer: $answer")
        userAnswer = answer
    }

    fun checkUserAnswer() {
        if (userAnswer.toInt() == currentEquation.answer) {
            val updatedExp = _uiSessionState.value.exp.plus(EXP_INCREASE)

            _uiEquationState.update {
                it.copy(
                    isCorrect = true
                )
            }
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.equation}")
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.isCorrect}")

            updateSessionExp(updatedExp = updatedExp)

            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.equation}")
            Log.d("asdasdasd", "update answer: ${_uiEquationState.value.isCorrect}")
        } else {
            updateSessionExp(_uiSessionState.value.exp)
        }
        Log.d("asdasd", "LISTA get VIEWMODEL --- CHECK !! :  $currentListExercises")
        currentListExercises = listExercises
        updateUserAnswer("")
    }

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
        } else {
            _uiSessionState.update {
                it.copy(
                    exp = updatedExp,
                    currentExerciseCount = it.currentExerciseCount.inc()
                )
            }
            _uiEquationState.value = pickRandomEquation()
        }
    }

    fun skipEquation() {
        updateSessionExp(_uiSessionState.value.exp)
        updateUserAnswer("")
    }
}