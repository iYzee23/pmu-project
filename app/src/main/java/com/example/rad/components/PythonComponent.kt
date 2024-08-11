package com.example.rad.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.example.rad.algorithm.AlgorithmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PythonComponent(
    viewModel: AlgorithmViewModel
) {
    val userCodeStatic = """
    def sort(arr):
        steps = []
        for i in range(1, len(arr)):
            j = i - 1
            key = arr[i]
            while j >= 0 and key < arr[j]:
                arr[j+1] = arr[j]
                steps.append(arr[:])
                j -= 1
            arr[j+1] = key
            steps.append(arr[:])
        return steps
    """.trimIndent()
    val inputArrayStatic = """
    50, 42, 165, 400, 244, 126, 54, 98, 54, 65, 2, 5, 509, 52, 76, 
    533, 46, 659, 438, 42, 534, 128, 87, 162, 25, 428, 38, 9, 26
    """.trimIndent()

    var userCode by remember { mutableStateOf(userCodeStatic) }
    var inputArray by remember { mutableStateOf(inputArrayStatic) }
    var result by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = userCode,
            onValueChange = { userCode = it },
            label = { Text("Enter your Python code") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = inputArray,
            onValueChange = { inputArray = it },
            label = { Text("Enter input array (comma-separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val inputArrayParsed = parseInputArray(inputArray)
                viewModel.runPythonCode(userCode, viewModel.arr.value.clone()) { resultText ->
                    result = resultText
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Run")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Result: $result",
            modifier = Modifier.padding(16.dp)
        )
    }
}

private fun parseInputArray(inputArrayStr: String): IntArray {
    return inputArrayStr.split(",").map { it.trim().toInt() }.toIntArray()
}