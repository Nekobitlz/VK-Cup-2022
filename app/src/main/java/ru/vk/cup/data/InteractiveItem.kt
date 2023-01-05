package ru.vk.cup.data

sealed class InteractiveItem(open val index: Int) {
    data class Poll(override val index: Int, val title: String, val items: MutableList<PollItem>) :
        InteractiveItem(index) {
        data class PollItem(val text: String, var isSelected: Boolean = false)
    }

    data class Column(override val index: Int,
                      var pairs: Map<ColumnItem, ColumnItem>,
                      val correctPairs: Map<ColumnItem, ColumnItem>,
    ) : InteractiveItem(index) {
        data class ColumnItem(val text: String)
    }

    data class Gaps(
        override val index: Int, val text: List<GapItem>, val answers: List<GapItem>? = null
    ) : InteractiveItem(index) {

        data class GapItem(
            val text: String,
            val type: GapTextType = GapTextType.DEFAULT,
        )

        enum class GapTextType {
            DEFAULT, ANSWER, TEXT_FIELD
        }
    }

    data class Rating(override val index: Int, val count: Int, var selected: Int = -1) : InteractiveItem(index)
}