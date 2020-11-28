package pl.tysia.maggstone.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TOP_FIRST = 300
private const val BOTTOM_LAST = 300
private const val TOP = 16
private const val BOTTOM = 16
private const val LEFT = 0
private const val RIGHT = 0

class RecyclerMarginDecorator() : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {

            left = LEFT
            right = RIGHT
            bottom = BOTTOM
            top = TOP

            if (parent.getChildAdapterPosition(view) == 0) {
                top = TOP_FIRST
            }else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount-1) {
                bottom = BOTTOM_LAST
            }

        }
    }
}