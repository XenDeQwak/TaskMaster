package com.taskmaster.appui.entity;

import android.app.Activity;
import android.widget.TextView;

import com.taskmaster.appui.util.DateTimeUtil;

import java.time.Duration;
import java.time.ZonedDateTime;

public class RemainingTimer {

    long hour, minute, second;
    TextView timeTextView;
    ZonedDateTime due;

    public RemainingTimer(TextView timeTextView, ZonedDateTime due) {
        this.timeTextView = timeTextView;
        this.due = due;
    }

    public String getRemainingTime () {
        ZonedDateTime now = DateTimeUtil.getDateTimeNow();
        Duration remaining = Duration.between(now, due);
        long rem = remaining.toSeconds();

        if (rem < 0) rem = 0;

        this.second = rem % 60;
        this.minute = (rem/60)%60;
        this.hour = rem/3600;

        return hour + ":" + ((minute>9)?minute:"0"+minute) + ":" + ((second>9)?second:"0"+second);
    }

    public void setText (String s) {
        Activity act = (Activity) timeTextView.getContext();
        act.runOnUiThread(() -> {timeTextView.setText(s);});
    }
}
