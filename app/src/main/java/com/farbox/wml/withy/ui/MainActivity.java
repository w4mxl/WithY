package com.farbox.wml.withy.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.MaterialMenuDrawable.Stroke;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconCompat;
import com.farbox.wml.withy.R;
import com.farbox.wml.withy.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by wml on 14/11/26
 */
public class MainActivity extends ActionBarActivity {

    private MaterialMenuIconCompat materialMenu;
    private boolean isDrawerOpened;

    private int[] mNavIcon = new int[]{R.drawable.ic_assignment_grey,R.drawable.ic_assignment_turned_in_grey,R.drawable.ic_assignment_late_grey};
    private String[] mNavTitles;
    private ArrayList<Integer> mNavCount = new ArrayList<Integer>();//left list item's each count

    /*定义一个动态数组*/
    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    private DrawerLayout mDrawerLayout;
    private LinearLayout mDrawerLl;
    private ListView mDrawerList;
    private LinearLayout mAbout;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    SimpleAdapter mSimpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        materialMenu = new MaterialMenuIconCompat(this, Color.WHITE, Stroke.THIN);

        mTitle = mDrawerTitle = getTitle();

        mNavTitles = getResources().getStringArray(R.array.nav_array);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        isDrawerOpened ? 2 - slideOffset : slideOffset
                );
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                isDrawerOpened = true;

                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

                //更新 left drawer list 中的数字
                setmNavCountValue();
                listItem.clear();
                for (int i = 0; i < 3; i++) {
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("mNavIcon",mNavIcon[i]);
                    map.put("mNavTitle", mNavTitles[i]);
                    map.put("mNavCount", mNavCount.get(i));
                    listItem.add(map);
                }
                mSimpleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                isDrawerOpened = false;

                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE) {
                    if (isDrawerOpened)
                        materialMenu.setState(MaterialMenuDrawable.IconState.ARROW);
                    else
                        materialMenu.setState(MaterialMenuDrawable.IconState.BURGER);
                }
            }

        });

        mDrawerLl = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_ls);
        mAbout = (LinearLayout) findViewById(R.id.ll_about);
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawers();
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });

        // set up the drawer's list view with items and click listener
        setmNavCountValue();

        /*在数组中存放数据*/
        listItem.clear();
        for (int i = 0; i < 3; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("mNavIcon",mNavIcon[i]);
            map.put("mNavTitle", mNavTitles[i]);
            map.put("mNavCount", mNavCount.get(i));
            listItem.add(map);
        }

        mSimpleAdapter = new SimpleAdapter(
                this,
                listItem,//需要绑定的数据
                R.layout.drawer_list_item_with_num,//每一行的布局
                new String[]{"mNavIcon","mNavTitle", "mNavCount"},//动态数组中的数据源的键对应到定义布局的View中
                new int[]{R.id.iv_icon,R.id.text1, R.id.tv_num}
        );


        //new ArrayAdapter<String>(this, R.layout.drawer_list_item_with_num, R.id.text1, mNavTitles)
        mDrawerList.setAdapter(mSimpleAdapter);//simple_list_item_activated_1
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    private void setmNavCountValue() {
        mNavCount.clear();
        mNavCount.add(Constants.allThingsCount);
        mNavCount.add(Constants.thingsFinishedCount);
        mNavCount.add(Constants.thingsUndoCount);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {//1 2 3
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args = new Bundle();
        switch (position) {
            case 0:
                Fragment thingsFragment = new ThingsFragment();
                args.putInt(ThingsFragment.ARG_NAV_NUMBER, position);
                thingsFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, thingsFragment).commit();
                break;
            case 1:
                Fragment finishedFragment = new FinishedFragment();
                args.putInt(ThingsFragment.ARG_NAV_NUMBER, position);
                finishedFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, finishedFragment).commit();
                break;
            case 2:
                Fragment undoFragment = new UndoFragment();
                args.putInt(ThingsFragment.ARG_NAV_NUMBER, position);
                undoFragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, undoFragment).commit();
                break;
        }
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerLl);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        isDrawerOpened = mDrawerLayout.isDrawerOpen(Gravity.START); // or END, LEFT, RIGHT
        materialMenu.syncState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        materialMenu.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                if (isDrawerOpened) {
                    mDrawerLayout.closeDrawers();
                    materialMenu.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
                } else {
                    mDrawerLayout.openDrawer(Gravity.START);
                    materialMenu.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerLl);
        if (drawerOpen) {
            mDrawerLayout.closeDrawer(mDrawerLl);
        } else {
            super.onBackPressed();
        }
    }
}
