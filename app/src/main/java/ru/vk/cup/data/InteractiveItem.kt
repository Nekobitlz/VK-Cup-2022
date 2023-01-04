package ru.vk.cup.data

sealed class InteractiveItem {
    data class Poll(val index: Int, val title: String, val items: MutableList<PollItem>) :
        InteractiveItem() {
        data class PollItem(val text: String, var isSelected: Boolean = false)
    }

    data class Column(val index: Int,
                      var pairs: Map<ColumnItem, ColumnItem>,
                      val correctPairs: Map<ColumnItem, ColumnItem>,
    ) : InteractiveItem() {
        data class ColumnItem(val text: String)
    }

    data class Gaps(
        val index: Int, val text: List<GapItem>, val answers: List<GapItem>? = null
    ) : InteractiveItem() {

        data class GapItem(
            val text: String,
            val type: GapTextType = GapTextType.DEFAULT,
        )

        enum class GapTextType {
            DEFAULT, ANSWER, TEXT_FIELD
        }
    }

    data class Rating(val index: Int, val count: Int, var selected: Int = -1) : InteractiveItem()
}