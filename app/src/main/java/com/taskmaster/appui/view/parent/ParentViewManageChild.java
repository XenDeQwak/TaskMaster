package com.taskmaster.appui.view.parent;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;

public class ParentViewManageChild extends ParentView {

    ChildManager childManager;
    ImageView createChildButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_manage_child_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentViewManageChild.class);

        childManager = new ChildManager(getApplicationContext());

        // Initialize createChildButton
        createChildButton = topBar.getCreateObjectButton();
        createChildButton.setOnClickListener(v -> {
            //System.out.println("I AM PRESSED IN PARENTVIEWMANAGECHILD");
            Child c = ChildManager.createTestChild();
            childManager.addChild(c);
            TemporaryConnectionManager.startTempConnection(getApplicationContext());
            TemporaryConnectionManager.uploadChild(c);
        });

    }
}