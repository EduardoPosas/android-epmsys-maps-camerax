package com.example.epmsys.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit) = {},
    colors: TextFieldColors? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null
) {
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        label = label,
        modifier = modifier,
        singleLine = true,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.bodySmall,
        keyboardOptions = keyboardOptions,
        trailingIcon = trailingIcon,
        colors = colors ?: TextFieldDefaults.colors(),
        enabled = enabled,
        isError = isError,
        supportingText = supportingText
    )
}