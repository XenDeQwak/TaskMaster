package com.taskmaster.appui.Page.Main;

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

import com.google.firebase.auth.FirebaseAuth;
import com.taskmaster.appui.Page.Login.Splash;
import com.taskmaster.appui.Page.ManageChild;
import com.taskmaster.appui.R;
import com.taskmaster.appui.Services.NavUtil;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

public class ParentQuestView extends AppCompatActivity {

    QuestManager questManager;

    ImageView NavBarButton, CreateQuestButton;

    // Dropdown buttons
     Button navQueueButton;
     Button navAdventurersButton;
     Button navLogOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_quest_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize top bar buttons
        NavBarButton = findViewById(R.id.NavBarButton);
        CreateQuestButton = findViewById(R.id.CreateQuestButton);

        navQueueButton  = findViewById(R.id.navQueueButton);
        navAdventurersButton  = findViewById(R.id.navAdventurersButton);
        navLogOutButton = findViewById(R.id.navLogOutButton);

        NavUtil.setNavigation(this, navQueueButton, ParentQuestView.class);
        NavUtil.setNavigation(this, navAdventurersButton, ManageChild.class);
        navLogOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            NavUtil.instantNavigation(this, Splash.class);
        });

        // Dropdown menu logic
        NavBarButton.setOnClickListener(view -> {
            FrameLayout dropdownContainer = findViewById(R.id.dropdownContainer);
            if (dropdownContainer.getVisibility() == View.VISIBLE ) {
                dropdownContainer.setVisibility(View.GONE);
            } else if (dropdownContainer.getVisibility() == View.GONE) {
                dropdownContainer.setVisibility(View.VISIBLE);
            }
        });



        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();
    }
}