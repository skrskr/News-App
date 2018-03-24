package com.mohamed.newsapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mohamed.newsapp.R;
import com.mohamed.newsapp.Utils;
import com.mohamed.newsapp.adapter.NewsPageAdapter;
import com.mohamed.newsapp.widget.WidgetUpdateService;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsActivity extends AppCompatActivity {

    private static final int HALF_HOUR = 1800;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.adView)
    AdView adView;
    private NewsPageAdapter newsPageAdapter;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        if(Utils.isOnline(this)) {
            if(!getResources().getBoolean(R.bool.is_landscape))
                setUpAdmob();

            setUpSwipeFragments();
        }else{
            showNoInternetDialog();
        }
     
    }

    private void setUpSwipeFragments() {
        newsPageAdapter = new NewsPageAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(newsPageAdapter);
        tabLayout.setupWithViewPager(viewPager,true);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void setUpAdmob() {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (Utils.isFirstTime(this)) {
            FirebaseJobDispatcher jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
            Job job = jobDispatcher.newJobBuilder()
                    .setService(WidgetUpdateService.class)
                    .setLifetime(Lifetime.FOREVER)
                    .setRecurring(true)
                    .setTag("update_widget")
                    .setTrigger(Trigger.executionWindow(0, HALF_HOUR))
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
            jobDispatcher.mustSchedule(job);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_show_favorites:
                Intent intent = new Intent(this,FavoritesActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showNoInternetDialog(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.dialog_title_error)
                .setMessage(R.string.dialog_msg_error)
                .setPositiveButton(getResources().getString(R.string.dialog_ok_button), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }
}
