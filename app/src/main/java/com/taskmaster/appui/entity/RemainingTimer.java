package com.taskmaster.appui.entity;

import static android.view.View.GONE;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.Duration;
import java.time.ZonedDateTime;

public class RemainingTimer {

    long day, hour, minute, second;
    TextView timeTextView;
    ZonedDateTime due;
    String format;

    Boolean pastDue = false;

    public RemainingTimer(TextView timeTextView, ZonedDateTime due, String format) {
        this.timeTextView = timeTextView;
        this.due = due;
        this.format = format;
    }

    public String getRemainingTime () {
        ZonedDateTime now = DateTimeUtil.getDateTimeNow();
        Duration remaining = Duration.between(now, due);

        if (remaining.isNegative() || remaining.isZero()) {
            pastDue = true;
        }

        this.second = remaining.toSeconds() % 60;
        this.minute = remaining.toMinutes() % 60;
        this.hour = remaining.toHours() % 60;
        this.day = remaining.toDays();

        return format
                .replace("ss",second>9?""+second:"0"+second)
                .replace("mm",minute>9?""+minute:"0"+minute)
                .replace("hh",hour>9?""+hour:"0"+hour)
                .replace("dd",day>9?""+day:"0"+day)
                .replace("s",""+second)
                .replace("m",""+minute)
                .replace("h",""+hour)
                .replace("d",""+day);
    }

    public void setText (String s) {
        Activity act = (Activity) timeTextView.getContext();
        act.runOnUiThread(() -> {
            timeTextView.setText(s);
        });
    }

    public static String toRemainingDuration (Duration d) {
        long rem = d.toSeconds();
        return (rem/3600) + ":" + (((rem/60)%60>9)?(rem/60)%60:"0"+(rem/60)%60) + ":" + ((rem % 60>9)?rem % 60:"0"+rem % 60);
    }


}
