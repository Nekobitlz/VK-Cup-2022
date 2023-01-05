package ru.vk.cup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import ru.vk.cup.data.InteractiveItem
import ru.vk.cup.presentation.MainViewModel
import ru.vk.cup.ui.theme.VKCup2022Theme
import ru.vk.cup.ui.view.ColumnView
import ru.vk.cup.ui.view.GapsView
import ru.vk.cup.ui.view.LongPressDraggable
import ru.vk.cup.ui.view.PollView
import ru.vk.cup.ui.view.RatingView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKCup2022Theme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "menu") {
                        composable("menu") {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Button(onClick = { navController.navigate(NavigationType.ALL_ITEMS.name) }) {
                                    Text(text = "Все элементы")
                                }
                                Button(onClick = { navController.navigate(NavigationType.POLL.name) }) {
                                    Text(text = "1. Опросы")
                                }
                                Button(onClick = { navController.navigate(NavigationType.COLUMN.name) }) {
                                    Text(text = "2. Сопоставление элементов")
                                }
                                Button(onClick = { navController.navigate(NavigationType.GAPS_WITH_ANSWERS.name) }) {
                                    Text(text = "3. Перетаскивание вариантов")
                                }
                                Button(onClick = { navController.navigate(NavigationType.GAPS_WITH_FIELDS.name) }) {
                                    Text(text = "4. Заполнение пропуска в тексте")
                                }
                                Button(onClick = { navController.navigate(NavigationType.RATING.name) }) {
                                    Text(text = "5. Оценка прочитанной статьи")
                                }
                            }
                        }
                        for (value in NavigationType.values()) {
                            composable(value.name) {
                                ItemsScreen(value)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemsScreen(navigationType: NavigationType, mainViewModel: MainViewModel = viewModel()) {
        val items = mainViewModel.getItems(navigationType).collectAsLazyPagingItems()
        val modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        LongPressDraggable(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(items, key = { it.index }) {
                    when (it) {
                        is InteractiveItem.Poll -> PollView(modifier, it, items.itemCount)
                        is InteractiveItem.Rating -> RatingView(modifier, it, items.itemCount)
                        is InteractiveItem.Gaps -> GapsView(modifier, it, items.itemCount)
                        is InteractiveItem.Column -> ColumnView(modifier, it, items.itemCount)
                        else -> {}
                    }
                }
            }
        }
    }
}

enum class NavigationType {
    ALL_ITEMS, POLL, RATING, GAPS_WITH_ANSWERS, GAPS_WITH_FIELDS, COLUMN
}
