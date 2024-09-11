package com.example.rad.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.rad.R
import com.example.rad.algorithm.AlgorithmViewModel

@Composable
fun VisBottomBar(
    modifier: Modifier = Modifier,
    playPauseClick: () -> Unit,
    speedUpClick: () -> Unit,
    slowDownClick: () -> Unit,
    nextClick: () -> Unit,
    previousClick: () -> Unit,
    isPlaying: Boolean = false,
    viewModel: AlgorithmViewModel
) {
    val isReady by viewModel.isAlgorithmLoaded

    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = playPauseClick, enabled = isReady) {
                Icon(
                    painter = painterResource(id = if (!isPlaying) R.drawable.ic_play else R.drawable.ic_pause),
                    contentDescription = "Play or pause",
                    tint = if (isReady) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            IconButton(onClick = slowDownClick, enabled = isReady) {
                Icon(
                    imageVector = Icons.Default.FastRewind, // ⏪
                    contentDescription = "Slow down",
                    tint = if (isReady) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            IconButton(onClick = speedUpClick, enabled = isReady) {
                Icon(
                    imageVector = Icons.Default.FastForward, // ⏩
                    contentDescription = "Speed up",
                    tint = if (isReady) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            IconButton(onClick = previousClick, enabled = isReady) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous step",
                    tint = if (isReady) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            IconButton(onClick = nextClick, enabled = isReady) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next step",
                    tint = if (isReady) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
        }
    }
}
