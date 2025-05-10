package com.example.discordcloneapp.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomTextField(
    value: String,
    hint: String,
    modifier: Modifier = Modifier,
    backColor: Color = MaterialTheme.colors.secondary,
    cornerRadius: Dp = 4.dp,
    isSingleLine: Boolean = true,
    isPasswordField: Boolean = false,
    error: String = "",
    helperText: String = "",
    onValueChange: (String) -> Unit
) {

    Column {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = isSingleLine,
            visualTransformation = if (isPasswordField) PasswordVisualTransformation() else VisualTransformation.None,
            textStyle = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.onSecondary),
            cursorBrush = SolidColor(MaterialTheme.colors.onSecondary.copy(0.7f))
        ) { innerTextField ->

            Box(
                Modifier
                    .background(backColor, RoundedCornerShape(cornerRadius))
                    .border(2.dp, if (error.isEmpty()) Color.Transparent else Color.Red)
                    .clip(RoundedCornerShape(cornerRadius))
                    .padding(16.dp, 12.dp)
                    .then(modifier)
            ) {

                innerTextField()
                if (value.isEmpty()) {
                    Text(
                        hint,
                        color = MaterialTheme.colors.onSecondary.copy(0.5f),
                        modifier = Modifier.align(Alignment.CenterStart),
                        style = MaterialTheme.typography.body2
                    )
                }

            }

        }

        val lowerTextColor = if (error.isEmpty()) {
            MaterialTheme.colors.onBackground
        } else {
            MaterialTheme.colors.error
        }

        val lowerText = error.ifEmpty { helperText }

        AnimatedVisibility(visible = error.isNotEmpty() || helperText.isNotEmpty()) {
            Text(lowerText, color = lowerTextColor, modifier = Modifier.padding(top = 8.dp))
        }

    }

}