package ru.vk.cup.ui.view

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem.Gaps
import ru.vk.cup.data.InteractiveItemRepository.Companion.generateGapsWithAnswers
import ru.vk.cup.ui.theme.pollSelectedIcon
import ru.vk.cup.ui.utils.vibrateStrong

@Composable
@Preview
fun GapsView(
    modifier: Modifier = Modifier,
    item: Gaps = generateGapsWithAnswers(1),
    count: Int = 1,
) {
    Column(modifier) {
        InteractiveItemTitleView(
            stringResource(if (item.answers != null) R.string.gap_with_answers_title else R.string.gap_question_title),
            stringResource(R.string.poll_question_number, item.index, count)
        )
        FlowRow {
            item.text.forEach { gapItem ->
                when (gapItem.type) {
                    Gaps.GapTextType.DEFAULT -> Text(text = gapItem.text, fontSize = 18.sp)
                    Gaps.GapTextType.ANSWER -> {
                        DropTarget<Gaps.GapItem> { isInBound, data ->
                            val view = LocalView.current
                            var text by rememberSaveable { mutableStateOf("") }
                            if (data != null) {
                                text = data.text
                            }
                            if (isInBound) {
                                view.vibrateStrong()
                            }
                            GapField(gapItem, text, isInBound, onValueChange = {
                                text = it.trim()
                            })
                        }
                    }

                    Gaps.GapTextType.TEXT_FIELD -> {
                        var text by rememberSaveable { mutableStateOf("") }
                        GapField(gapItem, text) {
                            text = it.trim()
                        }
                    }
                }
            }
        }
        if (item.answers != null) {
            LazyRow(modifier = Modifier.padding(top = 12.dp)) {
                items(item.answers) { gapItem ->
                    DragTarget(dataToDrop = gapItem) {
                        Text(
                            text = gapItem.text,
                            modifier = Modifier
                                .padding(horizontal = 1.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                            /*.pointerInput(Unit) {
                                detectTapGestures(
                                    onDoubleTap = {
                                        val emptyGap = item.text.find { it.currentText != null }
                                        if (emptyGap != null) {
                                            item.text[item.text.indexOf(emptyGap)] = emptyGap.copy(currentText = gapItem.text)
                                        }
                                    }
                                )
                            }*/,
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 18.sp,
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
private fun GapField(
    gapItem: Gaps.GapItem,
    text: String,
    isInBound: Boolean = false,
    onValueChange: (String) -> Unit
) {
    val boundColor = Color.Cyan
    BasicTextField(
        value = text,
        enabled = gapItem.type == Gaps.GapTextType.TEXT_FIELD,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center, fontSize = 18.sp),
        modifier = Modifier
            .width(IntrinsicSize.Min)
            .padding(horizontal = 4.dp)
            .widthIn(min = 40.dp)
            .indicatorLine(
                true,
                text.isNotEmpty() && text != gapItem.text,
                remember { MutableInteractionSource() },
                TextFieldDefaults.textFieldColors(
                    errorIndicatorColor = if (isInBound) boundColor else MaterialTheme.colors.error,
                    unfocusedIndicatorColor = if (isInBound) {
                        boundColor
                    } else if (text == gapItem.text) {
                        pollSelectedIcon
                    } else MaterialTheme.colors.onSurface.copy(
                        alpha = TextFieldDefaults.UnfocusedIndicatorLineOpacity
                    )
                ),
                focusedIndicatorLineThickness = 3.dp,
                unfocusedIndicatorLineThickness = 2.dp,
            )
    )
}

@Composable
fun MeasureUnconstrainedViewWidth(
    viewToMeasure: @Composable () -> Unit,
    content: @Composable (measuredWidth: Dp) -> Unit,
) {
    SubcomposeLayout { constraints ->
        val measuredWidth = subcompose("viewToMeasure", viewToMeasure)[0]
            .measure(Constraints()).width.toDp()
        val contentPlaceable = subcompose("content") {
            content(measuredWidth)
        }[0].measure(constraints)
        layout(contentPlaceable.width, contentPlaceable.height) {
            contentPlaceable.place(0, 0)
        }
    }
}