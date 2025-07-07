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
import com.google.firebase.firestore.FieldValue;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class QuestView extends FrameLayout {
    Quest q;
    QuestViewPreview qvp;
    Boolean isParent;

    public QuestView(@NonNull Context context, boolean isParent) {
        super(context);
        this.isParent = isParent;
        init();
    }

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

        viewQuestButtonC.setOnClickListener(v -> {
            close();
        });

        viewQuestButtonD.setOnClickListener(v -> {
            close();
            ViewGroup parent2 = (ViewGroup) qvp.getParent();
            parent2.removeView(qvp);
            q.setStatus("Deleted");
            FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                    .set(QuestManager.packQuestData(q));
        });

        if (!isParent) {
            viewQuestButtonD.setVisibility(GONE);
        }
    }

    public void setQuest (Quest q, QuestViewPreview qvp) {
        this.q = q;
        this.qvp = qvp;

        q.getAssignedReference().get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().get("Username") != null) {
                viewQuestAdventurer.setText("Assigned: " + task.getResult().get("Username").toString());
            } else {
                viewQuestAdventurer.setText("Assigned: None");
            }
        });

        Number endDate = q.getEndDate();
        ZonedDateTime deadline = DateTimeUtil.getDateTimeFromEpochSecond(endDate.longValue());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma");
        String dueDate = deadline.format(formatter);

        Number diff = q.getDifficulty();

        viewQuestName.setText(q.getName());
        viewQuestDifficulty.setText("Difficulty: " + "★".repeat(diff.intValue()));
        viewQuestAdventurer.setText("Assigned: .....");
        viewQuestDescription.setText("Description:\n"+q.getDescription());
        viewQuestRewardStat.setText("Reward: " + q.getRewardStat());
        viewQuestRewardExtra.setText("More Rewards:\n" + q.getRewardExtra());
        viewQuestDeadline.setText("Due: " + dueDate);
        viewQuestTimeRemaining.setText("00:00:00");
        viewQuestStatus.setText("Status: " + q.getStatus());

        String rewardStat = q.getRewardStat();
        if (rewardStat.equalsIgnoreCase("strength")) {
            viewQuestAvatar.setImageResource(R.drawable.icon_str);
        } else if (rewardStat.equalsIgnoreCase("intelligence")) {
            viewQuestAvatar.setImageResource(R.drawable.icon_int);
        } else {
            viewQuestAvatar.setImageResource(R.drawable.coin_sprite);
        }


        String status = q.getStatus().toLowerCase();
        switch (status) {

            case "awaiting configuration" : {
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
                    close();
                });
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) viewQuestButtonC.getLayoutParams();
                params.topToBottom = viewQuestButtonB.getId();
                viewQuestButtonC.setLayoutParams(params);
                break;
            }

            case "ongoing": {
                if (isParent) {
                    viewQuestButtonA.setVisibility(GONE);
                    viewQuestButtonB.setVisibility(GONE);
                } else {
                    viewQuestButtonA.setText("Submit for Verification");
                    viewQuestButtonA.setOnClickListener(v -> {
                        q.setStatus("Awaiting Verification");
                        q.setCompletedDate(DateTimeUtil.getDateTimeNow().toEpochSecond());
                        FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                                .set(QuestManager.packQuestData(q));
                        close();
                    });
                    viewQuestButtonB.setVisibility(GONE);
                }
                break;
            }

            case "awaiting verification": {
                if (isParent) {
                    viewQuestButtonA.setText("Verify");
                    viewQuestButtonA.setOnClickListener(v -> {
                        q.setStatus("Completed");
                        FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                                .set(QuestManager.packQuestData(q));
                        // Grant rewards
                        DocumentReference child = q.getAssignedReference();
                        child.update(((q.getStatus().equalsIgnoreCase("strength"))?"Strength":"Intelligence"), FieldValue.increment(1));
                        child.update("Gold", q.getDifficulty().intValue());
                        child.update("QuestCompleted", FieldValue.increment(1));
                        // Store a reference for all completed quests
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("QuestReference", FirestoreManager.getFirestore().document("Quests/"+q.getQuestID()));
                        q.getAssignedReference().collection("CompletedQuests").document(q.getQuestID()).set(map);
                        close();
                    });
                    viewQuestButtonB.setText("Reject");
                    viewQuestButtonB.setOnClickListener(v -> {
                        q.setStatus("Ongoing");
                        FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                                .set(QuestManager.packQuestData(q));
                        close();
                    });
                } else {
                    viewQuestButtonA.setText("Awaiting Verification");
                    viewQuestButtonB.setVisibility(GONE);
                }
                break;
            }

            case "failed": {
                // Only shown in ChildViewQuest
                viewQuestButtonA.setText("Submit Reason");
                viewQuestButtonA.setOnClickListener(v -> {
                    ChildExemptionTab cet = new ChildExemptionTab(getContext());
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
                    cet.setLayoutParams(params);
                    // Show
                    ViewGroup parent = (ViewGroup) this.getParent();
                    parent.addView(cet);
                    cet.setQuest(q);
                    close();
                });
                viewQuestButtonB.setVisibility(GONE);
                break;
            }

            case "awaiting exemption": {
                viewQuestRewardStat.setVisibility(GONE);
                viewQuestRewardExtra.setVisibility(GONE);
                viewQuestReason.setVisibility(VISIBLE);
                viewQuestReason.setText("Reason For Failure:\n" + q.getReason());
                viewQuestButtonA.setText("Accept");
                viewQuestButtonA.setOnClickListener(v -> {
                    q.setStatus("Exempted");
                    FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                            .set(QuestManager.packQuestData(q));
                    close();
                });
                viewQuestButtonB.setText("Reject");
                viewQuestButtonB.setOnClickListener(v -> {
                    q.setStatus("Failed");
                    FirestoreManager.getFirestore().document("Quests/"+q.getQuestID())
                            .set(QuestManager.packQuestData(q));
                    close();
                });
                break;
            }

        }

    }

    public void setTimer (ZonedDateTime deadline) {
        RemainingTimer rTimer = new RemainingTimer(viewQuestTimeRemaining, deadline, q, this);
        DateTimeUtil.addTimer(rTimer);
    }

    private void close () {
        ViewGroup parent = (ViewGroup) this.getParent();
        parent.removeView(this);
    }

}
