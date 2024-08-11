package com.example.rad.complexity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AlgorithmModal(
    info: AlgorithmInfo,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Algorithm Intro",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.primary,
                        shadow = Shadow(
                            color = Color.Gray,
                            offset = Offset(2f, 2f),
                            blurRadius = 4f
                        )
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = info.intro,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray // Slightly gray text for contrast
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SectionTitle("Time Complexity")
                Text(
                    text = info.timeComplexity,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SectionTitle("Space Complexity")
                Text(
                    text = info.spaceComplexity,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SectionTitle("Insights")
                Text(
                    text = info.insights,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                SectionTitle("Conclusion")
                Text(
                    text = info.conclusion,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.DarkGray
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Close")
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Bold,
            shadow = Shadow(
                color = Color.Gray,
                offset = Offset(1f, 1f),
                blurRadius = 3f
            )
        ),
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}
