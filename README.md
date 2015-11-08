#WaveView
Android 波浪特效。
实现原理：参考[我的博客](http://blog.csdn.net/hackooo/article/details/49704337)

效果图如下：<br>
![](https://github.com/hackoooo/WaveView/blob/master/images/GIF.gif) 

##如何使用
1.gradle添加依赖
```gradle
compile 'com.hackooo.www.lib:waveview:0.1.0'
```

2.自定义自己的逻辑
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
3.配置图案资源文件和布局参数
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
4.执行动画
让波浪动起来：
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
5.一些简单的api
```Java

public void setStartProgress(float startProgress);          //设置波浪Y轴的起始偏移
public void setPatternScaleFactor(float patternScaleFactor);//设置图案的缩放系数

public void start();          //设置WaveView开始状态，接收update的请求
public void pause();          //设置暂停状态，暂停接收update的请求
public void stop();           //暂停接收update请求，并且隐藏WaveView
public void stopAndRecycle(); //同stop();并且释放资源文件的引用
public boolean isRunning();   //是否接收update请求

//更新WaveView的状态，两个参数均为0~1f之间的值，fraction为波浪水平移动的偏移，progress为Y轴方向的偏移
public void update(float fraction,float progress);
public void update(float fraction);//同上，progress采用默认值
```


