package pl.tysia.maggstone.ui.sign

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import pl.tysia.maggstone.R

class SignView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    private var linePaint : Paint = Paint().apply {
        val color = context.resources.getColor(R.color.colorAccent)
        setColor(color)
        strokeWidth = 4f
        style = Paint.Style.STROKE
    }

    private var touchPath: Path = Path()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event!!.x;
        val touchY = event.y;
        when (event.action) {

            MotionEvent.ACTION_DOWN ->{
                touchPath.moveTo(touchX, touchY);
            }

            MotionEvent.ACTION_MOVE -> {
                touchPath.lineTo(touchX, touchY);
            }

            MotionEvent.ACTION_UP -> {
                touchPath.lineTo(touchX, touchY);
                touchPath = Path();
            }

            else -> return false;
        }

        invalidate();
        return true;
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawPath(touchPath, linePaint)
    }

}