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
    Quest quest;
    ZonedDateTime due;
    String format;

    Boolean pastDue = false;

    public RemainingTimer(Quest quest, ZonedDateTime due, String format) {
        this.quest = quest;
        this.due = due;
        this.format = format;
    }

    public String getRemainingTime () {
        ZonedDateTime now = DateTimeUtil.getDateTimeNow();
        Duration remaining = Duration.between(now, due);

        if (remaining.isNegative() || remaining.isZero()) {
            pastDue = true;
            return "00:00:00:00";
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
        Activity act = (Activity) quest.getContext();
        act.runOnUiThread(() -> {
            quest.getQuestBox().getViewQuestTimeRemaining().setText(s);
            quest.getQuestBoxPreview().getPreviewQuestTimeRemaining().setText(s);
        });
    }

    public static String toRemainingDuration (Duration remaining, String format) {

        long second = remaining.toSeconds() % 60;
        long minute = remaining.toMinutes() % 60;
        long hour = remaining.toHours() % 60;
        long day = remaining.toDays();

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

    public Boolean isPastDue() {
        return pastDue;
    }

    public Quest getQuest() {
        return quest;
    }
}
