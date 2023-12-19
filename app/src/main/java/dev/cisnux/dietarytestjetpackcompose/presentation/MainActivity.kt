package dev.cisnux.dietarytestjetpackcompose.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.dietarytestjetpackcompose.presentation.navigation.DietaryNavGraph
import dev.cisnux.dietarytestjetpackcompose.presentation.ui.theme.DietaryTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DietaryTheme {
                DietaryNavGraph()
            }
        }
    }
}