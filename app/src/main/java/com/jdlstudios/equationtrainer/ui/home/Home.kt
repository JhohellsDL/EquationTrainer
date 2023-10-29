package com.jdlstudios.equationtrainer.ui.home

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.theme.AppTheme


@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    AppTheme {
        Surface(tonalElevation = 5.dp) {
            Home(navHostController = rememberNavController())
        }
    }
}

@Preview
@Composable
fun Preview() {
    AppTheme {
        Home(navHostController = rememberNavController())
    }
}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    navHostController: NavHostController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Equation Trainer",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 72.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ecuacion),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(32.dp)
            )
            Text(
                text = stringResource(R.string.description_app),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    navHostController.navigateSingleTopTo(ConfigurationSession.route)
                },
                modifier = Modifier
                    .padding(top = 54.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.button_start_home),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


