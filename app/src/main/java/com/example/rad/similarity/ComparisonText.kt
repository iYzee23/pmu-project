package com.example.rad.similarity

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ComparisonText(
    label: String,
    content: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = Color.Gray,
                offset = Offset(1f, 1f),
                blurRadius = 3f
            )
        ),
        modifier = Modifier.padding(bottom = 4.dp)
    )
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge.copy(
            color = Color.DarkGray
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}