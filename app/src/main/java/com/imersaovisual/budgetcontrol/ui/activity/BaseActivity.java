package com.imersaovisual.budgetcontrol.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.imersaovisual.budgetcontrol.R;
import com.imersaovisual.budgetcontrol.ui.helper.ActionBarHelper;

/**
 * Created by LuizCarlos on 03/12/2014.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected DrawerLayout appDrawerLayout;
    protected ListView sideBarListView;

    protected ActionBarHelper actionBarHelper;

    protected ActionBarDrawerToggle drawerToggle;

    public abstract int getLayoutId();

    public abstract int getCurrentPosition();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        appDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        sideBarListView = (ListView) findViewById(R.id.start_drawer);

        appDrawerLayout.setDrawerListener(new DemoDrawerListener());

        appDrawerLayout.setDrawerTitle(GravityCompat.START, "Navigation");

        String home = getResources().getString(R.string.sidebar_home);
        String query = getResources().getString(R.string.sidebar_query);
        String settings = getResources().getString(R.string.sidebar_settings);

        ListAdapter itemsAdapter = new ArrayAdapter<String>(this,
                R.layout.my_list_view_layout, new String[]{home, query, settings});

        sideBarListView.setAdapter(itemsAdapter);

        sideBarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == getCurrentPosition()) {
                    appDrawerLayout.closeDrawer(sideBarListView);
                    return;

                }
                if (position == 0) {
                    Intent intent = new Intent(BaseActivity.this, MainActivity.class);

                    startActivity(intent);

                    finish();
                } else if (position == 1) {
                    Intent intent = new Intent(BaseActivity.this, QueryActivity.class);

                    startActivity(intent);

                    finish();
                } else if (position == 2) {

                }
                appDrawerLayout.closeDrawer(sideBarListView);
            }
        });

        actionBarHelper = new ActionBarHelper(this);

        drawerToggle = new ActionBarDrawerToggle(this, appDrawerLayout,
                R.string.drawer_open, R.string.drawer_close);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         * The action bar home/up action should open or close the drawer.
		 * mDrawerToggle will take care of this.
		 */
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DemoDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            drawerToggle.onDrawerOpened(drawerView);
            actionBarHelper.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            drawerToggle.onDrawerClosed(drawerView);
            actionBarHelper.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            drawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            drawerToggle.onDrawerStateChanged(newState);
        }
    }
}
