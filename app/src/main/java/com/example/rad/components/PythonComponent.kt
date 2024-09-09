package com.example.rad.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.highlighting.SyntaxMain
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType

@Composable
fun PythonComponent(
    viewModel: AlgorithmViewModel,
    onClicked: (String) -> Unit,
    inputArrayFix: String,
    isEditable: Boolean,
    visibleButton: Boolean
) {
    val userCode by viewModel.algorithmCode
    var inputArray by remember { mutableStateOf(inputArrayFix) }

    val language = CodeLang.Python
    val parser = remember { PrettifyParser() }
    val themeState by remember { mutableStateOf(CodeThemeType.Monokai) }
    val theme = remember(themeState) { themeState.theme() }

    var textFieldValue by remember { mutableStateOf(TextFieldValue(userCode)) }

    LaunchedEffect(userCode) {
        textFieldValue = textFieldValue.copy(text = userCode)
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SyntaxMain(
            textFieldValue = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                viewModel.updateAlgorithmCode(newValue.text)
            },
            language = language,
            parser = parser,
            theme = theme,
            readOnly = !isEditable
        )

        if (visibleButton) {
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val inputArrayParsed = parseInputArray(inputArray)
                    val userCode = textFieldValue.text
                    viewModel.checkPythonCode(userCode, inputArrayParsed) { resultText ->
                        onClicked(resultText)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isEditable) "Run" else "Delete")
            }
        }
    }
}

private fun parseInputArray(inputArrayStr: String): IntArray {
    return inputArrayStr.split(",").map { it.trim().toInt() }.toIntArray()
}
