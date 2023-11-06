package com.jdlstudios.equationtrainer.ui.exercises

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.gms.ads.rewarded.ServerSideVerificationOptions
import com.jdlstudios.equationtrainer.R
import com.jdlstudios.equationtrainer.domain.models.EquationFractionTypeOne
import com.jdlstudios.equationtrainer.domain.utils.DifficultyLevel
import com.jdlstudios.equationtrainer.domain.utils.Utilities.solutionEquation
import com.jdlstudios.equationtrainer.navigateSingleTopTo
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.navigation.Home
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview
@Composable
fun PreviewDark() {
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExerciseEasy(
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel,
    navHostController: NavHostController,
    context: Context
) {

    val equationState by sessionViewModel.uiEquationState.collectAsState()
    val equationFraction by sessionViewModel.uiEquationFractionState.collectAsState()
    val sessionState by sessionViewModel.uiSessionState.collectAsState()
    var isErrorInputText by remember { mutableStateOf(true) }
    var helpSolution by remember { mutableStateOf(false) }
    var buttonActive by remember { mutableStateOf(false) }

    var mInterstitialAd: InterstitialAd? = null
    var rewardedAd: RewardedAd? = null
    val adRequest = AdRequest.Builder().build()

    InterstitialAd.load(
        context,
        "ca-app-pub-8897050281816485/9904616074",
        adRequest,
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd

            }
        })
    RewardedAd.load(
        context,
        "ca-app-pub-8897050281816485/5888616922",
        adRequest,
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
            }

            override fun onAdLoaded(ad: RewardedAd) {
                buttonActive = true
                rewardedAd = ad

                val options = ServerSideVerificationOptions.Builder()
                    .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                    .build()
                rewardedAd!!.setServerSideVerificationOptions(options)
            }
        })

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val difficultyText = DifficultyLevel.values()[1]
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
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
                difficulty = sessionState.difficulty,
                equationFraction = equationFraction,
                helpSolutionActive = {

                    rewardedAd?.let { ad ->
                        ad.show(context as Activity, OnUserEarnedRewardListener { rewardItem ->
                            // Handle the reward.
                            val rewardAmount = rewardItem.amount
                            val rewardType = rewardItem.type
                        })
                    } ?: run {}

                    helpSolution = true
                },
                buttonActive = buttonActive,
                isErrorInputText = isErrorInputText
            )
            Spacer(modifier = modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Button(
                    modifier = modifier.fillMaxWidth(),
                    enabled = !isErrorInputText,
                    onClick = {
                        isErrorInputText = true
                        sessionViewModel.checkUserAnswer()
                    }
                ) {
                    Text(
                        text = "Comprobar",
                        fontSize = 16.sp
                    )
                }

                OutlinedButton(
                    onClick = {
                        sessionViewModel.skipEquation()
                    },
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Saltar Pregunta",
                        fontSize = 16.sp
                    )
                }
            }

            if (sessionState.isGameOver) {
                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(context as Activity)
                }
                FinalScoreDialog(
                    exp = sessionState.exp,
                    onPlayAgain = {
                        sessionViewModel.updateGameOver(false)
                        sessionViewModel.cleanSession()
                        navHostController.navigateSingleTopTo(ConfigurationSession.route)
                    },
                    onExit = {
                        sessionViewModel.updateGameOver(false)
                        sessionViewModel.cleanSession()
                        navHostController.navigateSingleTopTo(Home.route)
                    }
                )
            }

            if (helpSolution) {
                HelpDialog(
                    equationFraction = equationFraction,
                    onPlayAgain = { /*TODO*/ },
                    onExit = {
                        RewardedAd.load(
                            context,
                            "ca-app-pub-8897050281816485/5888616922",
                            adRequest,
                            object : RewardedAdLoadCallback() {
                                override fun onAdFailedToLoad(adError: LoadAdError) {
                                    rewardedAd = null
                                }

                                override fun onAdLoaded(ad: RewardedAd) {
                                    buttonActive = true
                                    rewardedAd = ad

                                    val options = ServerSideVerificationOptions.Builder()
                                        .setCustomData("SAMPLE_CUSTOM_DATA_STRING")
                                        .build()
                                    rewardedAd!!.setServerSideVerificationOptions(options)
                                }
                            })
                        helpSolution = false
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
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceTint)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        text = time.format(DateTimeFormatter.ofPattern("HH:mm:ss")),
        color = MaterialTheme.colorScheme.onPrimary
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
    difficulty: Int,
    equationFraction: EquationFractionTypeOne,
    helpSolutionActive: () -> Unit,
    buttonActive: Boolean,
    modifier: Modifier = Modifier
) {
    val mediumPadding = 16.dp

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(mediumPadding)
        ) {
            Text(
                modifier = modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceTint)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
                    .align(alignment = Alignment.End),
                text = "$equationCount/$numberOfExercises",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Timer()
            when (difficulty) {
                0 -> {
                    Text(
                        text = currentEquation,
                        style = MaterialTheme.typography.displayMedium,
                        modifier = modifier
                            .padding(top = 24.dp),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }

                1 -> {
                    val numerator = equationFraction.partNumberFraction.first
                    val denominator = equationFraction.partNumberFraction.second
                    val independentTerm = equationFraction.independentTerm
                    val result = equationFraction.result
                    Row {
                        Column {
                            Text(
                                text = numerator.toString(),//currentEquation
                                style = MaterialTheme.typography.displayMedium,
                                modifier = modifier
                                    .padding(top = 24.dp),
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Divider(
                                modifier = modifier
                                    .width(30.dp),
                                thickness = 2.dp,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Text(
                                text = denominator.toString(),
                                style = MaterialTheme.typography.displayMedium,
                                modifier = modifier,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                        Text(
                            text = "x + $independentTerm = $result",//currentEquation
                            style = MaterialTheme.typography.displayMedium,
                            modifier = modifier
                                .padding(vertical = 24.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                2 -> {}
                3 -> {}
            }


            Text(
                text = stringResource(R.string.instructions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                modifier = modifier
                    .padding(vertical = 16.dp)
            )
            when (difficulty) {
                0 -> {

                }

                1 -> {
                    Button(
                        onClick = helpSolutionActive,
                        enabled = buttonActive
                    ) {
                        Text(text = "Mostrar solución")
                        Image(
                            modifier = modifier.padding(start = 4.dp),
                            painter = painterResource(R.drawable.baseline_ondemand_video_24),
                            contentDescription = ""
                        )
                        Log.d("qweqweqwe", "presionado Help!!")
                    }
                }
            }

            OutlinedTextField(
                value = userAnswer,
                singleLine = true,
                shape = MaterialTheme.shapes.large,
                modifier = modifier.fillMaxWidth(),
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
                Text(text = "Salir")
            }
        },
        confirmButton = {
            TextButton(onClick = onPlayAgain) {
                Text(text = "Otra vez")
            }
        }
    )
}

@Composable
private fun HelpDialog(
    equationFraction: EquationFractionTypeOne,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onExit,
        title = {
            Text(
                style = MaterialTheme.typography.displayMedium,
                text = "Solución"
            )
        },
        text = {
            Text(
                text = solutionEquation(equationFraction)
            )
        },
        modifier = modifier.wrapContentWidth(unbounded = true),
        dismissButton = {
            TextButton(
                onClick = onExit
            ) {
                Text(text = "Cerrar")
            }
        },
        confirmButton = {},
        tonalElevation = 4.dp
    )
}

@Preview
@Composable
fun PreviewHelps() {
    HelpDialog(equationFraction = EquationFractionTypeOne(
        partNumberFraction = Pair(4, 6),
        variable = 9,
        independentTerm = 7,
        result = 13
    ),
        onPlayAgain = { /*TODO*/ },
        onExit = { /*TODO*/ }
    )
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
fun SessionDifficulty(
    difficultyLevel: DifficultyLevel
) {
    Text(
        text = stringResource(R.string.difficulty, difficultyLevel.description),
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(8.dp)
    )
}
