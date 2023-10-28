package com.jdlstudios.equationtrainer.ui.configuration

import android.util.Log
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.navigation.ExercisesEasy
import com.jdlstudios.equationtrainer.ui.theme.AppTheme

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun DarkPreview() {
    AppTheme {
        ConfigurationSession(
            sessionViewModel = viewModel(),
            navHostController = rememberNavController()
        )
    }
}

@Composable
fun ConfigurationSession(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController
) {
    val uiSessionState by sessionViewModel.uiSessionState.collectAsState()

    var isCardVisible by remember { mutableStateOf(false) }
    var isEnabledButton: Boolean = false
    if (uiSessionState.numberOfExercises != 0) {
        isEnabledButton = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState(), enabled = true),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(
                text = "Selecciona el nivel de dificultad",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            CardDifficulty(
                onDifficultyLevel = {
                    sessionViewModel.updateDifficulty(it)
                    Log.d("Configuration", "dificultad : ${it.description}")
                }
            )
            Text(
                text = "Selecciona la cantidad de ejercicios",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp)
            )
            CardSelectedQuantity(
                onNumberOfExercises = {
                    sessionViewModel.updateNumberExercises(it)
                }
            )
        }
        Button(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            onClick = {
                isCardVisible = true
                sessionViewModel.resetSession()
            },
            enabled = isEnabledButton
        ) {
            Text(text = "COMENZAR")
        }
    }

    if (isCardVisible) {
        PreviewSessionDialog(
            difficulty = DifficultyLevel.values()[uiSessionState.difficulty],
            nroExercises = uiSessionState.numberOfExercises,
            onPlayAgain = {
                //guardar la session antes de pasar de pantalla
                navHostController.navigateSingleTopTo(ExercisesEasy.route)
            },
            onExit = { isCardVisible = false },
            navHostController = navHostController
        )
    }
}

@Composable
fun CardDifficulty(
    onDifficultyLevel: (DifficultyLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        val radioOptions = listOf(
            DifficultyLevel.Easy,
            DifficultyLevel.Intermediate,
            DifficultyLevel.Challenge,
            DifficultyLevel.Advanced
        )
        val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
        Column(modifier.selectableGroup()) {
            radioOptions.forEach {

                Row(
                    modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = (it == selectedOption),
                            onClick = {
                                onOptionSelected(it)
                                onDifficultyLevel(it)
                                Log.d("Configuration", "dificultad : ${it.description}")
                            },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (it == selectedOption),
                        onClick = {
                            onOptionSelected(it)
                            onDifficultyLevel(it)
                        }
                    )
                    Text(
                        text = it.description,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CardSelectedQuantity(
    onNumberOfExercises: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        var sliderPosition by remember { mutableFloatStateOf(0f) }

        Column {
            Text(
                text = sliderPosition.toInt().toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            )
            Slider(
                modifier = modifier
                    .padding(horizontal = 32.dp)
                    .padding(vertical = 20.dp),
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    onNumberOfExercises(it.toInt())
                    Log.d("Configuration", "cantidad : ${it.toInt()}")
                },
                steps = 18,
                valueRange = 0f..20f
            )
        }
    }
}

@Preview
@Composable
fun previewAlert2() {
    PreviewSessionDialog(
        difficulty = DifficultyLevel.Advanced,
        nroExercises = 2,
        onPlayAgain = {},
        onExit = { },
        navHostController = rememberNavController()
    )
}

@Composable
private fun PreviewSessionDialog(
    difficulty: DifficultyLevel,
    nroExercises: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onExit,
        title = { Text(text = "Estas iniciando una session!") },
        text = { Text(text = "Dificultad: ${difficulty.description} \nNumero de ejercicios: $nroExercises") },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onExit
            ) {
                Text(text = "Salir")
            }
        },
        confirmButton = {
            TextButton(
                onClick = onPlayAgain
            ) {
                Text(text = "Continuar")
            }
        }
    )
}

