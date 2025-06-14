package com.taskmaster.appui.view.child;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.taskmaster.appui.R;
import com.taskmaster.appui.util.TimeUtil;

public class CosmeticShop extends AppCompatActivity {

    TextView countdownBox;
    TimeUtil timeUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Setup
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cosmetic_shop);

        countdownBox = findViewById(R.id.cosmeticShopContainerItemContainerItemPictureText2);
        timeUtil = TimeUtil.getInstance();
        timeUtil.setupTimer();
        timeUtil.startTimer(new TimeUtil.TimerListener() {
            @Override
            public void onTick(long timeRemaining) {
                setTime(timeRemaining);
            }

            @Override
            public void onFinish() {
                //Whatever
            }
        });
    }
    public void setTime(long ms){
        Long hours = ms / 3600000;
        Long minutes = (ms % 3600000) / 60000;
        Long seconds = ((ms % 3600000) % 60000) / 1000;
        String timeFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        runOnUiThread(() -> countdownBox.setText(timeFormatted));
    }
}
