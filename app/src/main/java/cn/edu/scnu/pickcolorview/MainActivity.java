package cn.edu.scnu.pickcolorview;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mCircleColorView;
    private CircleColorAdapter mCircleAdapter;
    private List<Integer> mColorList;
    private List<Boolean> mActiveList;
    private int mSelected = 0;
    private ValueAnimator mOpenAnimator, mCloseAnimator;
    private boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        colorPicker1();
        colorPicker2();
    }

    private void colorPicker1() {
        // 颜色选择器1
        PickColorView pickColorView = findViewById(R.id.pickcolor);
        pickColorView.addColor(0xff030303);
        pickColorView.addColor(0xffFF4500);
        pickColorView.addColor(0xffE0EEE0);
        pickColorView.addColor(0xffCD1076);
        pickColorView.addColor(0xffA1A1A1);
        pickColorView.addColor(0xff9A32CD);
        pickColorView.invalidate();
    }

    private void colorPicker2() {
        // 颜色选择器2
        mCircleColorView = findViewById(R.id.circleColorList);
        mColorList = new ArrayList<>();
        mActiveList = new ArrayList<>();
        initColorsData();
        mCircleAdapter = new CircleColorAdapter(this, mColorList, mActiveList);
        mCircleAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mActiveList.set(mSelected, false);
                mActiveList.set(position, true);
                mSelected = position;
                mCircleAdapter.notifyDataSetChanged();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCircleColorView.setLayoutManager(linearLayoutManager);
        mCircleColorView.setAdapter(mCircleAdapter);
        // 动画部分
        final ImageView control = findViewById(R.id.control);
        final int width = getScreenWidth() - 200;
        mOpenAnimator = ValueAnimator.ofInt(1, 100);
        mOpenAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator evaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mCircleColorView.getLayoutParams().width = evaluator.evaluate(fraction, 1, width);
                mCircleColorView.requestLayout();
                if (fraction >= 0.95)
                    control.setBackgroundResource(R.drawable.right);
            }
        });
        mCloseAnimator = ValueAnimator.ofInt(1, 100);
        mCloseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private IntEvaluator evaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = valueAnimator.getAnimatedFraction();
                mCircleColorView.getLayoutParams().width = evaluator.evaluate(fraction, width, 1);
                mCircleColorView.requestLayout();
                if (fraction >= 0.95)
                    control.setBackgroundResource(R.drawable.left);
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    // 关闭
                    isOpen = false;
                    mCloseAnimator.setDuration(1200).start();
                } else {
                    // 打开
                    isOpen = true;
                    mOpenAnimator.setDuration(1200).start();
                }
                mCircleColorView.scrollToPosition(mSelected);
            }
        });
    }


    private void initColorsData() {
        mColorList.add(0xff030303);
        mColorList.add(0xffFF4500);
        mColorList.add(0xffE0EEE0);
        mColorList.add(0xffCD1076);
        mColorList.add(0xffA1A1A1);
        mColorList.add(0xff9A32CD);
        mColorList.add(0xffEEE5DE);
        mColorList.add(0xffEE82EE);
        mColorList.add(0xffCD5C5C);
        mColorList.add(0xffA52A2A);
        for (int i = 0; i < mColorList.size(); i++)
            mActiveList.add(false);
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
}
