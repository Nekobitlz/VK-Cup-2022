package ru.vk.cup

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.vk.cup.data.Category
import ru.vk.cup.ui.theme.VKCup2022Theme
import ru.vk.cup.ui.theme.transparentWhite15
import ru.vk.cup.ui.theme.transparentWhite50

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VKCup2022Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ZenCategoriesView()
                }
            }
        }
    }
}

@Composable
fun ZenCategoriesView() {
    var selectedCategories by rememberSaveable { mutableStateOf<List<Category>>(listOf()) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val context = LocalContext.current
        CategoriesHeader()
        ChipGroupMultiSelection(
            selectedCategories = selectedCategories,
            onSelectedChanged = {
                val oldList = selectedCategories.toMutableList()
                if (it in selectedCategories) {
                    oldList.remove(it)
                } else {
                    oldList.add(it)
                }
                selectedCategories = oldList
            }
        )
        Button(
            modifier = Modifier
                .padding(vertical = 20.dp),
            onClick = { openZen(context) },
            shape = RoundedCornerShape(74.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 51.dp, vertical = 29.dp),
                text = stringResource(R.string.go_next),
                style = TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun CategoriesHeader() {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        val context = LocalContext.current
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.categories_title),
            color = transparentWhite50,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Normal)
        )
        Button(
            modifier = Modifier
                .padding(start = 12.dp)
                .height(40.dp),
            onClick = { openZen(context) },
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = transparentWhite15,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.skip), style = MaterialTheme.typography.body2)
        }
    }
}

private fun openZen(context: Context) = context.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://dzen.ru/discover")
    }
)
