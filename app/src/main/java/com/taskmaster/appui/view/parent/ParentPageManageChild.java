package com.taskmaster.appui.view.parent;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.R;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;
import com.taskmaster.appui.view.uimodule.ChildCreationTab;

public class ParentPageManageChild extends ParentPage {

    ChildManager childManager;
    ImageView createChildButton;
    ChildCreationTab childCreationTabPopUp;
    LinearLayout childCont;
    View blurOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_parent_view_manage_child);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cvlb_container), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initNavigationMenu(this, ParentPageManageChild.class);

        childCreationTabPopUp = findViewById(R.id.childCreationPopUp);
        blurOverlay = findViewById(R.id.blurOverlayManageChild);

        childManager = new ChildManager(getApplicationContext());

        // Initialize createChildButton
        createChildButton = topBar.getCreateObjectButton();
        createChildButton.setOnClickListener(v -> {
            if (childCreationTabPopUp.getVisibility() == GONE) {
                blurOverlay.setVisibility(View.VISIBLE);
                blurOverlay.bringToFront();
                childCreationTabPopUp.bringToFront();
                childCreationTabPopUp.setVisibility(VISIBLE);
            } else {
                childCreationTabPopUp.setVisibility(GONE);
            }
        });

        childCreationTabPopUp.getChildCreationConfirmButton().setOnClickListener(v -> {
            childCreationTabPopUp.setVisibility(GONE);
            String username = childCreationTabPopUp.getChildCreationUsername().getText().toString();
            String email = childCreationTabPopUp.getChildCreationEmail().getText().toString();
            String password = childCreationTabPopUp.getChildCreationPassword().getText().toString();
            //String firstname = childCreationTabPopUp.getChildCreationFirstname().getText().toString();
            //String lastname = childCreationTabPopUp.getChildCreationLastName().getText().toString();

            CurrentUser parent = CurrentUser.getInstance();
            String parentID = parent.getFirebaseUser().getUid();
            DocumentReference parentReference = parent.getUserData().getUserSnapshot().getReference();

            Child c = new Child(ChildData.newEmptyChildData());
            c.getChildData().setUsername(username);
            c.getChildData().setEmail(email);
            c.getChildData().setPassword(password);
            c.getChildData().setParentUID(parentID);
            c.getChildData().setParentReference(parentReference);

            childManager.addChild(c);
            TemporaryConnectionManager.startTempConnection(this);
            TemporaryConnectionManager.uploadChild(c)   ;
        });

        childCreationTabPopUp.getChildCreationExitButton().setOnClickListener(v -> {
            childCreationTabPopUp.setVisibility(GONE);
            blurOverlay.setVisibility(View.GONE);
        });

        childCont = findViewById(R.id.childCont);
        childManager.loadChildrenFromFirestore(childCont);
    }
}