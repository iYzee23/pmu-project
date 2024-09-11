package com.example.rad.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.example.rad.algorithm.AlgorithmViewModel

@Composable
fun VisMain(
    modifier: Modifier = Modifier,
    arr: IntArray,
    viewModel: AlgorithmViewModel
) {
    val isReady by viewModel.isAlgorithmLoaded

    BoxWithConstraints(
        modifier = modifier
    ) {
        val maxHeight = maxHeight - 32.dp
        val itemWidth = remember {
            val currSize = if (arr.size > 20) arr.size else 20
            maxWidth / currSize - 8.dp
        }

        val maxValue = arr.maxOrNull()?.toFloat() ?: 1f
        val barColor = if (isReady) MaterialTheme.colorScheme.primary else Color.LightGray

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            arr.forEach {
                val normalizedHeight = (it * 1.0 / maxValue) * maxHeight
                Box(
                    modifier = Modifier
                        .height(normalizedHeight)
                        .width(itemWidth)
                        .background(barColor)
                )
            }
        }
    }
}
