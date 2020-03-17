package com.hongtao.live;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hongtao.live.base.BaseActivity;
import com.hongtao.live.home.HomeFragment;
import com.hongtao.live.live.LiveFragment;
import com.hongtao.live.me.MeFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends BaseActivity {
    private final static String TAG = "MainActivity";

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        final ViewPager2 viewPager2 = findViewById(R.id.main_view_pager);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.main_bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_bottom_live:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.item_bottom_home:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.item_bottom_me:
                        viewPager2.setCurrentItem(2);
                        break;
                }
                return false;
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LiveFragment());
        fragments.add(new HomeFragment());
        fragments.add(new MeFragment());
        final MainVpAdapter mainVpAdapter = new MainVpAdapter(this, fragments);
        viewPager2.setAdapter(mainVpAdapter);
    }
}
