package com.taskmaster.appui.util;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class NavUtil {
    public static void setNavigation(Activity activity, View view, final Class<?> targetActivity) {
        view.setOnClickListener(v -> {
            Intent intent = new Intent(activity, targetActivity);
            activity.startActivity(intent);
            activity.finish();
        });
    }

    public static void instantNavigation(Activity origin, final Class<?> destination) {
        Intent intent = new Intent(origin, destination);
        origin.startActivity(intent);
        origin.finish();
    }

    public static void hideSystemBars(Activity window) {

    }
}

