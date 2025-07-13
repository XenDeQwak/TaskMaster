package com.taskmaster.appui.util;

import android.app.Activity;

import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DateTimeUtil {

    private static ArrayList<RemainingTimer> timerList = new ArrayList<>();
    private static ScheduledExecutorService timerExecutorService;

    public static ZonedDateTime getDateTimeNow () {
        return ZonedDateTime.now(ZoneId.of("UTC"));
    }

    public static ZonedDateTime getDateTimeFromNow(long hour, long minute, long second) {
        return getDateTimeNow().plusHours(hour).plusMinutes(minute).plusSeconds(second);
    }

    public static ZonedDateTime getDateTimeFromEpochSecond (long epochSecond) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault());
    }

    public static void addTimer (RemainingTimer timer) {
        if (!timerList.contains(timer)) timerList.add(timer);
    }

    public static void clearTimerList () {
        timerList.clear();
    }

    public static void startTimer() {
        timerExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable timerTick = () -> {
            Iterator<RemainingTimer> it = timerList.iterator();
            while (it.hasNext()) {
                RemainingTimer rt = it.next();
                String remTime = rt.getRemainingTime();
                if (rt.isPastDue()) {
                    Quest q = rt.getQuest();
                    q.getQuestData().setStatus("awaiting reason for failure");
                    q.getQuestData().uploadData();
                    it.remove();
                } else {
                    rt.setText(remTime);
                }
            }
        };

        timerExecutorService.scheduleWithFixedDelay(timerTick, 0, 1, TimeUnit.SECONDS);
    }

}
