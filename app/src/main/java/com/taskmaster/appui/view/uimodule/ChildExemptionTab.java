package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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

    public void setQuest (Quest q, GenericCallback callback) {
        excuseTabDescription.setText("");
        this.q = q;

        excuseTabCancel.setOnClickListener(v -> this.setVisibility(GONE));

        excuseTabSubmit.setOnClickListener(v -> {
            q.setStatus("Awaiting Exemption");
            q.setReason(excuseTabDescription.getText().toString());
            FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                    .set(QuestManager.packQuestData(q))
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("Debug", "Successfully submitted exemption reason");
                        } else {
                            Log.d("Debug", "Failed to submit exemption reason");
                        }
                    });
            this.setVisibility(GONE);
            callback.onCallback(null);
        });
    }
}
