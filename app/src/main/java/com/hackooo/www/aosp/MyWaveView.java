package com.hackooo.www.aosp;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hackooo.www.waveview.WaveView;

/**
 * Created by hackooo on 2015/10/29.
 * Email:hackooo@sina.cn
 */
public class MyWaveView extends WaveView {
    float factor = 0.618f;              //背景圆形的缩放系数

    //背景圆形
    int bgColor = Color.parseColor("#88000000"); //背景圆形的颜色
    int circleRadius;                            //背景圆形的半径

    //缩放后的中间图标
    Paint paint;

    public MyWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(bgColor);
        paint.setAntiAlias(true);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        circleRadius = (int) ((getLayoutWidth() > getLayoutHeight()? getLayoutHeight(): getLayoutWidth())/2f * factor);
        setPatternScaleFactor(factor * 0.618f);
    }
    public void hide(){
        ValueAnimator animator = ValueAnimator.ofFloat(0,1f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                setAlpha(1 - fraction);
                if (fraction == 1f) {
                    setVisibility(GONE);
                    setAlpha(1f);
                }
            }
        });
        animator.setDuration(200);
        animator.start();
    }
    @Override
    protected void onPreDrawWave(Canvas canvas){
        //画背景
//        int sc = canvas.saveLayer(0, 0, getLayoutWidth(), getLayoutHeight(), null, Canvas.ALL_SAVE_FLAG);
//        canvas.drawCircle(getLayoutWidth() / 2f, getLayoutHeight() / 2f, circleRadius, paint);
//        canvas.restoreToCount(sc);
    }
}
