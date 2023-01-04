package ru.vk.cup.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
        poll.items.forEach {
            val view = LocalView.current
            Row(
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(
                        color = if (it.isSelected) pollSelectedColor else pollDefaultColor,
                        shape = RoundedCornerShape(4.dp),
                    )
                    .clickable {
                        val index = poll.items.indexOf(it)
                        for (i in poll.items.indices) {
                            poll.items[i] = poll.items[i].copy(isSelected = i == index)
                        }
                        view.vibrate()
                    }
                    .padding(10.dp)
            ) {
                val fontWeight = if (it.isSelected) FontWeight.Bold else FontWeight.Normal
                Text(
                    text = it.text,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    fontWeight = fontWeight
                )
                Spacer(modifier = Modifier.weight(1f))
                if (it.isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check),
                        contentDescription = "",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        tint = pollSelectedIcon,
                    )
                }
                if (poll.items.any { it.isSelected }) {
                    Text(
                        text = if (it.isSelected) "100%" else "0%",
                        color = Color.Black,
                        fontWeight = fontWeight
                    )
                }
            }
        }
    }
}