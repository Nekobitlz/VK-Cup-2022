package ru.vk.cup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vk.cup.data.InteractiveItem
import ru.vk.cup.data.InteractiveItemRepository
import ru.vk.cup.ui.theme.VKCup2022Theme
import ru.vk.cup.ui.view.ColumnView
import ru.vk.cup.ui.view.GapsView
import ru.vk.cup.ui.view.LongPressDraggable
import ru.vk.cup.ui.view.PollView
import ru.vk.cup.ui.view.RatingView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = InteractiveItemRepository(this)
        val items = repository.items
        val pollCount = items.count()
        setContent {
            VKCup2022Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    LongPressDraggable(modifier = Modifier.fillMaxSize()) {
                        LazyColumn {
                            items(items) {
                                when (it) {
                                    is InteractiveItem.Poll -> PollView(modifier, it, pollCount)
                                    is InteractiveItem.Rating -> RatingView(modifier, it, pollCount)
                                    is InteractiveItem.Gaps -> GapsView(modifier, it, pollCount)
                                    is InteractiveItem.Column -> ColumnView(modifier, it, pollCount)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
