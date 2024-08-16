package com.digilyzr.feedback.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digilyzr.feedback.domain.Answer
import com.digilyzr.feedback.domain.FeedbackRepository
import com.digilyzr.feedback.domain.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val repository: FeedbackRepository
) : ViewModel() {

    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    private val selectedAnswers = mutableListOf<Answer>()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            questions = repository.fetchQuestions()
        }
    }

    fun selectAnswer(option: String) {
        val currentQuestion = questions.getOrNull(currentQuestionIndex)
        currentQuestion?.let {
            selectedAnswers.add(Answer(it.id, option))
            if (currentQuestionIndex < questions.size - 1) {
                currentQuestionIndex++
            } else {
                submitAnswers()
            }
        }
    }

    fun clear(){
        currentQuestionIndex=0
    }

    private fun submitAnswers() {
        viewModelScope.launch {
            repository.submitAnswers(selectedAnswers)
            // Handle submission success or failure
        }
    }
}