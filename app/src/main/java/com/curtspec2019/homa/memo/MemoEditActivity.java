package com.curtspec2019.homa.memo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.curtspec2019.homa.R;
import com.curtspec2019.homa.databinding.ActivityMemoEditBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MemoEditActivity extends AppCompatActivity{

    ActivityMemoEditBinding b;

    Intent intent;

    String title = "";
    String subTitle = "";
    Calendar date;
    Calendar dateFromParents;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_edit);
         b = DataBindingUtil.setContentView(this, R.layout.activity_memo_edit);

         intent = getIntent();
         index = intent.getIntExtra("index", -1);
         dateFromParents = (Calendar) intent.getSerializableExtra("date");
         title = intent.getStringExtra("title");
         subTitle = intent.getStringExtra("subTitle");

         setSupportActionBar(b.toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         if (title != null) getSupportActionBar().setTitle(title);
         else getSupportActionBar().setTitle("메모등록");

         if (dateFromParents == null) date = Calendar.getInstance();
         else date = dateFromParents;

         b.tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(date.getTimeInMillis())));

         if (title != null) b.editTitle.setText(title);
         b.editTitle.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {  }
             @Override
             public void afterTextChanged(Editable s) {
                 title = b.editTitle.getText().toString();
                 subTitle = b.editSubTitle.getText().toString();
                 if(title.equals("") && subTitle.equals("")) fabGone();
                 else fabShow();
             }
         });
        if (subTitle != null) b.editSubTitle.setText(subTitle);
         b.editSubTitle.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {  }
             @Override
             public void afterTextChanged(Editable s) {
                 title = b.editTitle.getText().toString();
                 subTitle = b.editSubTitle.getText().toString();
                 if(title.equals("") && subTitle.equals("")) fabGone();
                 else fabShow();
             }
         });

         b.switchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if (isChecked){
                     Calendar calendar = Calendar.getInstance();
                     DatePickerDialog dialog = new DatePickerDialog(MemoEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                         @Override
                         public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                             Calendar selected = Calendar.getInstance();
                             selected.set(year, month, dayOfMonth);
                             date = selected;
                             b.tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(selected.getTimeInMillis())));
                         }
                     }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                     dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                         @Override
                         public void onCancel(DialogInterface dialog) {
                             b.switchType.setChecked(false);
                         }
                     });
                     dialog.setCanceledOnTouchOutside(false);
                     dialog.show();
                 }else{
                     if (dateFromParents == null) date = Calendar.getInstance();
                     else date = dateFromParents;
                     b.tvTime.setText(new SimpleDateFormat("yyyy.MM.dd").format(new Date(date.getTimeInMillis())));
                 }
             }
         });



    }//onCreate()

    public void clickOK(View view) {
        String title = b.editTitle.getText().toString();
        String subTitle = b.editSubTitle.getText().toString();
        if (title.equals("") && subTitle.equals("")) return;
        intent.putExtra("date", date);
        intent.putExtra("title", title);
        intent.putExtra("subTitle", subTitle);
        intent.putExtra("index", index);
        setResult(RESULT_OK, intent);
        finish();
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
