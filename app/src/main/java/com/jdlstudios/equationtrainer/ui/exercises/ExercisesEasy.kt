package com.jdlstudios.equationtrainer.ui.exercises

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.theme.AppTheme
import kotlinx.coroutines.delay


@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDark(modifier: Modifier = Modifier.fillMaxSize()) {

    AppTheme {
        ExerciseEasy()
    }

}

@Preview
@Composable
fun ExerciseEasy(
    sessionViewModel: SessionViewModel = viewModel()
) {
    val sessionState by sessionViewModel.uiEquationState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            CardExercise(
                onUserGuessChanged = {
                    Log.d("asdasd", "asd: $it")
                },
                currentEquation = sessionState.equation,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            //CollectionAnswers()
        }
    }
}

@Composable
fun Timer() {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedSeconds by remember { mutableStateOf(0) }

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
    onUserGuessChanged: (String) -> Unit,
    currentEquation: String,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 16.dp
    var text by remember { mutableStateOf("") }
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
                text = "0/10",
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
                value = "userGuess",
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = onUserGuessChanged,
                label = { Text("Etiqueta del campo") },
                isError = false,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { //onKeyboardDone()
                    }
                )
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardAnswer(
    answer: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        ElevatedCard(
            onClick = { /* Do something */ },
            modifier = Modifier.size(width = 150.dp, height = 100.dp)
        ) {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = answer.toString(),
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewCardAnswer() {
    CardAnswer(answer = 3)
}

@Preview
@Composable
fun CollectionAnswers(modifier: Modifier = Modifier) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(250.dp)
    ) {
        items(listAnswers) {
            CardAnswer(it)
        }
    }
}

private val listAnswers = listOf<Int>(
    2, 4, 5, 7
)
