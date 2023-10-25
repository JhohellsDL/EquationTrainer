package com.jdlstudios.equationtrainer.ui.exercises

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.navigation.Home
import com.jdlstudios.equationtrainer.ui.theme.AppTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark() {
    AppTheme {
        ExerciseEasy(viewModel(), rememberNavController())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseEasy(
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController
) {
    val equationState by sessionViewModel.uiEquationState.collectAsState()
    val sessionState by sessionViewModel.uiSessionState.collectAsState()
    var isErrorInputText by remember { mutableStateOf(true) }

    Log.d("asdasd", "Session: $sessionState")
    Log.d("asdasd", "Equation: $equationState")

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val difficultyText = DifficultyLevel.values()[1]

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SessionDifficulty(
                    difficultyLevel = difficultyText
                )
                SessionExp(
                    exp = sessionState.exp
                )
            }

            Timer()

            CardExercise(
                onUserAnswer = {
                    isErrorInputText = it == ""
                    sessionViewModel.updateUserAnswer(it)
                },
                onKeyboardDone = {
                    isErrorInputText = true
                    sessionViewModel.checkUserAnswer()
                },
                userAnswer = sessionViewModel.userAnswer,
                currentEquation = equationState.equation,
                numberOfExercises = sessionState.numberOfExercises,
                equationCount = sessionState.currentExerciseCount,
                isErrorInputText = isErrorInputText
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isErrorInputText,
                    onClick = {
                        isErrorInputText = true
                        sessionViewModel.checkUserAnswer()
                    }
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Timer() {
    val time = LocalTime.now()

    Text(
        text = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardExercise(
    onUserAnswer: (String) -> Unit,
    onKeyboardDone: () -> Unit,
    userAnswer: String,
    currentEquation: String,
    numberOfExercises: Int,
    equationCount: Int,
    isErrorInputText: Boolean,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 16.dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
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
                style = MaterialTheme.typography.displayMedium,
                modifier = modifier
                    .padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = stringResource(R.string.instructions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .padding(vertical = 16.dp)
            )
            OutlinedTextField(
                value = userAnswer,
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onUserAnswer,
                label = { Text(text = "Valor de X") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (!isErrorInputText) {
                            onKeyboardDone()
                        }
                    }
                )
            )
        }
    }
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

@Composable
fun PreviewStatus() {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SessionDifficulty(difficultyLevel = DifficultyLevel.Challenge)
        SessionExp(exp = 12)
    }

}

@Composable
fun SessionExp(exp: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .wrapContentWidth(unbounded = true)
    ) {
        Text(
            text = stringResource(R.string.experience, exp),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun SessionDifficulty(difficultyLevel: DifficultyLevel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .wrapContentWidth(unbounded = true)
    ) {
        Text(
            text = stringResource(R.string.difficulty, difficultyLevel.description),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}
