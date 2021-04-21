package pl.tysia.maggstone.ui.sign

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import pl.tysia.maggstone.R

class SignView(context : Context, attrs : AttributeSet) : View(context, attrs) {

    var bmp: Bitmap? = null
    private var mCanvas = Canvas()

    private var linePaint : Paint = Paint().apply {
        val color = context.resources.getColor(R.color.colorAccent)
        setColor(color)
        strokeWidth = 4f
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private var touchPath: Path = Path()


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = Canvas(bmp!!);
    }

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
            }

            else -> return false;
        }

        invalidate();
        return true;
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawPath(touchPath, linePaint)
        mCanvas.drawPath(touchPath, linePaint)
    }

}