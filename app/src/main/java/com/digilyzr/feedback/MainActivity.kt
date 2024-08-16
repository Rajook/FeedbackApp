package com.digilyzr.feedback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.digilyzr.feedback.presentation.ui.FeedbackNavHost
import com.digilyzr.feedback.ui.theme.FeedbackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FeedbackNavHost(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackAppPreview() {
    FeedbackTheme {

    }
}