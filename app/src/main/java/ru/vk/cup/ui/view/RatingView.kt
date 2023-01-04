package ru.vk.cup.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem
import ru.vk.cup.ui.theme.orange
import ru.vk.cup.ui.utils.vibrateStrong

@Composable
@Preview
fun RatingView(
    modifier: Modifier = Modifier,
    item: InteractiveItem.Rating = InteractiveItem.Rating(1, 5, selected = 0),
    count: Int = 2,
) = Column(modifier) {
    var selected by remember { mutableStateOf(item.selected) }
    InteractiveItemTitleView(
        stringResource(id = R.string.rating_title),
        stringResource(R.string.poll_question_number, item.index, count)
    )
    LazyRow(modifier = Modifier.fillMaxWidth().wrapContentSize(align = Alignment.Center)) {
        items(item.count) { i ->
            val view = LocalView.current
            IconButton(onClick = {
                selected = if (selected != i) i else -1
                view.vibrateStrong()
            }) {
                Icon(
                    painterResource(
                        id = if (i <= selected) R.drawable.ic_star_24 else R.drawable.ic_star_border_24
                    ),
                    contentDescription = "$i",
                    tint = orange
                )
            }
        }
    }
}