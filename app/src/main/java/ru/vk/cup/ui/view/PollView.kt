package ru.vk.cup.ui.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem
import ru.vk.cup.ui.theme.pollDefaultColor
import ru.vk.cup.ui.theme.pollSelectedColor
import ru.vk.cup.ui.theme.pollSelectedIcon
import ru.vk.cup.ui.utils.vibrate

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun PollView(
    modifier: Modifier = Modifier,
    poll: InteractiveItem.Poll = InteractiveItem.Poll(
        index = 1,
        title = "Вопрос №1",
        items = (1..4).map {
            InteractiveItem.Poll.PollItem("ответ $it", isSelected = it == 1)
        }.toMutableStateList()
    ),
    count: Int = 1,
) = Column(
    modifier = modifier
        .background(Color.White)
) {
    InteractiveItemTitleView(
        poll.title,
        stringResource(R.string.poll_question_number, poll.index, count)
    )
    Column {
        poll.items.forEach { pollItem ->
            val view = LocalView.current
            Row(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = if (pollItem.isSelected) pollSelectedColor else pollDefaultColor,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .combinedClickable(
                        onClick = {
                            val index = poll.items.indexOf(pollItem)
                            for (i in poll.items.indices) {
                                poll.items[i] = poll.items[i].copy(isSelected = i == index)
                            }
                            view.vibrate()
                        },
                        onLongClick = {
                            val index = poll.items.indexOf(pollItem)
                            if (poll.items.indexOfFirst { it.isSelected } == index) {
                                for (i in poll.items.indices) {
                                    poll.items[i] = poll.items[i].copy(isSelected = false)
                                }
                                view.vibrate()
                            }
                        })
                    .padding(10.dp)
            ) {
                val fontWeight = if (pollItem.isSelected) FontWeight.Bold else FontWeight.Normal
                Text(
                    text = pollItem.text,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = fontWeight
                )
                Spacer(modifier = Modifier.weight(1f))
                if (pollItem.isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        tint = pollSelectedIcon,
                    )
                }
                if (poll.items.any { it.isSelected }) {
                    Text(
                        text = if (pollItem.isSelected) "100%" else "0%",
                        color = Color.Black,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}