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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.curtspec2018.homa.G;
import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.TenantPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityTenantBinding;
import com.curtspec2018.homa.vo.Room;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        Room room = (Room) data.getSerializableExtra("room");
        boolean isDelete = data.getBooleanExtra("delete", false);
        switch (requestCode){
            case REQUEST_CREATE_ROOM :
                if (resultCode == RESULT_OK && room != null){
                    FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
                    floorFragment.addItem(room);
                }
                break;
            case REQUEST_EDIT_FLOOR:
                if (resultCode == RESULT_OK && room != null && !isDelete){
                    FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
                    floorFragment.editItem(room, requestedItemPosition);
                }else if (resultCode == RESULT_OK && room != null && isDelete){
                    FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
                    floorFragment.deleteItem(room, requestedItemPosition);
                }
                break;
            case REQUEST_EDIT_EMPTY:
                b.viewPager.setCurrentItem(1, true);
                if (resultCode == RESULT_OK && room != null && !isDelete){
                    FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
                    floorFragment.editItem(room);
                }else if (resultCode == RESULT_OK && room != null && isDelete){
                    FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
                    floorFragment.deleteItem(room);
                }
                break;
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
    protected void onResume() {
        super.onResume();
        FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
        floorFragment.refreshView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        FloorFragment floorFragment = (FloorFragment) adapter.getItem(0);
        floorFragment.saveData();
    }
}
