package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

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

    void initNavigationMenu(Activity activity) {

        dropdownNavMenu = findViewById(R.id.dropdownNavMenu);
        dropdownNavMenu.attachActivity(this);

    }

}
