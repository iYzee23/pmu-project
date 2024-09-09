package com.example.rad.similarity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import com.example.rad.complexity.SectionTitle

@Composable
fun ComparisonModal(
    comparisons: List<AlgorithmComparison>,
    improvements: ImprovementSuggestions?,
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
                    text = "Algorithm Comparisons",
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

                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                comparisons.forEachIndexed { index, comparison ->
                    SectionTitle(title = comparison.algorithmName)

                    Spacer(modifier = Modifier.padding(bottom = 8.dp))

                    ComparisonText(
                        label = "Efficiency (Time):",
                        content = comparison.efficiencyTimeComparison
                    )
                    ComparisonText(
                        label = "Efficiency (Space):",
                        content = comparison.efficiencySpaceComparison
                    )
                    ComparisonText(
                        label = "Implementation Comparison:",
                        content = comparison.implementationComparison
                    )
                    ComparisonText(
                        label = "Use Cases Comparison:",
                        content = comparison.useCasesComparison
                    )
                    ComparisonText(
                        label = "Conclusion:",
                        content = comparison.conclusion
                    )

                    if (index < comparisons.size - 1) {
                        Divider(
                            color = Color.Gray,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                }

                improvements?.let {
                    Divider(
                        color = Color.Gray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    SectionTitle(title = "Improvement Suggestions")

                    it.suggestions.forEach { suggestion ->
                        Text(
                            text = suggestion,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                .padding(8.dp)
                        )
                    }

                    Text(
                        text = "Conclusion",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold,
                            shadow = Shadow(
                                color = Color.Gray,
                                offset = Offset(1f, 1f),
                                blurRadius = 3f
                            )
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                    )

                    Text(
                        text = it.conclusion,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(8.dp)
                    )
                }

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
