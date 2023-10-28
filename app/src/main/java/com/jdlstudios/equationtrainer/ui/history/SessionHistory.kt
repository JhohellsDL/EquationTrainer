package com.jdlstudios.equationtrainer.ui.history

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.data.local.entities.SessionEntity
import com.jdlstudios.equationtrainer.domain.models.Equation
import com.jdlstudios.equationtrainer.domain.models.Session
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.theme.AppTheme


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ScreenPreview() {
    AppTheme {
        SessionHistoryScreen(
            sessionViewModel = viewModel(),
            navHostController = rememberNavController()
        )
    }
}

@Composable
fun SessionHistoryScreen(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController
) {
    val uiSessionState by sessionViewModel.uiSessionState.collectAsState()
    val listCurrent = sessionViewModel.getListSession()
    Log.d("asdasd", "LISTAAAAAAASADSADAS !! :  $listCurrent")
    SessionRow(
        listSessions = listCurrent
    )
    // Implement composable here
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun RowPreview() {
    AppTheme {
        SessionRow(
            listSessions = listSessionsTest
        )
    }
}

private val listSessionsTest = listOf<Session>(
    Session(
        difficulty = 1,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "27 Octubre 2023 - 15:30:09"
    ),
    Session(
        difficulty = 0,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "27 Octubre 2023 - 15:30:09"
    ),
    Session(
        difficulty = 3,
        numberOfExercises = 2,
        correctAnswers = 2,
        incorrectAnswers = 3,
        exp = 44,
        date = "27 Octubre 2023 - 15:30:09"
    )

)

// Step: Align your body row - Arrangements
@Composable
fun SessionRow(
    modifier: Modifier = Modifier,
    listSessions: List<Session>
) {
    Log.d("asdasd", "LISTA fasdfa !! :  $listSessions")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
    ) {
        if (listSessions.isEmpty()) {

        } else {
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
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Fecha: ${session.date}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Dificultad de la sesion: ${DifficultyLevel.values()[session.difficulty]}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Numero de ejercicios: ${session.numberOfExercises}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Respuestas correctas ${session.correctAnswers}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Respuestas incorrectas ${session.incorrectAnswers}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Experiencia: ${session.exp}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    // Implement composable here 017085605
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
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
                date = "27 Octubre 2023 - 15:30:09"
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}