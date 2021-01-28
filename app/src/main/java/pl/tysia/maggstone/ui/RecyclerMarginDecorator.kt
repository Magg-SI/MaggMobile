package pl.tysia.maggstone.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

private const val TOP_FIRST = 128
private const val BOTTOM_LAST = 128
private const val TOP = 16
private const val BOTTOM = 16
private const val LEFT = 0
private const val RIGHT = 0

class RecyclerMarginDecorator(var mTopFirst : Int = TOP_FIRST,
                              var mBottomLast : Int = BOTTOM_LAST,
                              var mTop : Int = TOP,
                              var mBottom : Int = BOTTOM,
                              var mLeft : Int = LEFT,
                              var mRight : Int = RIGHT,
                              var numberInRow : Int = 1 ) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {

            left = mLeft
            right = mRight
            bottom = mBottom
            top = mTop

            if (parent.getChildAdapterPosition(view) == 0) {
                top = mTopFirst
            }else if (parent.getChildAdapterPosition(view) >= parent.adapter!!.itemCount - (parent.adapter!!.itemCount % numberInRow) - 1) {
                bottom = mBottomLast
            }

        }
    }
}