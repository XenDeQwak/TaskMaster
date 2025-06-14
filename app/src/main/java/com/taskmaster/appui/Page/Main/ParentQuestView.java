package com.taskmaster.appui.Page.Main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.QuestManager;

public class ParentQuestView extends AppCompatActivity {

    QuestManager questManager;

    ImageView NavBarButton, CreateQuestButton;

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

        NavBarButton = findViewById(R.id.NavBarButton);
        CreateQuestButton = findViewById(R.id.CreateQuestButton);

        NavBarButton.setOnClickListener(view -> {
            LinearLayout dropdownContainer = findViewById(R.id.dropdownContainer);
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