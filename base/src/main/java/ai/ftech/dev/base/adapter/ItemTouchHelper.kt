package ai.ftech.dev.base.adapter

import android.view.View
import androidx.annotation.IdRes

interface DragItemTouchListener {
    fun onMove(from: Int, to: Int) {}
    fun onMoved(position: Int, direction: Int) {}
}

interface SelectItemTouchListener<T> {
    fun select(t: T) {}
    fun unSelect(t: T) {}
    fun selectAll() {}
    fun clearAll() {}
}

interface DiffUtilListener<T> {
    fun submitList(newData: MutableList<T>?) {}
}

annotation class ItemTouchDrag

annotation class ItemTouchSwipe(
    val mode: SwipeMode,
    val isSpring: Boolean,
    @IdRes val menuId: Int = View.NO_ID,
    @IdRes val contentId: Int = View.NO_ID
)

annotation class ItemTouchSelect(
    val mode: SelectedMode
)

enum class SwipeMode {
    RIGHT_TO_LEFT, LEFT_TO_RIGHT, BOTH
}

enum class SelectedMode {
    SINGLE_SELECT, MULTIPLE_SELECT
}
