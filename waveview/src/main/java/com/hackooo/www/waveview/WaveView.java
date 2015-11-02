package com.hackooo.www.waveview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by hackooo on 2015/10/29.
 * Email:hackooo@sina.cn
 */
public class WaveView extends View implements ValueAnimator.AnimatorUpdateListener{
    float factor = 0.618f;              //背景圆形的缩放系数
    float midfactor = factor * 0.618f;  //中间图标的缩放系数

    //动画
    ValueAnimator valueAnimator;
    float startProgress = 0.0f;         //起始偏移
    float progress = 0.0f;              //当前加载的进度

    float lastFraction = 0f;            //最后一次动画执行时的fraction
    int progressCount = 0;              //动画执行了几圈了

    PorterDuffXfermode clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    PorterDuffXfermode atTopMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);

    //背景圆形
    int bgColor = Color.parseColor("#88000000"); //背景圆形的颜色
    int circleRadius;                            //背景圆形的半径

    //中间图标
    public static Bitmap mid;
    int midw, midh;
    int midTargetWidth,midTargetHeight;

    //波浪条
    public static Bitmap bg;
    int bgw,bgh;

    //=============branche ttttttttt
    //=============branch  test

    //缩放后的中间图标
    Bitmap scaleBitmap;
    BitmapShader shader;
    Paint paint,paint2;

    //视图的宽高
    int width,height;

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);

        loadRes();
        bgw = bg.getWidth();
        bgh = bg.getHeight();
        midw = mid.getWidth();
        midh = mid.getHeight();

        paint = new Paint();
        paint2 = new Paint();
    }
    //TODO 这两个图检查
    private void loadRes(){
        if(null == bg || bg.isRecycled()){
            bg = BitmapFactory.decodeResource(getResources(), R.mipmap.loading_bg);
        }
        if(null == mid || mid.isRecycled()){
            mid = BitmapFactory.decodeResource(getResources(), R.mipmap.loading_mid);
        }
    }

    public void setFactor(float f){
        this.factor = f;
        midfactor = factor * 0.618f;
    }
    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setStartProgress(float startProgress) {
        this.startProgress = startProgress;
    }

    public void startAnim(){
        startAnim(null);
    }
    public void startAnim(ValueAnimator animator){
        if(null == animator){
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.addUpdateListener(this);
            valueAnimator.setDuration(1000);
            valueAnimator.setRepeatMode(ValueAnimator.INFINITE);
            valueAnimator.setRepeatCount(Integer.MAX_VALUE);
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.setStartDelay(0);
            valueAnimator.start();
        }else{
            animator.addUpdateListener(this);
        }
    }
    public void hide(){
        setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);

        circleRadius = (int)(width*factor);
        circleRadius = circleRadius > (int)(height*factor)?(int)(height*factor):circleRadius;
        circleRadius /= 2f;

        midTargetWidth = (int) (width * midfactor);
        midTargetHeight = (int) (height * midfactor);
        if(midTargetWidth > midTargetHeight){
            midTargetWidth = midTargetHeight;
        }else{
            midTargetHeight = midTargetWidth;
        }
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }
    @Override
    protected void onDraw(Canvas canvas){
        if(null == scaleBitmap){
            scaleBitmap = Bitmap.createScaledBitmap(mid, midTargetWidth, midTargetHeight, true);
        }
        if(null == shader){
            shader = new BitmapShader(bg, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        }

        //画背景
        int sc = canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        paint.setColor(bgColor);
        canvas.drawCircle(width / 2f, height / 2f, circleRadius, paint);
        paint.setXfermode(clearMode);
        canvas.drawBitmap(scaleBitmap, getStartX(midTargetWidth), getStartY(midTargetHeight), paint);
        paint.setXfermode(null);
        canvas.restoreToCount(sc);

        //画中间层的颜色
        sc = canvas.saveLayer(0,0,width,height,null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(scaleBitmap, getStartX(midTargetWidth), getStartY(midTargetHeight), paint2);
        paint2.setXfermode(atTopMode);
        paint2.setShader(shader);

        if(fraction < lastFraction){
            ++progressCount;
        }
        lastFraction = fraction;
        float offsetX = (fraction+progressCount)*midTargetWidth;
        float translationX = getStartX(midTargetWidth) - offsetX;
        canvas.translate(translationX, getStartY(midTargetHeight) + midTargetHeight - bgh - midTargetHeight * startProgress - midTargetHeight *(1-startProgress) * progress );
        canvas.drawRect(0, 0, midTargetWidth + offsetX, midTargetHeight + bgh, paint2);
        paint2.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    //使目标图画出来处于中心
    private float getStartX(int w){
        return (int)(width/2f - w/2f);
    }
    //使目标图画出来处于中心
    private float getStartY(int h){
        return (int)(height/2f - h/2f);
    }

    float fraction = 0f;

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (getVisibility() == VISIBLE) {
            fraction = animation.getAnimatedFraction();
            invalidate();
        }else{
            animation.removeUpdateListener(this);
            if(null != valueAnimator && animation.equals(valueAnimator)){
                valueAnimator.cancel();
            }
        }
    }
}
