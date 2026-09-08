package com.example.habittracker.model


enum class Frequency {
    DAILY,
    WEEKLY
}

data class Habit(
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val icon: String = "📌",
    val frequency: Frequency = Frequency.DAILY,
    val goal: Int = 7,
    val progress: Int = 0
)