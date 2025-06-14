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
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.entity.enums.Stats;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.HashMap;

public class ParentQuestView extends AppCompatActivity {

    QuestManager questManager;
    ImageView NavBarButton, CreateQuestButton;
    Button navQueueButton, navAdventurersButton, navLogOutButton;

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

        // Initialize Nav Buttons
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
        NavBarButton.setOnClickListener(v -> {
            FrameLayout dropdownContainer = findViewById(R.id.dropdownContainer);
            if (dropdownContainer.getVisibility() == View.VISIBLE ) {
                dropdownContainer.setVisibility(View.GONE);
            } else if (dropdownContainer.getVisibility() == View.GONE) {
                dropdownContainer.setVisibility(View.VISIBLE);
            }
        });


        // Create Quest
        CreateQuestButton.setOnClickListener(v -> {
            HashMap<String, Object> questData = new HashMap<>();
            questData.put("Name", "testquest");
            questData.put("Description", "I am a test quest");
            questData.put("StartDate", 2025000000000L);
            questData.put("DeadlineDate", 202536586340L);
            questData.put("RewardStat", Stats.STRENGTH);
            questData.put("CreatorReference", FirestoreManager.getFirestore().collection("Users").document("test"));
            questData.put("CreatorUID", "test");
            Quest q = QuestManager.parseQuestData(questData);
            questManager.addQuest(q);
            FirestoreManager.uploadQuest(AuthManager.getAuth().getUid(), q);
        });






        questManager = new QuestManager();
        questManager.loadQuestsFromFirestore();
    }
}