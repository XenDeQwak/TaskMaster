package com.taskmaster.appui.Page;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

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

    public static void instantNavigation(Activity origin, final Class<?> destination) {
        Intent intent = new Intent(origin, destination);
        origin.startActivity(intent);
        origin.finish();
    }

    public static void hideSystemBars(Activity window) {
        // hide status bar
        window.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the nav bar
        window.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}

