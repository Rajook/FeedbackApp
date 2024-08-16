package com.digilyzr.feedback.domain

data class Question(
    val id: Int,
    val text: String,
    val type: OptionType,
    val options: List<String>
)

enum class OptionType {
    BUTTON, CHECKBOX, RADIO
}