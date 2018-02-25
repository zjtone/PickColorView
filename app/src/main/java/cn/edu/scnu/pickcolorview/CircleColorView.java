package cn.edu.scnu.pickcolorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by zjt on 18-2-25.
 */

public class CircleColorView extends View {
    private int mBorderColor, mColor;
    private Paint mColorPaint;
    private boolean isActive;

    public CircleColorView(Context context) {
        super(context);
        init();
    }

    public CircleColorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleColorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mColorPaint = new Paint();
        mColor = Color.BLACK;
        mBorderColor = Color.WHITE;
        isActive = false;
    }

    public void setBorderColor(int color) {
        this.mBorderColor = color;
    }

    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth(),
                height = getMeasuredHeight(),
                radius = width > height ? height / 2 : width / 2,
                borderWidth = (int) (radius * 0.1 / 0.4);
        mColorPaint.setStrokeWidth(borderWidth);
        mColorPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mColorPaint.setColor(mColor);
        float left = getPaddingLeft() + width / 2,
                top = getPaddingTop() + height / 2;
        if (!isActive) {
            canvas.drawCircle(left, top,
                    (float) (radius - borderWidth * 1.2), mColorPaint);
            return;
        }
        canvas.drawCircle(left, top, radius - borderWidth, mColorPaint);
        mColorPaint.setStyle(Paint.Style.STROKE);
        mColorPaint.setColor(mBorderColor);
        canvas.drawCircle(left, top,
                radius - borderWidth / 2, mColorPaint);
    }
}
