package ru.vk.cup.data

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.compose.runtime.toMutableStateMap
import ru.vk.cup.R
import ru.vk.cup.data.InteractiveItem.*
import java.util.LinkedList

class InteractiveItemRepository(private val context: Context) {

    fun generateItems(from: Int, size: Int): List<InteractiveItem> = (from until from + size).map { i ->
        when (i % 5) {
            1 -> generateColumn(i, (4..10).random())
            2 -> generateGapsWithFields(i)
            3 -> generateGapsWithAnswers(i)
            4 -> generateRating(i, (5..10).random())
            else -> generatePoll(i, (4..10).random())
        }
    }

    fun generateGapsWithAnswers(from: Int, size: Int) = (from until from + size).map {
        generateGapsWithAnswers(it)
    }

    fun generateGapsWithFields(from: Int, size: Int) = (from until from + size).map {
        generateGapsWithFields(it)
    }

    fun generateRatings(from: Int, size: Int) = (from until from + size).map {
        generateRating(it, (5..10).random())
    }

    fun generatePolls(from: Int, size: Int) = (from until from + size).map {
        generatePoll(it, (4..10).random())
    }

    private fun generatePoll(index: Int, size: Int): Poll {
        return Poll(
            index = index + 1,
            title = context.getString(R.string.poll_question_title, index + 1),
            items = (1 until size).map { generatePollItem(it) }.toMutableStateList()
        )
    }

    fun generateColumns(from: Int, size: Int) = (from until from + size).map {
        generateColumn(it, (4..10).random())
    }

    private fun generateColumn(index: Int, size: Int): Column {
        val correctPairs = (0 until size).associate {
            Column.ColumnItem(getStringNumber(it)) to Column.ColumnItem(it.toString())
        }.toList().toMutableStateMap()
        val randomValues = LinkedList(correctPairs.values.shuffled())
        return Column(
            index + 1,
            pairs = correctPairs.keys.associateWith { randomValues.pop() },
            correctPairs = correctPairs
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
                index + 1,
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
                index + 1,
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

    private fun generateRating(index: Int, size: Int): Rating {
        return Rating(index + 1, size)
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