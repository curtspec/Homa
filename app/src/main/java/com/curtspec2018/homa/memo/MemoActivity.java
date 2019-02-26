package com.curtspec2018.homa.memo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.MemoListAdapter;
import com.curtspec2018.homa.databinding.ActivityMemoBinding;
import com.curtspec2018.homa.vo.Schedule;

import java.util.ArrayList;
import java.util.Calendar;

public class MemoActivity extends AppCompatActivity {

    ActivityMemoBinding b;
    ArrayList<Schedule> schedules = new ArrayList<>();
    MemoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        b= DataBindingUtil.setContentView(this, R.layout.activity_memo);
        setSupportActionBar(b.toolbar);
        getSupportActionBar().setTitle("메모/일정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        schedules.add(Schedule.getInstanceFromMemo(Calendar.getInstance(), "테스트메모", "테스트입니다. 안나오면 화냄"));
        schedules.add(Schedule.getInstanceFromMemo(Calendar.getInstance(), "테스트메모", "테스트입니다. 안나오면 화냄"));

        adapter = new MemoListAdapter(schedules, getLayoutInflater());
        b.listview.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tenant_option, menu);
        MenuItem item = menu.getItem(0);
        Drawable drawable = item.getIcon();
        if (drawable != null){
            drawable.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }

        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("제목 검색");
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO : List의 호수로 검색
                searchView.setQuery("",false);
                searchView.setIconified(true);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
        });
        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setTextColor(Color.WHITE);
        searchAutoComplete.setHintTextColor(Color.WHITE);

        return super.onCreateOptionsMenu(menu);
    }

    public void clickAdd(View view) {
        startActivityForResult(new Intent(this, MemoEditActivity.class), 10);
    }
}
