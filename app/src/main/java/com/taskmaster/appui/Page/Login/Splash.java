package com.taskmaster.appui.Page.Login;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.taskmaster.appui.Page.Main.ParentQuestView;
import com.taskmaster.appui.Page.Main.User;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.R;

public class Splash extends AppCompatActivity {

    // var
    Animation pop_out_Anim, fade_in_Anim;
    ImageView bg, logo, logo_shadow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.splash);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            User newUser = User.getInstance();
            newUser.setUser(user);
            newUser.loadDocumentSnapshot(documentSnapshot -> {
                NavUtil.instantNavigation(Splash.this, ParentQuestView.class);
            });
        }
        else{
            NavUtil.instantNavigation(Splash.this, UserLogin.class);
        }
    }
}