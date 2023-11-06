package com.jdlstudios.equationtrainer

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.jdlstudios.equationtrainer.components.AppTabRow
import com.jdlstudios.equationtrainer.ui.configuration.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.configuration.SessionViewModel
import com.jdlstudios.equationtrainer.ui.exercises.ExerciseEasy
import com.jdlstudios.equationtrainer.ui.history.EquationsHistoryScreen
import com.jdlstudios.equationtrainer.ui.history.SessionHistoryScreen
import com.jdlstudios.equationtrainer.ui.home.Home
import com.jdlstudios.equationtrainer.ui.navigation.ConfigurationSession
import com.jdlstudios.equationtrainer.ui.navigation.EquationsHistory
import com.jdlstudios.equationtrainer.ui.navigation.ExercisesEasy
import com.jdlstudios.equationtrainer.ui.navigation.Home
import com.jdlstudios.equationtrainer.ui.navigation.SessionsHistory
import com.jdlstudios.equationtrainer.ui.navigation.appTabRowScreens
import com.jdlstudios.equationtrainer.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-8897050281816485/9904616074", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                mInterstitialAd?.show(this@MainActivity)
            }
        })
        setContent {
            EquationApp(this)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquationApp(context: Context) {
    AppTheme {
        val sessionViewModel: SessionViewModel = viewModel()
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = appTabRowScreens.find {
            it.route == currentDestination?.route
        } ?: Home
        Scaffold(
            topBar = {
                if (currentScreen.route != ExercisesEasy.route) {
                    AppTabRow(
                        allScreens = appTabRowScreens,
                        onTabSelected = { newScreen ->
                            navController
                                .navigateSingleTopTo(newScreen.route)
                        },
                        currentScreen = currentScreen
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = Home.route) {
                    Home(
                        navHostController = navController
                    )
                }
                composable(route = ConfigurationSession.route) {
                    ConfigurationSession(
                        sessionViewModel = sessionViewModel,
                        navHostController = navController
                    )
                }
                composable(route = ExercisesEasy.route) {
                    ExerciseEasy(
                        sessionViewModel = sessionViewModel,
                        navHostController = navController,
                        context = context
                    )
                }
                composable(route = EquationsHistory.route) {
                    EquationsHistoryScreen(
                        sessionViewModel = sessionViewModel
                    )
                }
                composable(route = SessionsHistory.route) {
                    SessionHistoryScreen(
                        sessionViewModel = sessionViewModel
                    )
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }
