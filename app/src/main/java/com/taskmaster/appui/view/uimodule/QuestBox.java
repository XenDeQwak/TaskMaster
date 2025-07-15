package com.taskmaster.appui.view.uimodule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.entity.RemainingTimer;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;
import com.taskmaster.appui.util.GenericCallback;

import org.checkerframework.checker.units.qual.C;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class QuestBox extends FrameLayout {

    Quest q;
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

        viewQuestButtonD.setVisibility(
                CurrentUser.getInstance().getUserData().getRole().equalsIgnoreCase("child") ? GONE : VISIBLE
        );
    }

    @SuppressLint("SetTextI18n")
    public void setQuest (Quest q) {
        this.q = q;

        viewQuestName.setText(q.getQuestData().getName());
        viewQuestDifficulty.setText("★".repeat(q.getQuestData().getDifficulty()));
        viewQuestDescription.setText(q.getQuestData().getDescription());
        viewQuestRewardStat.setText(q.getQuestData().getRewardStat());
        viewQuestRewardExtra.setText(q.getQuestData().getRewardExtra());
        viewQuestReason.setText("Reason for Failure:\n"+q.getQuestData().getFailureReason());

        viewQuestReason.setVisibility(GONE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mma");
        viewQuestDeadline.setText("Due: " + DateTimeUtil.getDateTimeFromEpochSecond(q.getQuestData().getEndDate()).format(formatter));

        // Set Deadline
        ZonedDateTime deadline = DateTimeUtil.getDateTimeFromEpochSecond(q.getQuestData().getEndDate());
        String status = q.getQuestData().getStatus();
        if (status.equalsIgnoreCase("ongoing")) {
            viewQuestTimeRemaining.setTextColor(Color.BLACK);
        } else {
            viewQuestTimeRemaining.setText(status.toUpperCase());
            switch (status.toLowerCase()) {
                case "completed": {viewQuestTimeRemaining.setTextColor(Color.GREEN);break;}
                case "deleted":
                case "failed": {viewQuestTimeRemaining.setTextColor(Color.RED);break;}
                case "exempted": {viewQuestTimeRemaining.setTextColor(Color.DKGRAY);break;}
                case "awaiting configuration":
                case "awaiting verification":
                case "awaiting reason for failure":
                case "awaiting exemption": {viewQuestTimeRemaining.setTextColor(Color.rgb(185, 101, 0));break;}
                default: {viewQuestTimeRemaining.setTextColor(Color.BLACK);break;}
            }
        }

        // Set Icon
        Boolean rewardStat = q.getQuestData().getRewardStat().equalsIgnoreCase("strength");
        Boolean questStatus = q.getQuestData().getStatus().equalsIgnoreCase("awaiting verification");
        int icon = rewardStat?
                questStatus? R.drawable.icon_str_pending : R.drawable.icon_str  :
                questStatus? R.drawable.icon_int_pending : R.drawable.icon_int
                ;
        viewQuestAvatar.setImageResource(icon);

        // Set adventurer
        DocumentReference adventurer = q.getQuestData().getAdventurerReference();
        if (adventurer != null) {
            adventurer.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Child c = new Child(task.getResult().toObject(ChildData.class));
                            viewQuestAdventurer.setText("Assigned to: " + c.getChildData().getUsername());
                            setButtons(status, CurrentUser.getInstance().getUserData().getRole());
                        } else {
                            task.getException().printStackTrace();
                        }
                    });
        } else {
            setButtons("awaiting configuration", CurrentUser.getInstance().getUserData().getRole());
            viewQuestAdventurer.setText("Assigned to: None");
        }

    }

    private void setButtons (String status, String role) {
        // Setup buttons
        //System.out.println(status);
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
                    //System.out.println("HELLO WORLD");
                });
                break;
            }

            case "ongoing": {
                if (role.equalsIgnoreCase("parent")) {
                    viewQuestButtonA.setVisibility(GONE);
                    viewQuestButtonB.setVisibility(GONE);
                } else {
                    viewQuestButtonA.setOnClickListener(v -> newDialog(
                            "Submit Quest?",
                            "Are you sure you want to submit this quest for verification of completion?",
                            (dialog) -> {
                                q.getQuestData().setStatus("Awaiting Verification");
                                q.getQuestData().setCompletedDate(DateTimeUtil.getDateTimeNow().toEpochSecond());
                                q.getQuestData().uploadData();
                                dialog.dismiss();
                            }
                    ));
                    viewQuestButtonB.setVisibility(GONE);
                }
                break;
            }

            case "awaiting verification": {
                if (role.equalsIgnoreCase("parent")) {
                    viewQuestButtonA.setText("Accept");
                    viewQuestButtonA.setOnClickListener(v -> newDialog(
                            "Accept Verification?",
                            "Are you sure you want to verify the completion of this quest?",
                            (dialog) -> {
                                q.getQuestData().setStatus("Completed");
                                q.getQuestData().uploadData();
                                dialog.dismiss();
                            }
                    ));
                    viewQuestButtonB.setText("Reject");
                    viewQuestButtonB.setOnClickListener(v -> newDialog(
                            "Reject Verification?",
                            "Are you sure you want to reject the completion of this quest?",
                            (dialog) -> {
                                q.getQuestData().setStatus("Ongoing");
                                q.getQuestData().setCompletedDate(0);
                                q.getQuestData().uploadData();
                                dialog.dismiss();
                            }
                    ));
                }
                break;
            }

            case "awaiting reason for failure": {
                viewQuestButtonA.setText("Submit Reason For Failure");
                viewQuestButtonA.setOnClickListener(v -> {
                    // Create Temporary ChildExemptionTab
                    ChildExemptionTab cet = new ChildExemptionTab(getContext());
                    cet.setQuest(q);
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
                    this.setVisibility(GONE);
                });
                viewQuestButtonB.setVisibility(GONE);
                break;
            }

            case "awaiting exemption": {
                viewQuestDifficulty.setVisibility(GONE);
                viewQuestRewardStat.setVisibility(GONE);
                viewQuestRewardExtra.setVisibility(GONE);
                viewQuestReason.setVisibility(VISIBLE);

                viewQuestButtonA.setText("Accept");
                viewQuestButtonA.setOnClickListener(v -> newDialog(
                        "Accept Reason for Failure?",
                        "Are you sure you want to exempt the failure of this quest?",
                        (dialog) -> {
                            q.getQuestData().setStatus("Exempted");
                            q.getQuestData().uploadData();
                            dialog.dismiss();
                        }
                ));
                viewQuestButtonB.setText("Reject");
                viewQuestButtonB.setOnClickListener(v -> newDialog(
                        "Reject Reason for Failure?",
                        "Are you sure you want fail this quest?",
                        (dialog) -> {
                            q.getQuestData().setStatus("Failed");
                            q.getQuestData().setCompletedDate(0);
                            q.getQuestData().uploadData();
                            dialog.dismiss();
                        }
                ));
                break;
            }

            case "completed":
            case "failed":
            case "exempted":
            case "deleted": {
                viewQuestButtonA.setVisibility(GONE);
                viewQuestButtonB.setVisibility(GONE);
                viewQuestButtonD.setVisibility(GONE);
                break;
            }


        }
    }

    private void newDialog (String title, String msg, GenericCallback<DialogInterface> callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Confirm", ((dialog, which) -> callback.onCallback(dialog)))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());;
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public TextView getViewQuestTimeRemaining() {
        return viewQuestTimeRemaining;
    }

    public Button getViewQuestButtonC() {
        return viewQuestButtonC;
    }

    public Button getViewQuestButtonD() {
        return viewQuestButtonD;
    }
}
