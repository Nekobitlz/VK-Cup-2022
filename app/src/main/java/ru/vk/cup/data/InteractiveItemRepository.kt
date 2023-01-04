package ru.vk.cup.data

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.toMutableStateMap
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem.*

class InteractiveItemRepository(private val context: Context) {

    val items by lazy(LazyThreadSafetyMode.NONE) { generateItems() }

    fun generateItems(): List<InteractiveItem> = (0 until 10).map { i ->
        generatePoll(i, (4..10).random())
        when (i % 5) {
            1 -> generateColumn(i, (4..10).random())
            2 -> generateGapsWithFields(i)
            3 -> generateGapsWithAnswers(i)
            4 -> generateRating(i, (5..10).random())
            else -> generatePoll(i, (4..10).random())
        }
    }.toMutableStateList()

    fun generatePoll(index: Int, size: Int): Poll {
        return Poll(
            index = index + 1,
            title = context.getString(R.string.poll_question_title, index + 1),
            items = (1 until size).map { generatePollItem(it) }.toMutableStateList()
        )
    }

    fun generateColumn(index: Int, size: Int): Column {
        return Column(
            index,
            pairs = (0 until size).associate {
                Column.ColumnItem(getStringNumber(it)) to Column.ColumnItem(it.toString())
            }.toList().toMutableStateMap()
        )
    }

    companion object {
        fun generateGapsWithAnswers(index: Int): Gaps {
            val answers = listOf(
                Gaps.GapItem("с", Gaps.GapTextType.ANSWER),
                Gaps.GapItem("и", Gaps.GapTextType.ANSWER),
                Gaps.GapItem("один", Gaps.GapTextType.ANSWER),
                Gaps.GapItem("два", Gaps.GapTextType.ANSWER),
                Gaps.GapItem("очень длинный вариант", Gaps.GapTextType.ANSWER),
            )
            return Gaps(
                index,
                listOf(
                    Gaps.GapItem("Текст"),
                    answers[0],
                    Gaps.GapItem("несколькими пропусками"),
                    answers[1],
                    Gaps.GapItem("вариантами")
                ), answers.shuffled()
            )
        }

        fun generateGapsWithFields(index: Int): Gaps {
            val answers = listOf(
                Gaps.GapItem("с", Gaps.GapTextType.TEXT_FIELD),
                Gaps.GapItem("и", Gaps.GapTextType.TEXT_FIELD),
            )
            return Gaps(
                index,
                listOf(
                    Gaps.GapItem("Текст"),
                    answers[0],
                    Gaps.GapItem("несколькими пропусками"),
                    answers[1],
                    Gaps.GapItem("вариантами")
                )
            )
        }
    }

    fun generateRating(index: Int, size: Int): Rating {
        return Rating(index, size)
    }

    private fun generatePollItem(number: Int) = Poll.PollItem(
        text = getStringNumber(number)
    )

    private fun getStringNumber(number: Int) = context.getString(
        when (number) {
            1 -> R.string.one
            2 -> R.string.two
            3 -> R.string.three
            4 -> R.string.four
            5 -> R.string.five
            6 -> R.string.six
            7 -> R.string.seven
            8 -> R.string.eight
            9 -> R.string.nine
            10 -> R.string.ten
            else -> R.string.other
        }
    )
}