package com.curtspec2018.homa.tenant;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.TenantPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityTenantBinding;
import com.curtspec2018.homa.vo.Floor;
import com.curtspec2018.homa.vo.Room;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;

public class TenantActivity extends AppCompatActivity {

    ActivityTenantBinding b;
    TenantPagerAdapter adapter;
    MenuItem item;

    int requestedItemPosition;

    public static final int REQUEST_EDIT_FLOOR = 1;
    public static final int REQUEST_EDIT_EMPTY = 2;
    public static final int REQUEST_CREATE_ROOM = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        b = DataBindingUtil.setContentView(this, R.layout.activity_tenant);

        setSupportActionBar(b.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.tenant_activity_title);
        adapter = new TenantPagerAdapter(getSupportFragmentManager());
        b.viewPager.setAdapter(adapter);

        b.tab.setupWithViewPager(b.viewPager);
        b.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {            }
            @Override
            public void onPageSelected(int i) {            }
            @Override
            public void onPageScrollStateChanged(int i) {
                int p = b.viewPager.getCurrentItem();
                item.setVisible(p == 0 ? true : false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tenant_option, menu);
        item = menu.getItem(0);
        Drawable drawable = item.getIcon();
        if (drawable != null){
            drawable.mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        }
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("호수입력");
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                FloorFragment f = (FloorFragment) adapter.getItem(0);
                f.scrollTo(query);
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
        if (data == null) return;
        switch (requestCode){
            case REQUEST_EDIT_EMPTY:
                b.viewPager.setCurrentItem(1, true);
                break;
        }
        if (resultCode == RESULT_OK){
            FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
            EmptyFragment emptyFragment = (EmptyFragment) adapter.getItem(1);
            floorFragment.refreshView();
            emptyFragment.refreshView();
        }
    }

    public void editRoomInfo(Room room, int requestCode, int position){
        Intent intent = new Intent(this, TenantEditActivity.class);
        intent.putExtra("type", "edit");
        intent.putExtra("room", room);
        startActivityForResult(intent, requestCode);
        requestedItemPosition = position;
    }

    public void createRoomInfo(){
        if (G.getCurrentBuilding() == null){
            Toast.makeText(this, "건물을 먼저 등록해야 합니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, TenantEditActivity.class);
        intent.putExtra("type", "new");
        startActivityForResult(intent, REQUEST_CREATE_ROOM);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (G.getCurrentBuilding() != null) {
            JsonArray roomsJson = new JsonArray();
            Gson gson = new Gson();
            ArrayList<Floor> floors =G.getCurrentBuilding().getFloors();
            for (Floor f : floors){
                for (Room r : f.getRooms()){
                    roomsJson.add(gson.toJson(r));
                }
            }
            String url = G.SERVER_URL + "saveRooms.php";
            SimpleMultiPartRequest request = new SimpleMultiPartRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(TenantActivity.this, "서버연결에 문제발생", Toast.LENGTH_SHORT).show();
                }
            });

            request.addStringParam("id", G.getId());
            request.addStringParam("belong", G.getCurrentBuilding().getTag());
            request.addStringParam("rooms", roomsJson.toString());

            Volley.newRequestQueue(this).add(request);
        }
    }
}
