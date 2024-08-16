package com.digilyzr.feedback.domain

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FeedbackApiService {
    @GET("questions")
    suspend fun getQuestions(): List<Question>

    @POST("submit-answers")
    suspend fun submitAnswers(@Body answers: List<Answer>)
}