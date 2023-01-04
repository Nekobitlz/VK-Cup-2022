package ru.vk.cup.ui.view

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vk.cup.ui.theme.secondaryText

@Composable
fun ColumnScope.InteractiveItemTitleView(title: String, subtitle: String) = with (this) {
    Text(
        text = subtitle,
        color = secondaryText,
        fontSize = 13.sp,
    )
    Text(
        text = title,
        fontSize = 21.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}