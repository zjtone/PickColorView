package cn.edu.scnu.pickcolorview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 颜色选择类
 */
public class PickColorView extends View {
    private List<Integer> mColors;// colors.size() >= 3
    private int mCurrent, mCurrentSelect;
    private int mViewHeight, mViewWidth;
    private float mMaxHeight, mMinHeight, oldY;
    private float mMovDis;
    private Paint paint;
    private boolean isLastAndMiddle,isTopAndMiddle;

    public PickColorView(Context context) {
        this(context, null);
    }

    public PickColorView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PickColorView(Context context, AttributeSet attr, int defStyleAttr) {
        super(context, attr, defStyleAttr);
        init();
    }

    private void init() {
        mColors = new ArrayList<>();
        paint = new Paint();
        mCurrentSelect = mCurrent = 1;
        mMovDis = 0;
        isLastAndMiddle = isTopAndMiddle = false;
    }

    public void addColor(int color) {
        mColors.add(color);
        mCurrentSelect = mCurrent = mColors.size() / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();
        mMaxHeight = mViewHeight / 3.0f;
        mMinHeight = mMaxHeight / 2.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float scale = calScale(mViewHeight / 2, mViewHeight / 2 - Math.abs(mMovDis));
//        Log.d("tagG","" + (mViewHeight / 2 - Math.abs(mMovDis)));
        float height = (mMaxHeight - mMinHeight) * scale + mMinHeight,
                width = (float) (height * 1.6 / 0.9);
        float left = (mViewWidth - width) / 2, right = left + width;
        float top = (mViewHeight - height) / 2 + mMovDis,
                bottom = top + height;
        paint.setColor(mColors.get(mCurrent % mColors.size()));
        canvas.drawRect(left, top, right, bottom, paint);
        for (int i = 0; i < mCurrent; i++) {
            drawOther(canvas, mCurrent - i, -1);
        }
        for (int i = mCurrent + 1; i < mColors.size(); i++) {
            drawOther(canvas, i - mCurrent, 1);
        }
        if (top < mViewHeight / 2.0 && bottom > mViewHeight / 2.0) {
            mCurrentSelect = mCurrent;
        }
    }

    private void drawOther(Canvas canvas, int i, int type) {
        float scale = calScale(mViewHeight / 2, mViewHeight / 2 - Math.abs(mMovDis + i * type * mViewHeight / 3));
        float height = (mMaxHeight - mMinHeight) * scale + mMinHeight,
                width = (float) (height * 1.6 / 0.9);
        float left = (mViewWidth - width) / 2, right = left + width;
        float top = (mViewHeight - height) / 2 + mMovDis + i * type * mViewHeight / 3,
                bottom = top + height;
        paint.setColor(mColors.get(mCurrent + i * type));
        canvas.drawRect(left, top, right, bottom, paint);
        if (top < mViewHeight / 2.0 && bottom > mViewHeight / 2.0) {
            mCurrentSelect = mCurrent + i * type;
            isLastAndMiddle = mCurrentSelect == mColors.size() - 1 && (top + bottom) / 2 - mViewHeight / 2 <= 0.1;
            isTopAndMiddle = mCurrentSelect == 0 && mViewHeight / 2 - (top + bottom) / 2  <= 0.01;
        }
    }

    // x is distance, so x >= 0
    private float calScale(float zero, float x) {
        float f = x / zero;
        return f < 0 ? 0 : f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                oldY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = event.getY();
                if((isTopAndMiddle && newY > oldY) ||
                        (isLastAndMiddle && newY < oldY))
                    newY = oldY;
                doMove(event,newY);
                break;
            case MotionEvent.ACTION_UP:
                doUp(event);
                break;
        }
        invalidate();
        return true;
    }

    private void doUp(MotionEvent event) {

    }

    private void doMove(MotionEvent event, float newY) {
        mMovDis += newY - oldY;
        oldY = newY;
        if (mMovDis > mViewHeight * (mCurrent + 1) / 3)
            mMovDis = mViewHeight * (mCurrent + 1) / 3;
        else
        if (mMovDis < -1 * mViewHeight * (mCurrent + 1) / 3)
            mMovDis = -1 * mViewHeight * (mCurrent + 1) / 3;
    }
}
