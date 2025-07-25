package com.taskmaster.appui.view.uimodule;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.GenericCallback;

public class ChildExemptionTab extends FrameLayout {

    Quest q;
    EditText excuseTabDescription;
    Button excuseTabCancel, excuseTabSubmit;

    public ChildExemptionTab(@NonNull Context context) {
        super(context);
        init();
    }

    public ChildExemptionTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_child_exemption_tab, this);

        excuseTabDescription = findViewById(R.id.excuseTabDescription);
        excuseTabCancel = findViewById(R.id.excuseTabCancel);
        excuseTabSubmit = findViewById(R.id.excuseTabSubmit);
    }

    public void setQuest (Quest q) {
        excuseTabDescription.setText("");
        this.q = q;

        excuseTabCancel.setOnClickListener(v -> {
            ViewGroup parent = (ViewGroup) this.getParent();
            parent.removeView(this);
        });

        excuseTabSubmit.setOnClickListener(v -> {
            q.getQuestData().setStatus("Awaiting Exemption");
            q.getQuestData().setFailureReason(excuseTabDescription.getText().toString());
            q.getQuestData().uploadData();

            ViewGroup parent = (ViewGroup) this.getParent();
            parent.removeView(this);
        });
    }
}
