/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class EarthquakeActivity extends AppCompatActivity implements EarthquakeMapFragment.FragmentListener{
    EarthquakeFragment mEarthquakeFragment;
    public SimpleFragmentPagerAdapter mAdapter;

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabView);
        tabLayout.setupWithViewPager(viewPager);
    }

    public Fragment getFragmentAtPos(int pos){
        return mAdapter.getRegisteredFragment(pos);
    }

    //-------------------------------------------------------------------------------------------
    /* Menu Methods */
    //-------------------------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment page = getFragmentAtPos(0);
        mEarthquakeFragment = (EarthquakeFragment) page;

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchButton:
                mEarthquakeFragment.createAndDisplaySearchDialog();
                return true;
            case R.id.refreshButton:
                mEarthquakeFragment.sendNewRequst();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
