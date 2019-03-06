package com.curtspec2018.homa;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    ImageView iv;
    Animation dropDown;
    Animation zoomOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        iv = findViewById(R.id.iv_logo);

        dropDown = AnimationUtils.loadAnimation(this, R.anim.drop_down);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);

        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });

        dropDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {   }
            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(10, 1000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        iv.startAnimation(dropDown);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            iv.startAnimation(zoomOut);
        }
    };
}
