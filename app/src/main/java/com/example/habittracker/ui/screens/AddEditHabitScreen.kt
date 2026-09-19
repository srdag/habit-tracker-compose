package com.example.habittracker.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.habittracker.R
import com.example.habittracker.model.Frequency
import com.example.habittracker.model.Habit
import com.example.habittracker.viewmodel.HabitViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditHabitScreen(
    viewModel: HabitViewModel,
    habitId: Int? = null,
    onNavigationBack: () -> Unit,
) {

    var textFieldHeader by remember { mutableStateOf("") }
    var textFieldDescription by remember { mutableStateOf("") }

    val icons = listOf("📌", "💪", "📚", "🏃", "💧", "🧘", "🎯", "✍️")

    var selectedIcon by remember { mutableStateOf("📌") }

    var frequency by remember { mutableStateOf(Frequency.DAILY) }

    var goal by remember { mutableStateOf("7") }

    var titleError by remember { mutableStateOf(false) }

    var goalError by remember { mutableStateOf(false) }

    val existingHabit = habitId?.let { id -> viewModel.habits.firstOrNull { it.id == id } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Новая привычка")},
                navigationIcon = {
                    IconButton(onClick = onNavigationBack) {
                        Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = null)
                    }
                },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(
                value = textFieldHeader,
                onValueChange = {textFieldHeader = it},
                isError = titleError,
                label = { Text("Заголовок") },
                supportingText = {
                    if (titleError) {
                        Text("Данное поле должно быть заполнено")
                    }
                }
            )

            OutlinedTextField(
                value = textFieldDescription,
                onValueChange = {textFieldDescription = it},
                label = { Text("Описание") }
            )

            OutlinedTextField(
                value = goal,
                onValueChange = { goal = it },
                isError = goalError,
                label = { Text("Цель (раз)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (goalError) Text("Введите число больше 0")
                }
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                SegmentedButton(
                    selected = frequency == Frequency.DAILY,
                    onClick = { frequency = Frequency.DAILY },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
                ) { Text("Ежедневно") }

                SegmentedButton(
                    selected = frequency == Frequency.WEEKLY,
                    onClick = { frequency = Frequency.WEEKLY },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
                ) { Text("Еженедельно") }
            }

            LazyRow() {
                items(icons) { icon ->
                    FilterChip(
                        selected = selectedIcon == icon,
                        onClick = {selectedIcon = icon},
                        label = { Text(icon) }
                    )
                }
            }

            Button(
                onClick = {
                    val goalInt = goal.toIntOrNull()
                    val isTitleValid = textFieldHeader.isNotBlank()
                    val isGoalValid = goalInt != null && goalInt > 0

                    titleError = !isTitleValid
                    goalError = !isGoalValid

                    if (isTitleValid && isGoalValid) {
                        if (existingHabit == null) {
                            viewModel.addHabit(
                                Habit(
                                    title = textFieldHeader,
                                    description = textFieldDescription,
                                    icon = selectedIcon,
                                    frequency = frequency,
                                    goal = goalInt
                                )
                            )
                        } else {
                            viewModel.updateHabit(
                                Habit(
                                    id = existingHabit.id,
                                    title = textFieldHeader,
                                    description = textFieldDescription,
                                    icon = selectedIcon,
                                    frequency = frequency,
                                    goal = goalInt
                                )
                            )
                        }

                        onNavigationBack()
                    }
                }
            ) {
                Text("Сохранить")
            }

        }
    }
}