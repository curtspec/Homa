package com.curtspec2018.homa.memo;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.MemoListAdapter;
import com.curtspec2018.homa.databinding.ActivityMemoBinding;
import com.curtspec2018.homa.vo.Schedule;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

        schedules.addAll(G.getMemos());
        adapter = new MemoListAdapter(schedules, getLayoutInflater());
        b.listview.setAdapter(adapter);

        b.tvHeader.setText("메모개수 : " + schedules.size() + "개");
        b.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
                intent.putExtra("index", position);
                intent.putExtra("date", schedules.get(position).getDate());
                intent.putExtra("title", schedules.get(position).getTitle());
                intent.putExtra("subTitle", schedules.get(position).getSubTitle());
                startActivityForResult(intent, 100);
            }
        });

        b.listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(MemoActivity.this, view);
                popupMenu.inflate(R.menu.house_edit);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.menu_delete:
                                schedules.remove(position);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
        });
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
                for (int i = 0; i< schedules.size(); i ++ ){
                    if (query.equals(schedules.get(i).getTitle())){
                        b.listview.smoothScrollToPosition(i);
                        break;
                    }
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if (resultCode == RESULT_OK){
                    Calendar date = (Calendar) data.getSerializableExtra("date");
                    String title = data.getStringExtra("title");
                    String subTitle = data.getStringExtra("subTitle");
                    int index = data.getIntExtra("index", -1);
                    if (index >= 0){
                        schedules.get(index).setDate(date);
                        schedules.get(index).setTitle(title);
                        schedules.get(index).setSubTitle(subTitle);
                    }else {
                        schedules.add(0, Schedule.getInstanceFromMemo(date, title, subTitle));
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        G.setMemos(schedules);
    }

    public void clickAdd(View view) {
        Intent intent = new Intent(MemoActivity.this, MemoEditActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(){
            @Override
            public void run() {

                ArrayList<Schedule> memos = G.getMemos();
                if (memos.size() > 0) {
                    Gson gson = new Gson();
                    JsonArray jsonArray = new JsonArray();
                    for (Schedule s : memos) {
                        jsonArray.add(gson.toJson(s));
                    }
                    String memosData = jsonArray.toString();

                    String url = G.SERVER_URL+"saveMemos.php";
                    SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("error")){
                                Toast.makeText(MemoActivity.this, "서버와의 통신이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MemoActivity.this, "서버와의 통신이 원활하지 않습니다", Toast.LENGTH_SHORT).show();
                        }
                    });
                    request.addStringParam("memos", memosData);
                    RequestQueue queue = Volley.newRequestQueue(MemoActivity.this);
                    queue.add(request);
                }

            }
        }.start();

    }
}
