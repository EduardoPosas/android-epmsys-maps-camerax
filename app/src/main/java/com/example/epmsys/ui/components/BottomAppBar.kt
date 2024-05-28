package com.example.epmsys.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun BottomBar(
    isValidEntry: Boolean,
    onPictureClick: () -> Unit = {},
    onSavePointEntry: () -> Unit = {}
) {
    BottomAppBar(
        actions = {
            IconButton(onClick = onPictureClick) {
                Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = null)
            }
        },
        floatingActionButton = {
            FloatingButton(
                isValidEntry = isValidEntry,
                onClick = onSavePointEntry,
            )
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    )
}

@Composable
fun FloatingButton(
    isValidEntry: Boolean,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        enabled = isValidEntry
    ) {
        Icon(imageVector = Icons.Default.AddTask, contentDescription = null)
    }
}
