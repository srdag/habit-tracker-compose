package com.example.habittracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.habittracker.R
import com.example.habittracker.model.Habit
import com.example.habittracker.viewmodel.HabitViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    viewModel: HabitViewModel,
    onAddHabit: () -> Unit,
    onHabitClick: (Habit) -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Мои привычки") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddHabit) {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = null,
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.habits) { habit ->
                HabitItem(habit = habit, modifier = Modifier.clickable { onHabitClick(habit) })
            }
        }
    }
}

@Composable
fun HabitItem(habit: Habit, modifier: Modifier) {
    Card(modifier) {
        Row() {

            Column() {
                Text("${habit.icon} ${habit.title}")
                Text("Прогресс: ${habit.progress} / ${habit.goal}")
                Text(habit.frequency.name)
            }

        }
    }
}

