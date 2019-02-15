package com.curtspec2018.homa.tenant;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;

import com.curtspec2018.homa.R;
import com.curtspec2018.homa.adapter.TenantPagerAdapter;
import com.curtspec2018.homa.databinding.ActivityTenantBinding;

public class TenantActivity extends AppCompatActivity {

    ActivityTenantBinding b;
    TenantPagerAdapter adapter;
    MenuItem item;

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
}
