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
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class RemainingTimer {

    long day, hour, minute, second;
    ZonedDateTime due;
    String format;
    Boolean pastDue = false;

    private Consumer<RemainingTimer> onTick;
    private Consumer<RemainingTimer>  onFinish;

    public RemainingTimer(ZonedDateTime due, String format) {
        this.due = due;
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RemainingTimer)) return false;
        RemainingTimer that = (RemainingTimer) o;
        return Objects.equals(due, that.due);
    }

    public Boolean isPastDue() {
        return pastDue;
    }

    public void setPastDue(Boolean pastDue) {
        this.pastDue = pastDue;
    }

    public void setOnTick (Consumer<RemainingTimer> onTick) {
        this.onTick = onTick;
    }

    public void setOnFinish (Consumer<RemainingTimer> onFinish) {
        this.onFinish = onFinish;
    }

    public void onTick () {
        if (onTick != null) onTick.accept(this);
    }

    public void onFinish() {
        if (onFinish != null) onFinish.accept(this);
    }
}
