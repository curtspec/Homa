package com.curtspec2018.homa.memo;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.databinding.ActivityMemoEditBinding;

public class MemoEditActivity extends AppCompatActivity{

    ActivityMemoEditBinding b;

    String title = "";
    String subTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
         b = DataBindingUtil.setContentView(this, R.layout.activity_memo_edit);
         setSupportActionBar(b.toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle("");

         b.editTitle.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {  }
             @Override
             public void afterTextChanged(Editable s) {
                 title = s.toString();
                 if(title.equals("") && subTitle.equals("")) fabGone();
                 else fabShow();
             }
         });
         b.editSubTitle.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {  }
             @Override
             public void afterTextChanged(Editable s) {
                 title = s.toString();
                 if(title.equals("") && subTitle.equals("")) fabGone();
                 else fabShow();
             }
         });
         
    }

    public void clickOK(View view) {

    }

    private void fabGone(){
        if (b.fab.getVisibility() == View.GONE) return;
        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        zoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {  }
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationEnd(Animation animation) {
                b.fab.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        b.fab.startAnimation(zoomOut);
    }

    private void fabShow(){
        if (b.fab.getVisibility() == View.VISIBLE) return;
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        zoomIn.setAnimationListener(new Animation.AnimationListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onAnimationStart(Animation animation) {
                b.fab.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        b.fab.startAnimation(zoomIn);
    }

}
