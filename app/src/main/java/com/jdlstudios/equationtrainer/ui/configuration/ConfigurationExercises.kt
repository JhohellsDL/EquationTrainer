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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.draw.clip
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
import java.util.Calendar

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

            CardDifficulty(
                onDifficultyLevel = {
                    sessionViewModel.updateDifficulty(it)
                    Log.d("Configuration", "dificultad : ${it.description}")
                }
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
                sessionViewModel.updateDateSession(getCurrentDateTime())
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
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Selecciona el nivel\n de dificultad",
                style = MaterialTheme.typography.titleMedium,
                modifier = modifier
                    .wrapContentWidth(unbounded = true),
            )
            CurrentDateTime()
        }

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
        Text(
            text = "Selecciona la cantidad de ejercicios",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp, bottom = 12.dp)
                .padding(horizontal = 16.dp)
        )
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
                    .padding(vertical = 16.dp),
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

@Preview
@Composable
fun CurrentDateTime() {
    val currentCalendar = remember { Calendar.getInstance() }
    val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = currentCalendar.get(Calendar.MINUTE)
    val currentSecond = currentCalendar.get(Calendar.SECOND)
    val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
    val currentMonth =
        currentCalendar.get(Calendar.MONTH) + 1 // Los meses en Calendar comienzan desde 0
    val currentYear = currentCalendar.get(Calendar.YEAR)

    val currentDateString =
        "Hora: $currentHour:$currentMinute:$currentSecond\nFecha: $currentDay/$currentMonth/$currentYear"

    Text(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceTint)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .wrapContentWidth(),
        text = "Hora: $currentHour:$currentMinute:$currentSecond\nFecha: $currentDay/$currentMonth/$currentYear",
        color = MaterialTheme.colorScheme.onPrimary
    )

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
