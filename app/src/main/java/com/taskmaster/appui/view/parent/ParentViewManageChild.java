package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import java.util.List;

public class ParentViewManageChild extends ParentView {

    ChildManager childManager;
    ImageView createChildButton;
    ChildCreationTab childCreationTabPopUp;
    LinearLayout childCont;

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
        ChildCreationTab childCreationContainer = findViewById(R.id.childCreationPopUp);

        childManager = new ChildManager(getApplicationContext());

        // Initialize createChildButton
        createChildButton = topBar.getCreateObjectButton();
        createChildButton.setOnClickListener(v -> {
//            //System.out.println("I AM PRESSED IN PARENTVIEWMANAGECHILD");
//            Child c = ChildManager.createTestChild();
//            childManager.addChild(c);
//            TemporaryConnectionManager.startTempConnection(getApplicationContext());
//            TemporaryConnectionManager.uploadChild(c);
            if (childCreationContainer.getVisibility() == GONE) {
                childCreationContainer.bringToFront();
                childCreationContainer.setVisibility(VISIBLE);
            } else {
                childCreationContainer.setVisibility(GONE);
            }
        });

        childCreationTabPopUp.getChildCreationConfirmButton().setOnClickListener(v -> {
            User user = User.getInstance();
            childCreationContainer.setVisibility(GONE);
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
                    user.getDocumentSnapshot().getReference(),
                    0,
                    0,
                    0,
                    0L,
                    true,
                    1,
                    0,
                    List.of("Armorless")
                    );

            childManager.addChild(c);
            TemporaryConnectionManager.startTempConnection(this);
            TemporaryConnectionManager.uploadChild(c)   ;
        });

        childCreationTabPopUp.getChildCreationExitButton().setOnClickListener(v -> {
            childCreationContainer.setVisibility(GONE);
        });

        childCont = findViewById(R.id.childCont);
        childManager.loadChildrenFromFirestore(childCont);
    }
}