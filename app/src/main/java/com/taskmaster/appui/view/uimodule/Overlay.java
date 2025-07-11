package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taskmaster.appui.R;

public class Overlay extends FrameLayout {

    ImageView blkOverlay;

    public Overlay(@NonNull Context context) {
        super(context);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.overlay, this);

        blkOverlay = findViewById(R.id.blkOverlay);
        blkOverlay.setScaleType(ImageView.ScaleType.FIT_XY);

    }
}
