package com.example.rad.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.rad.algorithm.AlgorithmViewModel
import com.example.rad.database.Algorithm
import com.example.rad.highlighting.SyntaxMain
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeThemeType
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PythonComponent(
    viewModel: AlgorithmViewModel,
    onClicked: (String) -> Unit,
    inputArrayFix: String,
    isEditable: Boolean
) {
    val userCode by viewModel.algorithmCode // Observe algorithmCode from ViewModel
    var inputArray by remember { mutableStateOf(inputArrayFix) }

    val language = CodeLang.Python
    val parser = remember { PrettifyParser() }
    val themeState by remember { mutableStateOf(CodeThemeType.Monokai) }
    val theme = remember(themeState) { themeState.theme() }

    // Proper initialization of the text field
    var textFieldValue by remember { mutableStateOf(TextFieldValue(userCode)) }

    // Ensure textFieldValue is updated correctly when userCode changes
    LaunchedEffect(userCode) {
        textFieldValue = textFieldValue.copy(text = userCode) // Keep the cursor position
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        SyntaxMain(
            textFieldValue = textFieldValue,
            onValueChange = { newValue ->
                // Update the text value while keeping the cursor's position
                textFieldValue = newValue
                viewModel.updateAlgorithmCode(newValue.text) // Update ViewModel's algorithmCode
            },
            language = language,
            parser = parser,
            theme = theme,
            readOnly = !isEditable
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Run Button
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

private fun parseInputArray(inputArrayStr: String): IntArray {
    return inputArrayStr.split(",").map { it.trim().toInt() }.toIntArray()
}
