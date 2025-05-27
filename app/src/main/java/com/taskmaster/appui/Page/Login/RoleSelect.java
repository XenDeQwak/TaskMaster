package com.taskmaster.appui.Page.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.taskmaster.appui.Page.NavUtil;
import com.taskmaster.appui.R;

public class RoleSelect extends AppCompatActivity {

    AppCompatButton parentButton, childButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.role_select);

        // hide status bar and nav bar
        NavUtil.hideSystemBars(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        parentButton = findViewById(R.id.button2);
        childButton = findViewById(R.id.confirmButton);

        // test for button
        parentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleSelect.this, ParentLogin.class);
                startActivity(intent);
            }
        });

        // test for button
        childButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoleSelect.this, ChildLogin.class);
                startActivity(intent);
            }
        });
    }


}