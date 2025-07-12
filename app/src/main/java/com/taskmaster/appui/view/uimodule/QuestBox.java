package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class QuestBox extends FrameLayout {

    Quest q;
    String state;
    ConstraintLayout viewQuestContainer;
    ImageView viewQuestAvatar;
    TextView viewQuestName,
            viewQuestDifficulty,
            viewQuestAdventurer,
            viewQuestDescription,
            viewQuestRewardStat,
            viewQuestRewardExtra,
            viewQuestDeadline,
            viewQuestTimeRemaining,
            viewQuestStatus,
            viewQuestReason;
    Button viewQuestButtonA, viewQuestButtonB, viewQuestButtonC, viewQuestButtonD;

    public QuestBox(@NonNull Context context) {
        super(context);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_quest_view, this);

        viewQuestContainer = findViewById(R.id.viewQuestContainer);

        viewQuestAvatar = findViewById(R.id.viewQuestAvatar);

        viewQuestName = findViewById(R.id.viewQuestName);
        viewQuestDifficulty = findViewById(R.id.viewQuestDifficulty);
        viewQuestAdventurer = findViewById(R.id.viewQuestAdventurer);
        viewQuestDescription = findViewById(R.id.viewQuestDescription);
        viewQuestRewardStat = findViewById(R.id.viewQuestRewardStat);
        viewQuestRewardExtra = findViewById(R.id.viewQuestRewardExtra);
        viewQuestDeadline = findViewById(R.id.viewQuestDeadline);
        viewQuestTimeRemaining = findViewById(R.id.viewQuestTimeRemaining);
        viewQuestStatus = findViewById(R.id.viewQuestStatus);
        viewQuestReason = findViewById(R.id.viewQuestReason);

        viewQuestButtonA = findViewById(R.id.viewQuestButtonA);
        viewQuestButtonB = findViewById(R.id.viewQuestButtonB);
        viewQuestButtonC = findViewById(R.id.viewQuestButtonC);
        viewQuestButtonD = findViewById(R.id.viewQuestButtonD);

//        if (state.equalsIgnoreCase("child")) {
//            viewQuestButtonD.setVisibility(GONE);
//        }
    }

    public void setQuest (Quest q) {
        this.q = q;

        viewQuestName.setText(q.getQuestData().getName());
        viewQuestDifficulty.setText("★".repeat(q.getQuestData().getDifficulty()));
        viewQuestDescription.setText(q.getQuestData().getDescription());
        viewQuestRewardStat.setText(q.getQuestData().getRewardStat());
        viewQuestRewardExtra.setText(q.getQuestData().getRewardExtra());

        // Set Deadline
        ZonedDateTime deadline = DateTimeUtil.getDateTimeFromEpochSecond(q.getQuestData().getEndDate());
        String status = q.getQuestData().getStatus();
        if (status.equalsIgnoreCase("ongoing")) {
            RemainingTimer rTimer = new RemainingTimer(viewQuestTimeRemaining, deadline, q, this);
            DateTimeUtil.addTimer(rTimer);
        } else {
            viewQuestTimeRemaining.setText(status.toUpperCase());
        }

        // Set Icon
        Boolean rewardStat = q.getQuestData().getRewardStat().equalsIgnoreCase("strength");
        Boolean questStatus = q.getQuestData().getStatus().equalsIgnoreCase("awaiting verification");
        int icon = rewardStat?
                questStatus? R.drawable.icon_str_pending : R.drawable.icon_str  :
                questStatus? R.drawable.icon_int_pending : R.drawable.icon_int
                ;
        viewQuestAvatar.setImageResource(icon);

        // Set Assigned To
        DocumentReference adventurer = q.getQuestData().getAdventurerReference();
        System.out.println("Adventurer: " + adventurer);
        if (adventurer != null) {
            adventurer.get()
                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Child c = ChildManager.parseChildData(task.getResult().getData());
//                            viewQuestAdventurer.setText("Assigned to: " + c.getChildUsername());
//                        }
                    });
        } else {
            viewQuestAdventurer.setText("Assigned to: None");
        }

        // Setup buttons
        switch (status.toLowerCase()) {

            case "awaiting configuration": {
                viewQuestButtonA.setVisibility(GONE);
                viewQuestButtonB.setOnClickListener(v -> {
                    // Create Temporary EditQuestTab
                    EditQuestTab eqt = new EditQuestTab(getContext());
                    eqt.setQuest(q);
                    // Set LayoutParams
                    ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
                    params.setMargins(32,16,32,16);
                    eqt.setLayoutParams(params);
                    // Show
                    ViewGroup parent = (ViewGroup) this.getParent();
                    parent.addView(eqt);
                    this.setVisibility(GONE);
                });
                break;
            }

        }

    }

    public Button getViewQuestButtonC() {
        return viewQuestButtonC;
    }

    public Button getViewQuestButtonD() {
        return viewQuestButtonD;
    }
}
