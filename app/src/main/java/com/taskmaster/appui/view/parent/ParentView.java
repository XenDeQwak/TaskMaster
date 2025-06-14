package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.view.login.Splash;

public class ParentView extends AppCompatActivity {

    ImageView NavBarButton;
    DropdownNavMenu dropdownNavMenu;

    void initNavigationMenu() {

        NavBarButton = findViewById(R.id.NavBarButton);
        dropdownNavMenu = new DropdownNavMenu(this);
        dropdownNavMenu.attachActivity(this);

        // Dropdown menu logic
        NavBarButton.setOnClickListener(v -> {
            System.out.println("HELLO WORLD");
            DropdownNavMenu nav = findViewById(R.id.dropdownNavMenu);
            if (nav.getVisibility() == GONE) {
                System.out.println("Now you see me");
                nav.setVisibility(VISIBLE);
            } else {
                System.out.println("Now you don't");
                nav.setVisibility(GONE);
            }
        });

    }

}
