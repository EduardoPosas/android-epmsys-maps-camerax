package com.example.epmsys.ui.components

import android.widget.DatePicker
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePickerDialog(
    onOpenDialog: () -> Unit,
    initialSelectedDateMillis: Long?,
    onSelectedDate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(
//        initialSelectedDateMillis = initialSelectedDateMillis,
        //initialDisplayMode = DisplayMode.Input
    )
    val confirmEnabled = remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = onOpenDialog,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        val instant = Instant.ofEpochMilli(selectedMillis)
                        val timeZone = ZoneId.of("America/Mexico_City")
                        val zonedDateTime = instant.atZone(timeZone)
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        onOpenDialog()
                        onSelectedDate(zonedDateTime.format(formatter))
                    }

                },
                enabled = confirmEnabled.value
            ) {
                Text(text = "OK")
            }
        },
        modifier = modifier.padding(16.dp),
        dismissButton = {
            TextButton(
                onClick = onOpenDialog,
            ) {
                Text(text = "Cancel")
            }
        },
        //shape = RoundedCornerShape(8.dp)
    ) {
        FormDatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = "Date",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDatePicker(
    state: DatePickerState,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    DatePicker(
        state = state,
        modifier = modifier,
        title = title,
        /*dateFormatter = DatePickerDefaults.dateFormatter(
            selectedDateSkeleton = "yyyy-MM-dd HH:mm:ss"
        )*/
        /*headline = {
            DatePickerDefaults.DatePickerHeadline(
                selectedDateMillis = state.selectedDateMillis,
                displayMode = DisplayMode.Picker,
                dateFormatter = DatePickerDefaults.dateFormatter(
                    selectedDateSkeleton = "MMM d y HH:mm:ss"
                ),
                modifier = Modifier.padding(16.dp)
            )
        },*/
    )
}