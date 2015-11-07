#WaveView
Android 波浪特效。
实现原理：参考[我的博客](http://blog.csdn.net/hackooo/article/details/49704337)

效果图如下：<br>
![](https://github.com/hackoooo/WaveView/blob/master/images/GIF.gif) 

##如何使用
```Java
public class MyWaveView extends WaveView{
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        setPatternScaleFactor(0.618 * 0.618f);              //设置特效图的缩放比例
    }
    
    @Override
    protected void onPreDrawWave(Canvas canvas){
        //画波浪之前做些事情
    }
    @Override
    protected void onPostDrawWave(Canvas canvas){
        //画波浪之后做些事情
    }
}
```

```Java
      <com.hackooo.www.aosp.MyWaveView
        android:id="@+id/waveView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:waveRes="@drawable/loading_bg"        //自定义波浪的资源文件
        app:patternRes="@drawable/loading_mid"    //自定义图案的资源文件
        app:patternScaleFactor="0.618f"           //自定义图案相对于布局的缩放系数
        />
```

让动画动起来：
```Java
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1f);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.setRepeatMode(ValueAnimator.INFINITE);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                waveView.update(fraction);
            }
        });
        valueAnimator.start();
        waveView.start();
```



