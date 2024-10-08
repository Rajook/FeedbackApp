package com.digilyzr.feedback.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import com.digilyzr.feedback.domain.OptionType
import com.digilyzr.feedback.domain.Question
import com.digilyzr.feedback.presentation.FeedbackViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun FeedbackNavHost(navController: NavHostController, viewModel: FeedbackViewModel = hiltViewModel()) {
    NavHost(navController = navController, startDestination = "questions") {
        composable("questions") {
            FeedbackPager(viewModel, navController)
        }
        composable("submission") {
            SubmissionScreen(viewModel,navController)
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FeedbackPager(viewModel: FeedbackViewModel, navController: NavHostController) {
    val pagerState = rememberPagerState()
    val questions = viewModel.questions

    if (questions.isNotEmpty()) {
        HorizontalPager(count = questions.size, state = pagerState) { page ->
            QuestionPage(
                question = questions[page],
                viewModel = viewModel,
                pagerState = pagerState,
                navController = navController,
                currentPage = page,
                lastPage = questions.size - 1
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "No questions available")
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QuestionPage(
    question: Question,
    viewModel: FeedbackViewModel,
    pagerState: PagerState,
    navController: NavHostController,
    currentPage: Int,
    lastPage: Int
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Define the URL of the background image
    val imageUrl = "https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0"

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        // Content over the background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = question.text, style = MaterialTheme.typography.bodyLarge, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            when (question.type) {
                OptionType.BUTTON -> OptionButtons(question.options) { selected ->
                    selectedOption = selected
                }
                OptionType.CHECKBOX -> OptionCheckboxes(question.options) { selected ->
                    selectedOption = selected
                }
                OptionType.RADIO -> OptionRadioButtons(question.options) { selected ->
                    selectedOption = selected
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (currentPage > 0) {
                            coroutineScope.launch {
                                pagerState.scrollToPage(currentPage - 1)
                            }
                        }
                    },
                    enabled = currentPage > 0
                ) {
                    Text(text = "Previous")
                }
                Button(
                    onClick = {
                        if (selectedOption != null) {
                            viewModel.selectAnswer(selectedOption!!)
                            if (currentPage < lastPage) {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(currentPage + 1)
                                }
                            } else {
                                navController.navigate("submission")
                            }
                        }
                    },
                    enabled = selectedOption != null
                ) {
                    Text(text = "Next")
                }
            }
        }
    }
}

@Composable
fun SubmissionScreen(viewModel: FeedbackViewModel, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Thank you for your feedback!")
        Button(onClick = {
            viewModel.clear()
            navController.navigate("questions") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
                         }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Close")
        }
        // Optionally show a summary of responses
    }
}

@Composable
fun OptionButtons(options: List<String>, onOptionSelected: (String) -> Unit) {
    options.forEach { option ->
        Button(onClick = { onOptionSelected(option) }, modifier = Modifier.fillMaxWidth()) {
            Text(text = option)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun OptionCheckboxes(options: List<String>, onOptionSelected: (String) -> Unit) {
    options.forEach { option ->
        var checkedState by remember { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxWidth()) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = {
                    checkedState = it
                    if (checkedState) onOptionSelected(option)
                }
            )
            Text(text = option, modifier = Modifier.padding(start = 8.dp), color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun OptionRadioButtons(options: List<String>, onOptionSelected: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    options.forEach { option ->
        Row(modifier = Modifier.fillMaxWidth()) {
            RadioButton(
                selected = (selectedOption == option),
                onClick = {
                    selectedOption = option
                    onOptionSelected(option)
                }
            )
            Text(text = option, modifier = Modifier.padding(start = 8.dp), color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeedbackScreen() {
    // Implement a preview for FeedbackPager or QuestionPage
}
