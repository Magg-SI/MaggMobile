package pl.tysia.maggstone.ui.presentation_logic

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TOP_FIRST = 300
private const val BOTTOM_LAST = 300
private const val TOP = 16
private const val BOTTOM = 16
private const val LEFT = 0
private const val RIGHT = 0

class RecyclerMarginDecorator(  var mTopFirst : Int = TOP_FIRST,
                                var mBottomLast : Int = BOTTOM_LAST,
                                var mLeft : Int = LEFT,
                                var mRight : Int = RIGHT,
                                var mBottom : Int = BOTTOM,
                                var mTop : Int = TOP) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {

            left = mLeft
            right = mRight
            bottom = mBottom
            top = mTop

            if (parent.getChildAdapterPosition(view) == 0) {
                top = mTopFirst
            }else if (parent.getChildAdapterPosition(view) == parent.adapter!!.itemCount-1) {
                bottom = mBottomLast
            }

        }
    }
}