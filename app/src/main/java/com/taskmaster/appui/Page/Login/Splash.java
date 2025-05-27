package com.taskmaster.appui.Page.Login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.Services.NavUtil;
import com.taskmaster.appui.R;

public class Splash extends AppCompatActivity {

    private static int SPLASH_SCREEN = 3000;

    // var
    Animation pop_out_Anim, fade_in_Anim;
    ImageView bg, logo, logo_shadow;

    Intent LoginIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash);

        // init intent
        LoginIntent = new Intent(Splash.this, UserLogin.class);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // anim
        pop_out_Anim = AnimationUtils.loadAnimation(this, R.anim.pop_out_animation);
        fade_in_Anim = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation);


        // hooks
        bg = findViewById(R.id.imageView4);
        logo = findViewById(R.id.imageView);
        logo_shadow = findViewById(R.id.imageView2);

        // set anim
        bg.setAnimation(fade_in_Anim);
        logo.setAnimation(pop_out_Anim);
        logo_shadow.setAnimation(pop_out_Anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(LoginIntent);
                finish();
            }
        }, SPLASH_SCREEN);
    }
}