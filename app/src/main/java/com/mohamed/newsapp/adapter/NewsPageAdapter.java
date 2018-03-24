package com.mohamed.newsapp.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mohamed.newsapp.CategoryFragment;
import com.mohamed.newsapp.R;

/**
 * Created by mohamed on 10/03/18.
 */

public class NewsPageAdapter extends FragmentStatePagerAdapter {

    private String [] tabTitles=null;
    public NewsPageAdapter(FragmentManager fm,Context context) {
        super(fm);
        tabTitles = context.getResources().getStringArray(R.array.categories_tab);
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(tabTitles[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
