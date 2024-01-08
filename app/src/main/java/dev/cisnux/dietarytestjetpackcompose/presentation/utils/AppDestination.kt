package dev.cisnux.dietarytestjetpackcompose.presentation.utils

import androidx.compose.runtime.Immutable

@Immutable
sealed class AppDestination(val route: String) {
    data object HomeRoute : AppDestination(route = "home")
    data object ReportRoute : AppDestination(route = "report")
    data object MyProfileRoute : AppDestination(route = "my_profile")
    data object FoodScannerRoute : AppDestination(route = "food_scanner")
    data object ScannerResultRoute : AppDestination(route = "scanner_result?foodPicture={foodPicture}")
}