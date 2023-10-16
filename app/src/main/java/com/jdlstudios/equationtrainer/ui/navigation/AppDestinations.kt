package com.jdlstudios.equationtrainer.ui.navigation

import androidx.compose.material.icons.Icons
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

object Configuration : AppDestination {
    override val icon = Icons.Filled.FitnessCenter
    override val route = "configuration"
}

// Screens to be displayed in the top RallyTabRow
val appTabRowScreens = listOf(Home, Configuration)