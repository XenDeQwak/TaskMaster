package com.taskmaster.appui.view.login;

import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.AuthUserData;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;
import com.taskmaster.appui.view.child.ChildPageQuestBoard;
import com.taskmaster.appui.view.parent.ParentPageQuestBoard;
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

        // Quest Timer
        DateTimeUtil.startTimer();

        //FirebaseAuth.getInstance().signOut();
        FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fUser == null) {
            goTo(UserLogin.class);
        } else if (fUser.isAnonymous()) {
                fUser.delete();
                goTo(UserLogin.class);
        } else if (fUser != null) {
            fUser.reload().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Debug", "CurrentUser still exists.");
                    // CurrentUser still exists
                    FirebaseUser fnewUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (fnewUser != null) {
                        // CurrentUser is still signed-in
                        CurrentUser user = CurrentUser.getInstance();
                        user.setFirebaseUser(fnewUser);
                        user.setUserData(new AuthUserData());
                        FirestoreManager.getUserInformation(fnewUser.getUid(), ds -> {
                            user.getUserData().setUserSnapshot(ds, e -> {
                                String role = user.getUserData().getRole();
                                //System.out.println(ds);
                                //System.out.println(role);
                                if (role.equalsIgnoreCase("parent")) {
                                    goTo(ParentPageQuestBoard.class);
                                } else if (role.equalsIgnoreCase("child")) {
                                    goTo(ChildPageQuestBoard.class);
                                }
                            });
                        });
                    } else {
                        // CurrentUser is signed-out
                        Log.d("Debug", "CurrentUser no longer exists.");
                        FirebaseAuth.getInstance().signOut();
                        goTo(UserLogin.class);
                    }
                } else {
                    goTo(UserLogin.class); // Safe fallback
                }
            });
        }

    }

    private void goTo (Class<?> dest) {
        NavUtil.instantNavigation(Splash.this, dest);
    }
}