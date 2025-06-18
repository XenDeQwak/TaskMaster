package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;

public class ChildCreationTab extends FrameLayout {


    EditText childCreationUsername, childCreationEmail, childCreationPassword, childCreationFirstname, childCreationLastName;
    Button childCreationConfirmButton, childCreationExitButton;

    public ChildCreationTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_child_creation_tab, this);

        ConstraintLayout childCreationContainer = findViewById(R.id.childCreationContainer);
        childCreationContainer.getBackground().setAlpha(200);

        childCreationUsername = findViewById(R.id.childCreationUsername);
        childCreationEmail = findViewById(R.id.childCreationEmail);
        childCreationPassword = findViewById(R.id.childCreationPassword);
        childCreationFirstname = findViewById(R.id.childCreationFirstname);
        childCreationLastName = findViewById(R.id.childCreationLastName);

        childCreationConfirmButton = findViewById(R.id.childCreationConfirmButton);
        childCreationExitButton = findViewById(R.id.childCreationExitButton);
    }

    public EditText getChildCreationUsername() {
        return childCreationUsername;
    }

    public EditText getChildCreationEmail() {
        return childCreationEmail;
    }

    public EditText getChildCreationPassword() {
        return childCreationPassword;
    }

    public EditText getChildCreationFirstname() {
        return childCreationFirstname;
    }

    public EditText getChildCreationLastName() {
        return childCreationLastName;
    }

    public Button getChildCreationConfirmButton() {
        return childCreationConfirmButton;
    }

    public Button getChildCreationExitButton() {
        return childCreationExitButton;
    }
}
