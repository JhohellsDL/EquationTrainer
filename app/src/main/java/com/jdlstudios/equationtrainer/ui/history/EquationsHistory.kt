package com.jdlstudios.equationtrainer.ui.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.jdlstudios.equationtrainer.domain.models.Equation
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun ScreenContentPreview() {
    AppTheme {
        EquationsHistoryScreen(
            sessionViewModel = viewModel()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EquationsHistoryScreen(
    sessionViewModel: SessionViewModel
) {
    val listCurrent = sessionViewModel.getListEquations()
    AlignYourBodyRow(listaEquations = listCurrent)
    // Implement composable here
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyRowPreview() {
    AppTheme {
        AlignYourBodyRow(
            listaEquations = listEquationsTest
        )
    }
}

private val listEquationsTest = listOf(
    Equation(
        equation = "3x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100L,
        date = "Hora: 16:14:29\nFecha: 28/10/2023",
        isCorrect = false
    ),
    Equation(
        equation = "2x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100L,
        date = "Hora: 16:14:29\nFecha: 28/10/2023",
        isCorrect = false
    ),
    Equation(
        equation = "1x + 3 = 3",
        answer = 2,
        answerUser = 4,
        time = 100L,
        date = "Hora: 16:14:29\nFecha: 28/10/2023",
        isCorrect = false
    )

)

// Step: Align your body row - Arrangements
@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier,
    listaEquations: List<Equation>
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AnuncioSuperiorEquation()
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = modifier
        ) {
            if (listaEquations.isNotEmpty()) {
                items(listaEquations) {
                    FavoriteCollectionCard(equation = it)
                }
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
                    text = equation.equation,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Tiempo: ${milisegundosATiempo(equation.time)}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "Respuesta Usuario: ${equation.answerUser}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "Respuesta: ${equation.answer}",
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
                var isCorrectText = "Incorrecto"
                if (equation.isCorrect) isCorrectText = "Correcto"
                Text(
                    modifier = modifier
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceTint)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                        .align(alignment = Alignment.End),
                    text = isCorrectText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = equation.date,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = modifier
                        .padding(8.dp)
                        .wrapContentSize(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }

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
                date = "Hora: 16:14:29\nFecha: 28/10/2023",
                isCorrect = false
            ),
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun AnuncioSuperiorEquation() {
    val adWith = LocalConfiguration.current.screenWidthDp - 32
    AndroidView(
        factory = {
            val adView = AdView(it)
            adView.setAdSize(AdSize(adWith, 50))
            adView.apply {
                adUnitId = "ca-app-pub-8897050281816485/8741360603"
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
