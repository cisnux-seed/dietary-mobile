package dev.cisnux.dietarytestjetpackcompose.presentation.scannerresult

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.cisnux.dietarytestjetpackcompose.presentation.ui.theme.DietaryTheme

@Composable
fun ScannerResultScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ScannerResultViewModel = hiltViewModel(),
) {
    val isBackCamera by viewModel.isBackCamera.collectAsState()
    val foodPicture by viewModel.foodPicture.collectAsState()

    isBackCamera?.let {
//        viewModel.rotateFile(foodPicture, it)
    }

    ScannerResultContent(
        body = {
            ScannerResultBody(
                onNavigateUp = onNavigateUp,
                foodPicture = foodPicture,
                modifier = modifier.padding(it)
            )
        },
    )
}

@Composable
@Preview(showBackground = true)
private fun ScannerResultContentPreview() {
    DietaryTheme {
        ScannerResultContent(
            body = {
                ScannerResultBody(onNavigateUp = { /*TODO*/ }, foodPicture = "")
            },
        )
    }
}

@Composable
private fun ScannerResultContent(
    body: @Composable (innerPadding: PaddingValues) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        body(innerPadding)
    }
}

@Composable
private fun ScannerResultBody(
    onNavigateUp: () -> Unit,
    foodPicture: String,
    modifier: Modifier = Modifier,
) {
    Log.d("ScannerResultBody", foodPicture)
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(foodPicture).build(),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = onNavigateUp,
            content = {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = "back"
                )
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.54f),
                    shape = CircleShape,
                )
        )
    }
}
