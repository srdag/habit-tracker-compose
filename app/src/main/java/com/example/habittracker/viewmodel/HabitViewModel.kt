package com.example.habittracker.viewmodel

import androidx.compose.runtime.mutableStateListOf


import androidx.lifecycle.ViewModel
import com.example.habittracker.model.Habit

class HabitViewModel : ViewModel() {

    private val _habits = mutableStateListOf<Habit>()
    val habits: List<Habit> = _habits

    private var nextId = 1

    fun addHabit(habit: Habit) {
        _habits.add(habit.copy(id = nextId++))
    }

    fun updateHabit(habit: Habit) {
        val index = _habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            _habits[index] = habit
        }
    }

    fun deleteHabit(habit: Habit) {
        _habits.remove(habit)
    }

    fun incrementProgress(habit: Habit) {
        val index = _habits.indexOfFirst { it.id == habit.id }
        if (index != -1) {
            val current = _habits[index]
            if (current.progress < current.goal) {
                _habits[index] = current.copy(progress = current.progress + 1)
            }
        }
    }

    fun resetAll() {
        _habits.clear()
        nextId = 1
    }
}