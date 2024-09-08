package com.example.rad.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmEvents
import com.example.rad.algorithm.AlgorithmViewModel

@Composable
fun VisComponent(
    viewModel: AlgorithmViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column {
            VisMain(
                arr = viewModel.arr.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            val isPlaying = viewModel.isPlaying.value
            val isFinished = viewModel.onSortingFinish.value

            VisBottomBar(
                playPauseClick = { viewModel.onEvent(AlgorithmEvents.PlayPause) },
                speedUpClick = { viewModel.onEvent(AlgorithmEvents.SpeedUp) },
                slowDownClick = { viewModel.onEvent(AlgorithmEvents.SlowDown) },
                nextClick = { viewModel.onEvent(AlgorithmEvents.Next) },
                previousClick = { viewModel.onEvent(AlgorithmEvents.Previous) },
                isPlaying = if (isFinished) !isFinished else isPlaying,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
            )
        }
    }
}