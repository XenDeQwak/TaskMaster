package com.taskmaster.appui.view.child;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.manager.entitymanager.ChildManager;

public class ChildViewLeaderboard extends ChildView {

    ChildManager childManager;

    ScrollView cvlb_scrollView;
    LinearLayout cvlb_scrollContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_child_view_leaderboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cvlb_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        childManager = new ChildManager(this);

        initNavigationMenu(this, ChildViewLeaderboard.class);

        cvlb_scrollView = findViewById(R.id.cvlb_scrollView);
        cvlb_scrollContent = findViewById(R.id.cvlb_scrollContent);

        cvlb_scrollView.getBackground().setAlpha(150);

        childManager.loadChildrenFromFirestore(cvlb_scrollContent);
    }
}