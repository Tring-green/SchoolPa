package com.example.schoolpa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.schoolpa.adapter.SectionsPagerAdapter;
import com.example.schoolpa.fragment.Data.OfficialDocumentData;
import com.example.schoolpa.fragment.Data.ScheduleData;
import com.example.schoolpa.fragment.OfficialDocumentFragment;
import com.example.schoolpa.fragment.ScheduleFragment;
import com.example.schoolpa.R;
import com.example.schoolpa.service.ChatCoreService;
import com.example.schoolpa.utils.CommonUtil;
import com.example.schoolpa.utils.SharedPreferenceUtils;

/**
 *
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        ScheduleFragment.OnListFragmentInteractionListener,
        OfficialDocumentFragment.OnListFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Toolbar toolbar;

    private ViewPager mViewPager;
    private String[] weburl;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "MainActivity start.");
        initData();
        initView();
    }

    private void initData() {
        // 开启服务
        if (!CommonUtil.isServiceRunning(this,
                ChatCoreService.class)) {
            this.startService(
                    new Intent(this,
                            ChatCoreService.class));
            Log.d("MainActivity", "ChatCoreService start.");
        }

    }

    public void initView() {
        setContentView(R.layout.activity_main);
        initToolbar();
        initFloatingActionButton();
        initViewPager();
        initTabLayout();
        initDrawerLayout();
        initNavigationView();
    }


    /**
     * 初始化顶栏toolbar
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem == 0)
            mFab.setVisibility(View.GONE);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mFab.setVisibility(View.GONE);
                        break;
                    default:
                        mFab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化tablayout
     */
    private void initTabLayout() {
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * 初始化floatingactionbutton
     */
    private void initFloatingActionButton() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferenceUtils.putString(MainActivity.this, "NO", null);
                SharedPreferenceUtils.putString(MainActivity.this, "len", null);
                SharedPreferenceUtils.putString(MainActivity.this, "maxId", null);
                Snackbar.make(view, "Clearing Successful!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 初始化抽屉
     */
    private void initDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string
                .navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

    }

    /**
     * 初始化抽屉视图
     */
    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        View header = navigationView.getHeaderView(0);

        //        LinearLayout header = (LinearLayout) navigationView.findViewById(R.id.draw_header);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }


    /**
     * 初始化菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 初始化菜单选择项
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * 抽屉视图对象的点击事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this, CommonActivity.class);
            intent.putExtra("newPage", R.id.nav_gallery);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onListFragmentInteraction(ScheduleData.ScheduleItem item) {
        mFab.setVisibility(View.VISIBLE);
    }


    @Override
    public void onListFragmentInteraction(OfficialDocumentData.OfficialDocumentItem item) {
        mFab.setVisibility(View.VISIBLE);
    }

}
