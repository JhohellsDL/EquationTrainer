package com.jdlstudios.equationtrainer.ui.exercises

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.navigation.Home
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.theme.AppTheme
import com.jdlstudios.equationtrainer.ui.theme.typography
import kotlinx.coroutines.delay

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    AppTheme {
        ExerciseEasy(viewModel(), rememberNavController())
    }

}

@Composable
fun ExerciseEasy(
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController
) {
    val equationState by sessionViewModel.uiEquationState.collectAsState()
    val sessionState by sessionViewModel.uiSessionState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Equation Trainer",
                style = typography.titleSmall
            )
            CardExercise(
                onUserAnswer = {
                    sessionViewModel.updateUserAnswer(it)
                },
                userAnswer = sessionViewModel.userAnswer,
                currentEquation = equationState.equation,
                numberOfExercises = sessionState.numberOfExercises,
                equationCount = sessionState.currentExerciseCount,
                modifier = Modifier.padding(16.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { sessionViewModel.checkUserAnswer() }
                ) {
                    Text(
                        text = "Continuar",
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    onClick = { sessionViewModel.skipEquation() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Saltar Pregunta",
                        fontSize = 16.sp
                    )
                }
            }

            if (sessionState.isGameOver) {
                FinalScoreDialog(
                    exp = sessionState.exp,
                    onPlayAgain = {
                        sessionViewModel.resetSession()
                        sessionViewModel.updateGameOver(false)
                        navHostController.navigateSingleTopTo(ConfigurationSession.route)
                    },
                    onExit = {
                        sessionViewModel.updateGameOver(false)
                        navHostController.navigateSingleTopTo(Home.route)
                    }
                )
            }
        }
    }
}

@Composable
fun Timer() {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }

    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(1000) // Espera 1 segundo
            elapsedSeconds++
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tiempo transcurrido: $elapsedSeconds segundos",
            style = MaterialTheme.typography.titleLarge
        )

        if (!isRunning) {
            Button(
                onClick = { isRunning = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Iniciar")
            }
        } else {
            Button(
                onClick = { isRunning = false },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Detener")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardExercise(
    onUserAnswer: (String) -> Unit,
    userAnswer: String,
    currentEquation: String,
    numberOfExercises: Int,
    equationCount: Int,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 16.dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(mediumPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = "$equationCount/$numberOfExercises",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = currentEquation,
                style = MaterialTheme.typography.displayMedium
            )
            Text(
                text = stringResource(R.string.instructions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Timer()
            OutlinedTextField(
                value = userAnswer,
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onUserAnswer,
                label = {
                    Text(text = "Ingrese el valor de X")
                },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = { //onKeyboardDone()
                    }
                )
            )
        }
    }
}


@Preview
@Composable
fun previewAlert() {
    FinalScoreDialog(exp = 12, onPlayAgain = {}, onExit = {})
}

@Composable
private fun FinalScoreDialog(
    exp: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onExit,
        title = { Text(text = "Felicitaciones") },
        text = { Text(text = "tienes $exp de exp") },
        modifier = modifier,
        dismissButton = {
            TextButton(
                onClick = onExit
            ) {
                Text(text = "SAlir")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "otra vez")
            }
        }
    )
}
