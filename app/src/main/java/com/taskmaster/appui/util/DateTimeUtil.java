package com.taskmaster.appui.util;

import android.app.Activity;

import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;

import java.time.Duration;
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

    public static void addTimer (RemainingTimer... timer) {
        for (RemainingTimer remainingTimer : timer) {
            if (!timerList.contains(remainingTimer)) {
                timerList.add(remainingTimer);
            } else {
                timerList.remove(remainingTimer);
                timerList.add(remainingTimer);
            }
        }
    }

    public static void clearTimerList () {
        timerList.clear();
    }

    public static void startTimer() {
        timerExecutorService = Executors.newSingleThreadScheduledExecutor();
        Runnable timerTick = () -> {
            //System.out.println(timerList);
            Iterator<RemainingTimer> it = timerList.iterator();
            while (it.hasNext()) {
                RemainingTimer rt = it.next();
                rt.onTick();
                if (rt.isPastDue()) {
                    rt.onFinish();
                    it.remove();
                }
            }
        };
        timerExecutorService.scheduleWithFixedDelay(timerTick, 0, 1, TimeUnit.SECONDS);
    }

    public static String formatDuration (Duration d, String format) {
        return format
                .replace("DD", Long.toString(d.toDays()))
                .replace("HHH",(d.toHours()%24>9?"":"0")+(d.toHours() % 24))
                .replace("MMM", (d.toMinutes()%60>9?"":"0")+(d.toMinutes() % 60))
                .replace("SSS", (d.toSeconds()%60>9?"":"0")+(d.toSeconds() % 60))
                .replace("HH",Long.toString(d.toHours()%1000))
                .replace("MM", Long.toString(d.toMinutes()%60))
                .replace("SS", Long.toString(d.toSeconds()%60));
    }

}
