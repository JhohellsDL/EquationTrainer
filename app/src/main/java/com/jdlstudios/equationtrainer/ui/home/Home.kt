package com.jdlstudios.equationtrainer.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.compose.AppTheme
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.navigation.Configuration


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    AppTheme {
        Home(navHostController = rememberNavController())
    }
}

@Composable
fun Home(
    navHostController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .background(MaterialTheme.colorScheme.onPrimary)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ecuacion),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(12.dp)
            )
            Text(
                text = "¡Hola! Bienvenido a Equation Trainer!.",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Aquí puedes practicar tus habilidades matemáticas resolviendo ecuaciones de diferentes tipos.",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = {
                    navHostController.navigateSingleTopTo(Configuration.route)
                    Log.d("asdasd", "asdsadasd")
                },
                modifier = Modifier
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Empezar",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


