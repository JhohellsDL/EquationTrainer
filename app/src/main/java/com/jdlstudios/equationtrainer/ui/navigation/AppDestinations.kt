package com.jdlstudios.equationtrainer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChecklistRtl
import androidx.compose.material.icons.filled.CollectionsBookmark
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

object EquationsHistory : AppDestination {
    override val icon = Icons.Filled.ChecklistRtl
    override val route = "equationsHistory"
}

object SessionsHistory : AppDestination {
    override val icon = Icons.Filled.CollectionsBookmark
    override val route = "sessionsHistory"
}
// Screens to be displayed in the top RallyTabRow
val appTabRowScreens = listOf(Home, ConfigurationSession, ExercisesEasy, EquationsHistory, SessionsHistory)