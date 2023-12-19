package dev.cisnux.dietarytestjetpackcompose.presentation.navigation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.cisnux.dietarytestjetpackcompose.presentation.utils.AppDestination

@Immutable
data class BottomNavigationItem(
    val icon: ImageVector,
    val title: String,
    val destination: AppDestination,
    val contentDescription: String,
)