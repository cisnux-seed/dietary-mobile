package dev.cisnux.dietarytestjetpackcompose.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import dev.cisnux.dietarytestjetpackcompose.R
import dev.cisnux.dietarytestjetpackcompose.presentation.ui.components.BottomBar
import dev.cisnux.dietarytestjetpackcompose.presentation.ui.theme.DietaryTheme
import dev.cisnux.dietarytestjetpackcompose.presentation.utils.AppDestination
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navigateForBottomNav: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    onFabFoodScanner: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onFabFoodScanner()
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "permission not granted",
                        withDismissAction = true,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }

    HomeContent(
        onSelectedDestination = navigateForBottomNav,
        body = { HomeBody(modifier = modifier.padding(it)) },
        shouldBottomBarOpen = true,
        snackbarHostState = snackbarHostState,
        onFabFoodScanner = {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                onFabFoodScanner()
            } else {
                cameraLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeContentPreview() {
    DietaryTheme {
        HomeContent(
            onSelectedDestination = { _, _ -> },
            body = { HomeBody(modifier = Modifier.padding(it)) },
            shouldBottomBarOpen = true,
            snackbarHostState = SnackbarHostState(),
            onFabFoodScanner = {}
        )
    }
}

@Composable
private fun HomeContent(
    onSelectedDestination: (destination: AppDestination, currentRoute: AppDestination) -> Unit,
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    shouldBottomBarOpen: Boolean,
    snackbarHostState: SnackbarHostState,
    onFabFoodScanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = shouldBottomBarOpen) {
                BottomBar(
                    currentRoute = AppDestination.HomeRoute,
                    onSelectedDestination = onSelectedDestination
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onFabFoodScanner) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_food_scanner_24dp),
                    contentDescription = stringResource(R.string.food_scanner_title)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
private fun HomeBody(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(id = R.string.home_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}
