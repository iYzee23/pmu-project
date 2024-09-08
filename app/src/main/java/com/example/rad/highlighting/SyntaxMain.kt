package com.example.rad.highlighting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakaztahir.codeeditor.highlight.model.CodeLang
import com.wakaztahir.codeeditor.highlight.prettify.PrettifyParser
import com.wakaztahir.codeeditor.highlight.theme.CodeTheme
import com.wakaztahir.codeeditor.highlight.utils.parseCodeAsAnnotatedString

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SyntaxMain(
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    language: CodeLang,
    parser: PrettifyParser,
    theme: CodeTheme,
    readOnly: Boolean
) {
    var lineTops by remember { mutableStateOf(emptyArray<Float>()) }
    val density = LocalDensity.current

    // Parse and highlight the code on every text change
    val parsedAnnotatedString = remember(textFieldValue.text) {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = textFieldValue.text
        )
    }

    Row(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxWidth()
            .height(300.dp)
    ) {
        if (lineTops.isNotEmpty()) {
            Box(modifier = Modifier.padding(horizontal = 4.dp)) {
                lineTops.forEachIndexed { index, top ->
                    Text(
                        modifier = Modifier.offset(y = with(density) { top.toDp() }),
                        text = (index + 1).toString(),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .onPreviewKeyEvent { event ->
                    if (event.key == Key.Enter && event.type == KeyEventType.KeyDown) {
                        val cursorPosition = textFieldValue.selection.start
                        val textBeforeCursor = textFieldValue.text.substring(0, cursorPosition)
                        val currentLine = textBeforeCursor.substringAfterLast('\n')
                        val indentation = currentLine.takeWhile { it == ' ' || it == '\t' }
                        val extraIndentation =
                            if (currentLine.trimEnd().endsWith(":")) "    "
                            else ""
                        val newText = buildString {
                            append(textBeforeCursor)
                            append("\n")
                            append(indentation)
                            append(extraIndentation)
                            append(textFieldValue.text.substring(cursorPosition))
                        }
                        onValueChange(
                            textFieldValue.copy(
                                text = newText,
                                selection = TextRange(cursorPosition + 1 + indentation.length + extraIndentation.length),
                            )
                        )
                        true
                    } else {
                        false
                    }
                },
            value = textFieldValue.copy(annotatedString = parsedAnnotatedString),
            onValueChange = { newValue ->
                onValueChange(newValue)
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            ),
            cursorBrush = SolidColor(Color.White),
            onTextLayout = { result ->
                lineTops = Array(result.lineCount) { result.getLineTop(it) }
            },
            readOnly = readOnly
        )
    }
}
