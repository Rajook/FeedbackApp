package com.digilyzr.feedback.domain

import javax.inject.Inject

class FeedbackRepository @Inject constructor(private val apiService: FeedbackApiService) {

    suspend fun fetchQuestions(): List<Question> {
//        return apiService.getQuestions()

        val ques = ArrayList<Question>()
        val optionsList = ArrayList<String>()
        optionsList.add("Answer of question 1")
        optionsList.add("Answer of question 2")
        optionsList.add("Answer of question 3")
        optionsList.add("Answer of question 4")
        ques.add(Question(0,"Question no 1?",OptionType.BUTTON,optionsList))
        ques.add(Question(1,"Question no 2?",OptionType.RADIO,optionsList))
        ques.add(Question(2,"Question no 3?",OptionType.CHECKBOX,optionsList))
        ques.add(Question(3,"Question no 4?",OptionType.RADIO,optionsList))
        ques.add(Question(4,"Question no 5?",OptionType.BUTTON,optionsList))
        ques.add(Question(5,"Question no 6?",OptionType.RADIO,optionsList))
        ques.add(Question(6,"Question no 7?",OptionType.BUTTON,optionsList))

        return ques
    }

    suspend fun submitAnswers(answers: List<Answer>) {
//        apiService.submitAnswers(answers)
    }
}