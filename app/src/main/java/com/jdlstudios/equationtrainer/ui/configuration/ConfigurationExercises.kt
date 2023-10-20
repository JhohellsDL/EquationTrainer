package com.jdlstudios.equationtrainer.ui.configuration

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.ui.theme.AppTheme
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.navigation.ExercisesEasy

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DarkPreview() {
    AppTheme {
        Configuration(navHostController = rememberNavController())
    }
}

@Composable
fun Configuration(
    navHostController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Text(
                text = "Selecciona el nivel de dificultad",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            CardDifficulty()
            Text(
                text = "Selecciona la cantidad de ejercicios",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp, bottom = 12.dp)
                    .padding(horizontal = 16.dp)
            )
            CardSelectedQuantity()
        }
        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth(),
            onClick = {
                navHostController.navigateSingleTopTo(ExercisesEasy.route)
                Log.d("asdasd", "asdsadasd")
            },
        ) {
            Text(text = "COMENZAR")
        }
    }
}

@Preview
@Composable
fun CardDifficulty(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp
        )
    ) {
        RadioGroupDifficulty()
    }
}

@Preview
@Composable
fun CardSelectedQuantity(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        SliderQuantityExercises()
    }
}

@Preview
@Composable
fun RadioGroupDifficulty() {
    val radioOptions = listOf(
        DifficultyLevel.Easy,
        DifficultyLevel.Intermediate,
        DifficultyLevel.Challenge,
        DifficultyLevel.Advanced
    )
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Column(Modifier.selectableGroup()) {
        radioOptions.forEach {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .selectable(
                        selected = (it == selectedOption),
                        onClick = {
                            onOptionSelected(it)
                            Log.d("asdasd", "test : ${it.description}")
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
                        Log.d("asdasd", "test : ${it.description}")
                    } // null recommended for accessibility with screenreaders
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

@Preview
@Composable
fun SliderQuantityExercises(modifier: Modifier = Modifier) {
    var sliderPosition by remember { mutableStateOf(0f) }
    Column {
        Text(
            text = sliderPosition.toInt().toString(),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 32.dp)
        )
        Log.d("asdasd", "test : $sliderPosition")
        Slider(
            modifier = modifier
                .padding(horizontal = 32.dp)
                .padding(vertical = 20.dp),
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 18,
            valueRange = 0f..20f
        )
    }
}

@Preview
@Composable
fun SwitchIcon() {
    var checked by remember { mutableStateOf(true) }

    val icon: (@Composable () -> Unit)? = if (checked) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }

    Switch(
        modifier = Modifier.semantics { contentDescription = "Activate Slider" },
        checked = checked,
        onCheckedChange = { checked = it },
        thumbContent = icon
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TextFieldQuantity() {
    var text by rememberSaveable { mutableStateOf("0") }
    OutlinedTextField(
        modifier = Modifier.width(150.dp),
        value = text,
        onValueChange = { text = it },
        label = { Text("Number exercises") }
    )
}

@Preview
@Composable
fun cardSelectionDifficulty() {
    Card {
        Column {

        }
    }
}