package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.User;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;
import com.taskmaster.appui.view.uimodule.ChildCreationTab;

public class ParentViewManageChild extends ParentView {

    ChildManager childManager;
    ImageView createChildButton;
    ChildCreationTab childCreationTabPopUp;
    View childCreationBackdrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_manage_child);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentViewManageChild.class);

        childCreationTabPopUp = findViewById(R.id.childCreationPopUp);
        childCreationBackdrop = findViewById(R.id.childCreationBackdrop);
        createChildButton = topBar.getCreateObjectButton();

        childManager = new ChildManager(getApplicationContext());

        // Toggle popup and backdrop when create button is clicked
        createChildButton.setOnClickListener(v -> {
            //System.out.println("I AM PRESSED IN PARENTVIEWMANAGECHILD");
//            Child c = ChildManager.createTestChild();
//            childManager.addChild(c);
//            TemporaryConnectionManager.startTempConnection(getApplicationContext());
//            TemporaryConnectionManager.uploadChild(c);
            boolean shouldShow = childCreationTabPopUp.getVisibility() == GONE;
            childCreationTabPopUp.setVisibility(shouldShow ? VISIBLE : GONE);
            childCreationBackdrop.setVisibility(shouldShow ? VISIBLE : GONE);
        });


        childCreationTabPopUp.getChildCreationConfirmButton().setOnClickListener(v -> {
            childCreationTabPopUp.setVisibility(GONE);
            childCreationBackdrop.setVisibility(GONE);

            User user = User.getInstance();
            String username = childCreationTabPopUp.getChildCreationUsername().getText().toString();
            String email = childCreationTabPopUp.getChildCreationEmail().getText().toString();
            String password = childCreationTabPopUp.getChildCreationPassword().getText().toString();
            String firstname = childCreationTabPopUp.getChildCreationFirstname().getText().toString();
            String lastname = childCreationTabPopUp.getChildCreationLastName().getText().toString();

            Child c = new Child(
                    email,
                    password,
                    username,
                    firstname,
                    lastname,
                    user.getDocumentSnapshot().getId(),
                    user.getDocumentSnapshot().getReference()
            );

            childManager.addChild(c);
            TemporaryConnectionManager.startTempConnection(this);
            TemporaryConnectionManager.uploadChild(c);
        });

        childCreationTabPopUp.getChildCreationExitButton().setOnClickListener(v -> {
            childCreationTabPopUp.setVisibility(GONE);
            childCreationBackdrop.setVisibility(GONE);
        });

        //Click backdrop also closes popup
        childCreationBackdrop.setOnClickListener(v -> {
            childCreationTabPopUp.setVisibility(GONE);
            childCreationBackdrop.setVisibility(GONE);
        });
    }
}
