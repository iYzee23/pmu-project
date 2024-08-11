package com.example.rad.algorithm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AlgorithmViewModel(
    private val algorithm: Algorithm
): ViewModel() {
    var arr = mutableStateOf(
        intArrayOf(
            50, 42, 165, 400, 244, 126, 54, 98, 54, 65, 2, 5, 509, 52, 76,
            533, 46, 659, 438, 42, 534, 128, 87, 162, 25, 428, 38, 9, 26
        )
    )

    val isPlaying = mutableStateOf(false)
    val onSortingFinish = mutableStateOf(false)
    private var delay = 150L
    private var pause = false
    private var sortingState = 0

    private var next = 0
    private var previous = 0

    var sortedArrayLevels = mutableListOf<List<Int>>()


    init {
        viewModelScope.launch {
            algorithm.sort(
                arr.value.clone()
            ) { modifiedArray ->
                sortedArrayLevels.add(modifiedArray.toMutableList())
            }
        }
    }


    fun runPythonCode(userCode: String, inputArray: IntArray, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val py = Python.getInstance()
            val pyf = py.getModule("validator").callAttr("validate_and_execute", userCode, inputArray)

            val resultMap = pyf.asMap().mapKeys { it.key.toString() }

            val result = if (resultMap.containsKey("error")) {
                resultMap["error"].toString()
            } else {
                // sortedArrayLevels = convertPyObjectToListOfLists(resultMap["steps"]!!)
                sortedArrayLevels.clear()
                val steps = resultMap["steps"]!!.asList()
                for (step in steps) {
                    val stepList = step.asList().map { it.toInt() }.toMutableList()
                    sortedArrayLevels.add(stepList)
                }
                resultMap["steps"].toString()
            }

            onResult(result)
        }
    }

    private fun convertPyObjectToListOfLists(pyObject: PyObject): MutableList<List<Int>> {
        val outerList = mutableListOf<List<Int>>()
        for (pyInnerList in pyObject.asList()) {
            val innerList = mutableListOf<Int>()
            for (pyInt in pyInnerList.asList()) {
                innerList.add(pyInt.toInt())
            }
            outerList.add(innerList)
        }
        return outerList
    }

    fun onEvent(event: AlgorithmEvents) {
        when(event) {
            is AlgorithmEvents.PlayPause -> {
                playPause()
            }
            is AlgorithmEvents.SpeedUp -> {
                speedUp()
            }
            is AlgorithmEvents.SlowDown -> {
                slowDown()
            }
            is AlgorithmEvents.Next -> {
                next()
            }
            is AlgorithmEvents.Previous -> {
                previous()
            }
        }
    }

    private fun playPause() {
        if (isPlaying.value) pause()
        else play()
        isPlaying.value = !isPlaying.value
    }

    private fun play() = viewModelScope.launch {
        pause = false
        for (i in sortingState until sortedArrayLevels.size) {
            if (!pause) {
                delay(delay)
                arr.value = sortedArrayLevels[i].toIntArray()
            }
            else {
                sortingState = i
                next = i + 1
                previous = i
                return@launch
            }
        }
        onSortingFinish.value = true
    }

    private fun pause() {
        pause = true
    }

    private fun speedUp() {
        if (delay >= 150L) delay -= 50
    }

    private fun slowDown() {
        delay += 50
    }

    private fun next() {
        if (next < sortedArrayLevels.size) {
            arr.value = sortedArrayLevels[next].toIntArray()
            ++next
            ++previous
        }
    }

    private fun previous() {
        if (previous >= 0) {
            arr.value = sortedArrayLevels[previous].toIntArray()
            --next
            --previous
        }
    }
}