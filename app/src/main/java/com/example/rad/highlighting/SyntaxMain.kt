package com.example.rad.highlighting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    // Shared ScrollState to synchronize vertical scrolling
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .background(Color.DarkGray)
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Line numbers column, vertically scrollable
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(start = 8.dp, end = 8.dp)
        ) {
            Column {
                lineTops.forEachIndexed { index, _ ->
                    Text(
                        text = (index + 1).toString(),
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }

        // The code field, vertically scrollable with the same ScrollState
        BasicTextField(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .verticalScroll(scrollState) // Sync vertical scroll with line numbers
                .onPreviewKeyEvent { event ->
                    // Handle Enter Key: Indentation logic
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
                    } else if (event.key == Key.Backspace && event.type == KeyEventType.KeyDown) {
                        // Handle backspace for blank lines or joining lines
                        val cursorPosition = textFieldValue.selection.start
                        if (cursorPosition > 0) {
                            val textBeforeCursor = textFieldValue.text.substring(0, cursorPosition)
                            val currentLine = textBeforeCursor.substringAfterLast('\n')

                            // Check if the current line is empty (just spaces/tabs) and cursor is at the start
                            if (currentLine.isBlank()) {
                                val currentLineStartPos = textBeforeCursor.lastIndexOf('\n') + 1

                                // If the cursor is at the beginning of the line
                                if (cursorPosition == currentLineStartPos) {
                                    // Handle backspacing into the previous line
                                    val previousLineEnd = textBeforeCursor.lastIndexOf('\n')  // End of the previous line
                                    val previousLineStart = textBeforeCursor.lastIndexOf('\n', previousLineEnd - 1)

                                    // Calculate new cursor position after removing the newline character
                                    val newCursorPos = (previousLineEnd).coerceAtLeast(0)
                                    val newText = buildString {
                                        append(textFieldValue.text.substring(0, previousLineEnd))
                                        append(textFieldValue.text.substring(cursorPosition))
                                    }

                                    // Update the text and set the new cursor position at the end of the previous line
                                    onValueChange(
                                        textFieldValue.copy(
                                            text = newText,
                                            selection = TextRange(newCursorPos),
                                        )
                                    )
                                    true
                                } else {
                                    // Handle removing spaces (default 4 spaces back)
                                    val newCursorPos = (cursorPosition - 4).coerceAtLeast(currentLineStartPos)
                                    val newText = buildString {
                                        append(textFieldValue.text.substring(0, newCursorPos))
                                        append(textFieldValue.text.substring(cursorPosition))
                                    }

                                    // Apply changes: move cursor back by 4 spaces or stop at newline
                                    onValueChange(
                                        textFieldValue.copy(
                                            text = newText,
                                            selection = TextRange(newCursorPos),
                                        )
                                    )
                                    true
                                }
                            } else {
                                false
                            }
                        } else {
                            false
                        }
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
