package ru.vk.cup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.flowlayout.SizeMode
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import ru.vk.cup.data.Category
import ru.vk.cup.data.CategoryRepository
import ru.vk.cup.ui.theme.chipContentColor
import ru.vk.cup.ui.theme.colorAccent
import ru.vk.cup.ui.theme.transparentWhite20
import ru.vk.cup.ui.theme.transparentWhite30

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ColumnScope.ChipGroupMultiSelection(
    modifier: Modifier = Modifier,
    categories: List<Category> = CategoryRepository.categories,
    selectedCategories: Collection<Category> = setOf(CategoryRepository.categories.first()),
    onSelectedChanged: (Category) -> Unit = {},
) = Column(
    modifier = Modifier
        .verticalScroll(rememberScrollState())
        .weight(weight = 1f, fill = false)
) {
    FlowRow(
        modifier = Modifier.padding(8.dp),
        mainAxisAlignment = MainAxisAlignment.Start,
        mainAxisSize = SizeMode.Expand,
        crossAxisSpacing = 8.dp,
        mainAxisSpacing = 8.dp
    ) {
        categories.forEach { item ->
            val selected = selectedCategories.contains(item)
            FilterChip(
                selected = selected,
                modifier = Modifier.height(40.dp),
                onClick = { onSelectedChanged(item) },
                shape = RoundedCornerShape(12.dp),
                colors = ChipDefaults.filterChipColors(
                    backgroundColor = transparentWhite20,
                    contentColor = chipContentColor,
                    selectedBackgroundColor = colorAccent,
                    selectedContentColor = chipContentColor,
                )
            ) {
                Text(
                    text = item.text,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Box(
                    modifier
                        .padding(start = 14.dp, end = 10.dp)
                        .height(20.dp)
                        .width(1.dp)
                        .background(color = if (selected) Color.Transparent else transparentWhite30)
                )
                Icon(
                    painter = painterResource(if (selected) R.drawable.ic_check else R.drawable.ic_plus),
                    contentDescription = "Chip icon"
                )
            }
        }
    }
}