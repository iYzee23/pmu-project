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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun VisMain(
    modifier: Modifier = Modifier,
    arr: IntArray
) {
    BoxWithConstraints(
        modifier = modifier
    ) {
        val maxHeight = maxHeight - 32.dp
        val itemWidth = remember {
            maxWidth / arr.size - 8.dp
        }

        val maxValue = arr.maxOrNull()?.toFloat() ?: 1f

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
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    }
}
