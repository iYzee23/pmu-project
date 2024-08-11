package com.example.rad.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.rad.R

@Composable
fun VisBottomBar(
    modifier: Modifier = Modifier,
    playPauseClick: () -> Unit,
    speedUpClick: () -> Unit,
    slowDownClick: () -> Unit,
    nextClick: () -> Unit,
    previousClick: () -> Unit,
    isPlaying: Boolean = false
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = playPauseClick) {
                Icon(
                    painter = painterResource(id = if (!isPlaying) R.drawable.ic_play else R.drawable.ic_pause),
                    contentDescription = "Play or pause",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = speedUpClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Speed up",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = slowDownClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_horizontal),
                    contentDescription = "Slow down",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = previousClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous step",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = nextClick) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next step",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}