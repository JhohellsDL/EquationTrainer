package com.jdlstudios.equationtrainer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

interface AppDestination {
    val icon: ImageVector
    val route: String
}

object Home : AppDestination {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object ConfigurationSession : AppDestination {
    override val icon = Icons.Filled.FitnessCenter
    override val route = "configurationSession"
}

object ExercisesEasy : AppDestination {
    override val icon = Icons.Filled.AccessTime
    override val route = "exercisesEasy"
}

// Screens to be displayed in the top RallyTabRow
val appTabRowScreens = listOf(Home, ConfigurationSession, ExercisesEasy)