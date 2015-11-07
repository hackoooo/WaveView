package com.hackooo.www.aosp;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.hackooo.www.waveview.MyWaveView;

public class MainActivity extends AppCompatActivity {

    MyWaveView waveView;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        waveView = (MyWaveView) findViewById(R.id.waveView);

        //1.start with default animator
        //waveView.startAnim();

        //2. start with custom animator
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,1f);
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

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (waveView.isRunning()) {
                    waveView.pause();
                } else {
                    waveView.start();
                }
            }
        });
    }
}
