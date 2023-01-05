package ru.vk.cup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
    private fun ItemsScreen(navigationType: NavigationType) {
        val repository = remember { InteractiveItemRepository(this) }
        val items = rememberSaveable {
            when (navigationType) {
                NavigationType.POLL -> (0 until 10).map { i -> repository.generatePoll(i, (4..10).random()) }
                NavigationType.ALL_ITEMS -> repository.generateItems()
                NavigationType.RATING -> (0 until 10).map { i -> repository.generateRating(i, (4..10).random()) }
                NavigationType.GAPS_WITH_ANSWERS -> (0 until  10).map { i -> InteractiveItemRepository.generateGapsWithAnswers(i) }
                NavigationType.GAPS_WITH_FIELDS -> (0 until 10).map { i -> InteractiveItemRepository.generateGapsWithFields(i) }
                NavigationType.COLUMN -> (0 until 10).map { i -> repository.generateColumn(i, (4..10).random()) }
            }
        }
        val count = items.count()
        val modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        LongPressDraggable(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(items) {
                    when (it) {
                        is InteractiveItem.Poll -> PollView(modifier, it, count)
                        is InteractiveItem.Rating -> RatingView(modifier, it, count)
                        is InteractiveItem.Gaps -> GapsView(modifier, it, count)
                        is InteractiveItem.Column -> ColumnView(modifier, it, count)
                    }
                }
            }
        }
    }
}

enum class NavigationType {
    ALL_ITEMS, POLL, RATING, GAPS_WITH_ANSWERS, GAPS_WITH_FIELDS, COLUMN
}
