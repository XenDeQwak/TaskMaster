package com.taskmaster.appui.Services;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class NavUtil {
    public static void setNavigation(Activity activity, View view, final Class<?> targetActivity) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, targetActivity);
                activity.startActivity(intent);
                activity.finish();
            }
        });
    }
    public static void instantNavigation(Activity activity, final Class<?> targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        activity.startActivity(intent);
        activity.finish();
    }
}

