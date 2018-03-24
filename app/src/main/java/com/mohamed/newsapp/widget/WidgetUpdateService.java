package com.mohamed.newsapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.RemoteViews;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.mohamed.newsapp.R;

/**
 * Created by mohamed on 14/03/18.
 */

public class WidgetUpdateService extends JobService {
    public static final String WIDGET_UPDATE_SERVICE = "widget_update_service";

    @Override
    public boolean onStartJob(JobParameters job) {
        RemoteViews views = new RemoteViews(this.getPackageName(), R.layout.news_widget);
        Intent intent = new Intent(this, WidgetService.class);
        intent.putExtra(WIDGET_UPDATE_SERVICE,true);
        views.setRemoteAdapter(R.id.widget_list, intent);
        ComponentName theWidget = new ComponentName(this, NewsWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(theWidget, views);

        jobFinished(job, false);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return true;
    }
}
