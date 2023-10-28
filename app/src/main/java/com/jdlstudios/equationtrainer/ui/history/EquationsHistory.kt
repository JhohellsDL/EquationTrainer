package com.jdlstudios.equationtrainer.ui.history

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.domain.models.Equation
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.theme.AppTheme

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ScreenContentPreview() {
    AppTheme {
        EquationsHistoryScreen(
            sessionViewModel = viewModel(),
            navHostController = rememberNavController()
        )
    }
}

@Composable
fun EquationsHistoryScreen(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController
) {
    val uiSessionState by sessionViewModel.uiSessionState.collectAsState()
    val listCurrent = sessionViewModel.getListEquations()
    Log.d("asdasd", "LISTAAAAAAASADSADAS !! :  $listCurrent")
    AlignYourBodyRow(listaEquations = listCurrent)
    // Implement composable here
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyRowPreview() {
    AppTheme { AlignYourBodyRow(
        listaEquations = listEquationsTest)
    }
}

private val listEquationsTest = listOf<Equation>(
    Equation(
        equation = "3x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100,
        date = "27 Octubre 2023 - 15:30:09",
        isCorrect = false
    ),
    Equation(
        equation = "2x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100,
        date = "27 Octubre 2023 - 15:30:09",
        isCorrect = false
    ),
    Equation(
        equation = "1x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100,
        date = "27 Octubre 2023 - 15:30:09",
        isCorrect = false
    )

)

// Step: Align your body row - Arrangements
@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier,
    listaEquations: List<Equation>
) {
    Log.d("asdasd", "LISTA fasdfa !! :  $listaEquations")
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = modifier
    ) {
        if (listaEquations.isEmpty()){

        } else {
            items(listaEquations) {
                FavoriteCollectionCard(equation = it)
            }
        }

    }
}

@Composable
fun FavoriteCollectionCard(
    equation: Equation,
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
                text = "Equation: ${equation.equation}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Fecha: ${equation.date}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "Respuesta Usuario: X = ${equation.answerUser}",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = "${equation.isCorrect}",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
    // Implement composable here 017085605
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun FavoriteCollectionCardPreview() {
    AppTheme {
        FavoriteCollectionCard(
            equation = Equation(
                equation = "1x + 3 = 3",
                answer = 2,
                answerUser = 4,
                time = 100,
                date = "27 Octubre 2023 - 15:30:09",
                isCorrect = false
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}