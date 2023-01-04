package ru.vk.cup.ui.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.gesture.MotionEvent
import com.smarttoolfactory.gesture.pointerMotionEvents
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem

@Composable
@Preview
fun ColumnView(
    modifier: Modifier = Modifier,
    item: InteractiveItem.Column = InteractiveItem.Column(
        1,
        pairs = (0 until 5).associate {
            InteractiveItem.Column.ColumnItem("Номер $it") to InteractiveItem.Column.ColumnItem(
                it.toString()
            )
        }.toList().toMutableStateMap(),
        correctPairs = (0 until 5).associate {
            InteractiveItem.Column.ColumnItem("Номер $it") to InteractiveItem.Column.ColumnItem(
                it.toString()
            )
        }.toList().toMutableStateMap(),
    ),
    count: Int = 1,
) = Column(modifier = modifier) {
    InteractiveItemTitleView(
        stringResource(R.string.column_title),
        stringResource(R.string.poll_question_number, item.index, count)
    )
    ColumnWithDrawing(item)
}

@Composable
private fun ColumnWithDrawing(item: InteractiveItem.Column) = Box(modifier = Modifier.padding(top = 8.dp)) {
    var motionEvent by remember { mutableStateOf(MotionEvent.Idle) }
    var currentPosition by remember { mutableStateOf(Offset.Unspecified) }
    var previousPosition by remember { mutableStateOf(Offset.Unspecified) }

    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    var canvasRect by remember { mutableStateOf(Rect(0f, 0f, 0f, 0f)) }
    var path by remember { mutableStateOf(Path()) }
    val answerPaths = remember { mutableListOf<AnswerPath>() }
    var connectedLeft by remember { mutableStateOf<ConnectedData?>(null) }
    var connectedRight by remember { mutableStateOf<ConnectedData?>(null) }

    val screenPixelDensity = LocalDensity.current.density
    val drawModifier = Modifier
        .background(Color.Yellow.copy(alpha = 0.3f))
        .size(Dp(canvasSize.width / screenPixelDensity),Dp( canvasSize.height / screenPixelDensity))
        .pointerMotionEvents(
            onDown = { pointerInputChange: PointerInputChange ->
                currentPosition = pointerInputChange.position
                motionEvent = MotionEvent.Down
                pointerInputChange.consume()
            },
            onMove = { pointerInputChange: PointerInputChange ->
                currentPosition = pointerInputChange.position
                motionEvent = MotionEvent.Move
                pointerInputChange.consume()
            },
            onUp = { pointerInputChange: PointerInputChange ->
                motionEvent = MotionEvent.Up
                pointerInputChange.consume()
            },
            delayAfterDownInMillis = 25L
        )
        .onGloballyPositioned {
            it.boundsInWindow().let { rect ->
                canvasRect = rect
            }
        }
    Column(modifier = Modifier.onGloballyPositioned { coordinates ->
        canvasSize = coordinates.size
    }) {
        val itemModifier = Modifier
            .padding(vertical = 1.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
        val itemStyle = MaterialTheme.typography.body1.copy(
            fontSize = 21.sp,
            platformStyle = PlatformTextStyle(includeFontPadding = false)
        )
        item.pairs.forEach { pair ->
            Row {
                val currentPosition = if (currentPosition != Offset.Unspecified) currentPosition.copy(
                    x = currentPosition.x + canvasRect.left,
                    y = currentPosition.y + canvasRect.top
                ) else currentPosition
                Text(
                    text = pair.key.text,
                    modifier = itemModifier.onGloballyPositioned {
                        it.boundsInWindow().let { rect ->
                            if (currentPosition != Offset.Unspecified
                                && rect.contains(currentPosition)
                            ) {
                                connectedLeft = ConnectedData(pair.key, rect)
                            }
                        }
                    },
                    style = itemStyle
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = pair.value.text,
                    modifier = itemModifier.onGloballyPositioned {
                        it.boundsInWindow().let { rect ->
                            if (currentPosition != Offset.Unspecified
                                && rect.contains(currentPosition)
                            ) {
                                connectedRight = ConnectedData(pair.value, rect)
                            }
                        }
                    },
                    style = itemStyle
                )
            }
        }
    }
    Canvas(modifier = drawModifier) {
        when (motionEvent) {
            MotionEvent.Down -> {
                path.moveTo(currentPosition.x, currentPosition.y)
                previousPosition = currentPosition
            }
            MotionEvent.Move -> {
                path.quadraticBezierTo(
                    previousPosition.x,
                    previousPosition.y,
                    (previousPosition.x + currentPosition.x) / 2,
                    (previousPosition.y + currentPosition.y) / 2
                )
                previousPosition = currentPosition
            }
            MotionEvent.Up -> {
                path.lineTo(currentPosition.x, currentPosition.y)
                currentPosition = Offset.Unspecified
                previousPosition = currentPosition
                motionEvent = MotionEvent.Idle
                connectedLeft?.let { connectedLeft ->
                    connectedRight?.let { connectedRight ->
                        val startPosition = connectedLeft.rect.centerRight
                        val endPosition = connectedRight.rect.centerLeft
                        val answerPath = Path().apply {
                            moveTo(startPosition.x - canvasRect.left, startPosition.y - canvasRect.top)
                            lineTo(endPosition.x - canvasRect.left, endPosition.y - canvasRect.top)
                        }
                        val alreadyAdded = answerPaths.find {
                            it.left == connectedLeft.item || it.right == connectedRight.item
                        }
                        if (alreadyAdded != null) {
                            answerPaths.remove(alreadyAdded)
                        }
                        answerPaths.add(
                            AnswerPath(
                                answerPath,
                                connectedLeft.item,
                                connectedRight.item,
                                connectedRight.item == item.correctPairs[connectedLeft.item]
                            )
                        )
                    }
                }
                connectedLeft = null
                connectedRight = null
                path = Path()
            }

            else -> Unit
        }
        drawPath(
            color = Color.Blue,
            path = path,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
        answerPaths.forEach {
            drawPath(
                color = if (it.isCorrect) Color.Green else Color.Red,
                path = it.path,
                style = Stroke(
                    width = 6.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}

data class ConnectedData(
    val item: InteractiveItem.Column.ColumnItem,
    val rect: Rect,
)

data class AnswerPath(
    val path: Path,
    val left: InteractiveItem.Column.ColumnItem,
    val right: InteractiveItem.Column.ColumnItem,
    val isCorrect: Boolean
)