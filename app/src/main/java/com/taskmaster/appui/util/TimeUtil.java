package com.taskmaster.appui.util;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.taskmaster.appui.entity.User;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class TimeUtil {
    private CountDownTimer countDownTimer;
    private static TimeUtil instance;
    private final User user = User.getInstance();
    private long baseElapsed,msTimeSnapshot,duration,timeSnapshot,bossTimer;

    public interface TimerCaller {
        void onTimeReceived(Long receivedDate);
    }
    public static TimeUtil getInstance() {
        if (instance == null) {
            instance = new TimeUtil();
        }
        return instance;
    }

    public interface TimerListener {
        void onTick(long timeRemaining);
        void onFinish();
    }

    public static void fetchTime(TimerCaller callback) {
        new Thread(() -> { //Technically, a proper app would have a server which has time running. This just imports time from google.com
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://www.google.com");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD");
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(20000);
                connection.connect();
                long checkConnection = connection.getDate();
                if (checkConnection > 0) { //0 means nothing was sent. TRUE means something was sent
                    callback.onTimeReceived(checkConnection);
                } else { //Fallback on system current time (the one that can be cheated by changing time on phone)
                    Log.e("TimeUtil", "Failed to fetch time, using system time.");
                    callback.onTimeReceived(System.currentTimeMillis());
                }
            } catch (Exception e) {
                Log.e("TimeUtil", "Failed to fetch time, using system time.");
                callback.onTimeReceived(System.currentTimeMillis());
            } finally { //Apparently this is good practice
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }

    public void setupTimer(GenericCallback callback) { //Call this onFinish
        long weekMs = TimeUnit.DAYS.toMillis(7);
        fetchTime(receivedDate -> {
            bossTimer = receivedDate + weekMs;
            user.getDocumentSnapshot().getReference().update("BossTimer", bossTimer);
            user.loadDocumentSnapshot(callback1-> callback.onCallback(null));
        });
    }

    public void startTimer(TimerListener tickListener) { //Called whenever WeeklyBoss is opened
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        fetchTime(receivedDate -> {
            timeSnapshot = receivedDate;

            //just so getTime isn't called perTick
            bossTimer = user.getDocumentSnapshot().getLong("BossTimer");
            msTimeSnapshot = timeSnapshot;
            duration = bossTimer - msTimeSnapshot;

            if (duration <= 0) {
                new Handler(Looper.getMainLooper()).post(tickListener::onFinish);
                return;
            }
            //Elapsed time so fetchTime is only called once
            //Captures time, compares that to the time onTick to calculate elapsed time
            baseElapsed = android.os.SystemClock.elapsedRealtime();

            //Handler and Looper is the work of Dark Arts, apparently needed because fetchTime runs on BG thread
            new Handler(Looper.getMainLooper()).post(() -> {
                countDownTimer = new CountDownTimer(duration, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // Use millisUntilFinished directly or compute via elapsedRealtime:
                        long elapsed = android.os.SystemClock.elapsedRealtime() - baseElapsed;
                        long remaining = duration - elapsed;
                        if (remaining < 0) remaining = 0;
                        tickListener.onTick(remaining);
                    }
                    @Override
                    public void onFinish() {
                        tickListener.onFinish();
                    }
                }.start();
            });
        });
    }
}
