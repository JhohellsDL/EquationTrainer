package com.jdlstudios.equationtrainer

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EquationApp()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EquationApp() {
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
                        navHostController = navController
                    )
                }
                composable(route = EquationsHistory.route) {
                    EquationsHistoryScreen(
                        sessionViewModel = sessionViewModel,
                        navHostController = navController
                    )
                }
                composable(route = SessionsHistory.route) {
                    SessionHistoryScreen(
                        sessionViewModel = sessionViewModel,
                        navHostController = navController
                    )
                }
            }
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }
