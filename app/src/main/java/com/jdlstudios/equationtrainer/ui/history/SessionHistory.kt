package com.jdlstudios.equationtrainer.ui.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jdlstudios.equationtrainer.domain.models.Session
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ScreenPreview() {
    AppTheme {
        SessionHistoryScreen(
            sessionViewModel = viewModel()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SessionHistoryScreen(
    sessionViewModel: SessionViewModel
) {
    val listCurrent = sessionViewModel.getListSession()
    AppTheme {
        SessionRow(
            listSessions = listCurrent
        )
    }
    // Implement composable here
}

@Preview
@Composable
fun RowPreview() {
    AppTheme {
        SessionRow(
            listSessions = listSessionsTest
        )
    }
}

private val listSessionsTest = listOf(
    Session(
        difficulty = 1,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "Hora: 16:14:29\nFecha: 28/10/2023"
    ),
    Session(
        difficulty = 0,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "Hora: 16:14:29\nFecha: 28/10/2023"
    ),
    Session(
        difficulty = 3,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "Hora: 16:14:29\nFecha: 28/10/2023"
    )

)

// Step: Align your body row - Arrangements
@Composable
fun SessionRow(
    modifier: Modifier = Modifier,
    listSessions: List<Session>
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
    ) {
        if (listSessions.isNotEmpty()) {
            items(listSessions) {
                SessionItemCard(session = it)
            }
        }

    }
}

@Composable
fun SessionItemCard(
    session: Session,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(horizontal = 12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier
                    .weight(3f)
                    .padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Sesion: ${DifficultyLevel.values()[session.difficulty]}",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Tiempo: ${milisegundosATiempo(session.time)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Nro de ejercicios: ${session.numberOfExercises}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Respuestas correctas ${session.correctAnswers}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Respuestas incorrectas ${session.incorrectAnswers}",
                    style = MaterialTheme.typography.bodySmall
                )

            }
            Column(
                modifier = modifier
                    .weight(2f)
                    .padding(horizontal = 12.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceTint)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(alignment = Alignment.End),
                    text = "Exp: ${session.exp}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = session.date,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier
                        .padding(8.dp)
                        .wrapContentSize(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }
    // Implement composable here 017085605
}

@Preview
@Composable
fun CollectionCardPreview() {
    AppTheme {
        SessionItemCard(
            session = Session(
                difficulty = 1,
                numberOfExercises = 2,
                correctAnswers = 2,
                incorrectAnswers = 3,
                exp = 44,
                date = "Hora: 16:14:29\nFecha: 28/10/2023"
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

fun milisegundosATiempo(milisegundos: Long): String {
    val seconds = milisegundos / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    val remainingSeconds = seconds % 60
    return String.format("%02d:%02d:%02d", hours, remainingMinutes, remainingSeconds)
}