package com.taskmaster.appui.view.parent;

import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.taskmaster.appui.entity.CurrentUser;
import com.taskmaster.appui.util.DropdownUtil;
import com.taskmaster.appui.util.NavUtil;
import com.taskmaster.appui.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManagement extends AppCompatActivity {

    public CurrentUser currentUser;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private FirebaseAuth auth;
    ConstraintLayout newParentLayout;
    FirebaseFirestore db;
    CollectionReference questInfo;
    String storedUsername,userId,rewardType,role,questName, questDescription, time, rewardStat, rewardOptional;
    private String username,parentCode;
    ImageButton imagebutton1, imagebutton3, imagebutton4, imagebutton5, openQuestButton, rewardsStrButton, rewardsIntButton, assignAdvButton1, assignAdvButton2;
    AppCompatButton addQuestButton,assignAdv, assignPrevBtn, assignNextBtn, setRewardsButton, viewRewardsDropdownButton, assignQuestButton, cancelQuestEditButton, saveQuestEditButton, rewardsDropdownButton, rewardsCancelButton, rewardsConfirmButton, viewRewardsButton, viewRewardsExitButton, cancelQuestViewButton, finishQuestViewButton, childBarName, childBarFloorCount, childBarStatsButton, cancelQuestViewButtonC,finishQuestViewButtonC, cancelQuestViewButtonP,approveQuestViewButtonP,rejectQuestViewButtonP, viewNotifOkayButton, assignDropdownButton, assignCancelButton, assignConfirmButton;
    ImageView questFrame, questNameFrame, questImage, questImageIcon, imageView23, imageView18, assignDropdownFrame, popupAssignFrame, editQuestImageIcon, viewQuestImageIcon, imageView19, basePageFrame, popupRewardsFrameShadow, popupRewardsFrame, rewardsDropdownFrame, viewQuestFrame, viewQuestImage, viewDifficultyBG, childBarFrame, childBarAvatar;
    TextView questNameText, rewardsStr, rewardsInt, textView8, viewNotifTextMsg, textView5, basePageTitle;
    EditText editQuestTime, editQuestName, editQuestDesc, viewQuestName, viewQuestTime, viewQuestDesc, viewRewardsOptionalText;
    ScrollView scrollView;
    Group editQuestGroup, popupRewardsGroup, viewQuestGroup, childBarGroup, viewQuestGroupButtonC, viewQuestGroupButtonP, popupViewRewardsGroup, popupViewNotif, popupAssignGroup;
    GridLayout gridLayout;
    LinearLayout newGroup;
    ConstraintLayout newQuest;
    ConstraintSet constraintSet;
    RatingBar setDifficultyRating, viewDifficultyRating;
    DocumentReference questRef;

    //quest count
    int groupCount = 0;
    int questId = 1;
    List<String> childIds;
    int difficulty,questDifficulty,defaultHour, defaultMinute,childAvatar,questWidth, questHeight, imageWidth, imageHeight, nameFrameWidth, nameFrameHeight, topMarginImage, bottomMarginImage, topMarginNameFrame, bottomMarginNameFrame;
    Context context = this;
    View rootLayout;
    private int currentIndex;
    private int lastClickedQuestId = -1;
    private final Map<Integer, TextView> questTextViews = new HashMap<>();
    private final Map<Integer, String> questDescriptions = new HashMap<>();
    private final Map<Integer, Integer> questRatings = new HashMap<>();
    private final Map<Integer, String> questTimes = new HashMap<>();
    private final Map<Integer, String> questRewardStat = new HashMap<>();
    private final Map<Integer, String> questAssigned = new HashMap<>();
    private final Map<Integer, String> questRewardOptional = new HashMap<>();
    private final Map<Integer, TextView> viewQuestTextViews = new HashMap<>();
    private final Map<Integer, String> viewQuestDescriptions = new HashMap<>();
    private final Map<Integer, Integer> viewQuestRatings = new HashMap<>();
    private final Map<Integer, String> viewQuestTimes = new HashMap<>();
    private final Map<Integer, String> viewQuestRewardStat = new HashMap<>();
    private final Map<Integer, String> viewQuestRewardOptional = new HashMap<>();
    private final Map<String, Object> questData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.quest_management);
//
//        //database init
//        db = FirebaseFirestore.getInstance();
//        auth = FirebaseAuth.getInstance();
//
//        // Init currentUser object
//        currentUser = CurrentUser.getInstance();
//        //role = currentUser.getDocumentSnapshot().get("Role").toString();
//
//        NavUtil.hideSystemBars(this);
//
//        // hooks
//        addQuestButton = findViewById(R.id.addQuestButton);
//        gridLayout = findViewById(R.id.gridLayout);
//        rootLayout = findViewById(R.id.cvlb_container);
//        scrollView = findViewById(R.id.scrollView1);
//        DropdownUtil.dropdownSetup(this,rootLayout);
//
//        editQuestTime = findViewById(R.id.editQuestTime);
//        editQuestName = findViewById(R.id.editQuestName);
//        setRewardsButton = findViewById(R.id.setRewardsButton);
//        assignQuestButton = findViewById(R.id.assignQuest);
//        cancelQuestEditButton = findViewById(R.id.editQuestCancel);
//        saveQuestEditButton = findViewById(R.id.editQuestSave);
//        editQuestGroup = findViewById(R.id.editQuestGroup);
//        setDifficultyRating = findViewById(R.id.editQuestDifficulty);
//
//        popupRewardsGroup = findViewById(R.id.popupRewardsGroup);
//        popupRewardsFrameShadow = findViewById(R.id.popupRewardsFrameShadow);
//        popupRewardsFrame = findViewById(R.id.popupRewardsFrame);
//        rewardsDropdownFrame = findViewById(R.id.rewardsDropdownFrame);
//        rewardsStrButton = findViewById(R.id.rewardsStrButton);
//        rewardsIntButton = findViewById(R.id.rewardsIntButton);
//        rewardsCancelButton = findViewById(R.id.rewardsCancelButton);
//        rewardsConfirmButton = findViewById(R.id.rewardsConfirmButton);
//        rewardsInt = findViewById(R.id.rewardsInt);
//        rewardsStr = findViewById(R.id.rewardsStr);
//        rewardsDropdownButton = findViewById(R.id.rewardsDropdownButton);
//
//        viewQuestGroup = findViewById(R.id.viewQuestGroup);
//        viewQuestFrame = findViewById(R.id.viewQuestFrame);
//        //viewQuestName = findViewById(R.id.viewQuestName);
//        viewQuestTime = findViewById(R.id.viewQuestTime);
//        viewQuestDesc = findViewById(R.id.viewQuestDesc);
//        viewDifficultyBG = findViewById(R.id.viewDifficultyBG);
//        viewDifficultyRating = findViewById(R.id.viewDifficultyRating);
//        viewRewardsButton = findViewById(R.id.viewRewardsButton);
//        viewRewardsOptionalText = findViewById(R.id.viewRewardsOptionalText);
//
//        editQuestImageIcon = findViewById(R.id.editQuestImageIcon);
//        viewQuestImageIcon = findViewById(R.id.viewQuestImageIcon);
//
//        popupViewRewardsGroup = findViewById(R.id.popupViewRewardsGroup);
//        viewRewardsExitButton = findViewById(R.id.viewRewardsExitButton);
//
//        viewQuestGroupButtonP = findViewById(R.id.viewQuestGroupButtonP);
//
//        cancelQuestViewButtonP = findViewById(R.id.cancelQuestViewButtonP);
//        approveQuestViewButtonP = findViewById(R.id.approveQuestViewButtonP);
//        rejectQuestViewButtonP = findViewById(R.id.rejectQuestViewButtonP);
//
//        childBarName = findViewById(R.id.childBarName);
//        viewQuestGroupButtonC = findViewById(R.id.viewQuestGroupButtonC);
//
//        cancelQuestViewButtonC = findViewById(R.id.cancelQuestViewButtonC);
//        finishQuestViewButtonC = findViewById(R.id.finishQuestViewButtonC);
//
//        popupViewNotif = findViewById(R.id.popupViewNotif);
//        viewNotifTextMsg = findViewById(R.id.viewNotifTextMsg);
//        viewNotifOkayButton = findViewById(R.id.viewNotifOkayButton);
//
//        childBarGroup = findViewById(R.id.childBarGroup);
//        childBarFrame = findViewById(R.id.childBarFrame);
//        childBarName = findViewById(R.id.childBarName);
//        childBarFloorCount = findViewById(R.id.childBarFloorCount);
//        childBarStatsButton = findViewById(R.id.childBarStatsButton);
//        childBarAvatar = findViewById(R.id.childBarAvatar);
//        childBarAvatar = findViewById(R.id.childBarAvatar);
//
//        basePageFrame = findViewById(R.id.basePageFrame);
//        basePageTitle = findViewById(R.id.basePageTitle);
//        textView5 = findViewById(R.id.textView5);
//        imageView19 = findViewById(R.id.imageView19);
//        imageView23 = findViewById(R.id.imageView23);
//
//        popupAssignGroup = findViewById(R.id.popupAssignGroup);
//        assignDropdownFrame = findViewById(R.id.assignDropdownFrame);
//        assignDropdownButton = findViewById(R.id.assignDropdownButton);
//        assignCancelButton = findViewById(R.id.assignCancelButton);
//        assignConfirmButton = findViewById(R.id.assignConfirmButton);
//        assignAdv = findViewById(R.id.assignAdv);
//        popupAssignFrame = findViewById(R.id.popupAssignFrame);
//        assignPrevBtn = findViewById(R.id.assignPrevBtn);
//        assignNextBtn = findViewById(R.id.assignNextBtn);
//        viewRewardsDropdownButton = findViewById(R.id.viewRewardsDropdownButton);
//
//        popupRewardsGroup.setVisibility(View.GONE);
//        editQuestGroup.setVisibility(View.GONE);
//        viewQuestGroup.setVisibility(View.GONE);
//
//        // remove scrollview visibility initially, keep this cause dropdown exit doesn't function as intended
//        scrollView.setVisibility(View.GONE);
//        assignNextBtn.setOnClickListener(e -> {
//            currentIndex++;
//            if (currentIndex >= childIds.size())
//                currentIndex = 0;
//            saveQuestToChild(childIds.get(currentIndex));
//        });
//
//        if("child".equals(role)){
//            childInit();
//        }
//        else if("parent".equals(currentUser.getDocumentSnapshot().get("Role"))){
//            parentInit();
//        }
//        else{
//            System.out.println("error");
//        }
//
//        addQuestButton.setOnClickListener(v -> {
//
//        });
//
//        // cancel quest view child
//        cancelQuestViewButtonC.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewQuestGroup.setVisibility(View.GONE);
//                viewQuestGroupButtonC.setVisibility(View.GONE);
//                recreate();
//            }
//        });
//
//        // quest time
//        editQuestTime.setOnClickListener(v -> {
//            defaultHour = 23;
//            defaultMinute = 59;
//
//            TimePickerDialog timePickerDialog = new TimePickerDialog(
//                    context,
//                    (view, hourOfDay, minute1) -> {
//                        int displayHour = Math.min(hourOfDay, 24);
//                        String selectedTime = String.format("%02d:%02d:00", displayHour, minute1);
//                        editQuestTime.setText(selectedTime);
//                        questTimes.put(lastClickedQuestId, selectedTime);
//                    },
//                    defaultHour, // Use the default hour
//                    defaultMinute, // Use the default minute
//                    true
//            );
//            timePickerDialog.show();
//        });
//
//        // set rewards
//        setRewardsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupRewardsGroup.setVisibility(View.VISIBLE);
//                rewardsDropdownFrame.setVisibility(View.GONE);
//                rewardsInt.setVisibility(View.GONE);
//                rewardsStr.setVisibility(View.GONE);
//                rewardsIntButton.setVisibility(View.GONE);
//                rewardsStrButton.setVisibility(View.GONE);
//
//
//            }
//        });
//
//        // assign quest
//        assignDropdownButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//
//
//        rewardsDropdownButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (rewardsDropdownFrame.getVisibility() == View.VISIBLE) {
//                    rewardsDropdownFrame.setVisibility(View.GONE);
//                    rewardsInt.setVisibility(View.GONE);
//                    rewardsStr.setVisibility(View.GONE);
//                    rewardsIntButton.setVisibility(View.GONE);
//                    rewardsStrButton.setVisibility(View.GONE);
//                    Toast.makeText(QuestManagement.this, "close", Toast.LENGTH_SHORT).show();
//                } else {
//                    rewardsDropdownFrame.setVisibility(View.VISIBLE);
//                    rewardsInt.setVisibility(View.VISIBLE);
//                    rewardsStr.setVisibility(View.VISIBLE);
//                    rewardsIntButton.setVisibility(View.VISIBLE);
//                    rewardsStrButton.setVisibility(View.VISIBLE);
//                    Toast.makeText(QuestManagement.this, "open", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//        rewardsIntButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                questRewardStat.put(lastClickedQuestId, "intelligence");
//                Log.d("QuestReward", "Quest " + lastClickedQuestId + ": set to intelligence");
//                rewardsDropdownButton.setText("Intelligence");
//                rewardsDropdownFrame.setVisibility(View.GONE);
//                rewardsInt.setVisibility(View.GONE);
//                rewardsStr.setVisibility(View.GONE);
//                rewardsIntButton.setVisibility(View.GONE);
//                rewardsStrButton.setVisibility(View.GONE);
//
//                updateQuestIcon(lastClickedQuestId);
////                questImageIcon.setImageResource(R.drawable.icon_int);
////                editQuestImageIcon.setImageResource(R.drawable.icon_int);
////                viewQuestImageIcon.setImageResource(R.drawable.icon_int);
//            }
//        });
//
//        rewardsStrButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                questRewardStat.put(lastClickedQuestId, "strength");
//                Log.d("QuestReward", "Quest " + lastClickedQuestId + ": set to strength");
//                rewardsDropdownButton.setText("Strength");
//                rewardsDropdownFrame.setVisibility(View.GONE);
//                rewardsInt.setVisibility(View.GONE);
//                rewardsStr.setVisibility(View.GONE);
//                rewardsIntButton.setVisibility(View.GONE);
//                rewardsStrButton.setVisibility(View.GONE);
//
//                updateQuestIcon(lastClickedQuestId);
////                questImageIcon.setImageResource(R.drawable.icon_str);
////                editQuestImageIcon.setImageResource(R.drawable.icon_str);
////                viewQuestImageIcon.setImageResource(R.drawable.icon_str);
//            }
//        });
//
//        // redundant?
//        rewardsCancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rewardsDropdownFrame.setVisibility(View.GONE);
//                rewardsInt.setVisibility(View.GONE);
//                rewardsStr.setVisibility(View.GONE);
//                rewardsIntButton.setVisibility(View.GONE);
//                rewardsStrButton.setVisibility(View.GONE);
//                popupRewardsGroup.setVisibility(View.GONE);
//            }
//        });
//
//        // redundant?
//        rewardsConfirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                rewardsDropdownFrame.setVisibility(View.GONE);
//                rewardsInt.setVisibility(View.GONE);
//                rewardsStr.setVisibility(View.GONE);
//                rewardsIntButton.setVisibility(View.GONE);
//                rewardsStrButton.setVisibility(View.GONE);
//                popupRewardsGroup.setVisibility(View.GONE);
//            }
//        });
//
//        setDifficultyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                if (fromUser) {
//                    questRef = db.collection("quest").document(username + "Quest" + (lastClickedQuestId));
//                    int intRating = (int) rating; // Cast to int
//                    Toast.makeText(QuestManagement.this, "New rating: " + rating, Toast.LENGTH_SHORT).show();
//                    questRatings.put(lastClickedQuestId, intRating);
//                    viewQuestRatings.put(lastClickedQuestId, intRating);
//                    questRef.update("difficulty", intRating);
//                }
//            }
//        });
//
//        // cancel quest edit
//        cancelQuestEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(QuestManagement.this, "cancel quest edit", Toast.LENGTH_SHORT).show();
//                editQuestGroup.setVisibility(View.GONE);
//            }
//        });
//
//        // create quest edit
//        saveQuestEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Use lastClickedQuestId to update the correct quest
//                questRef = db.collection("quest").document(username + "Quest" + (lastClickedQuestId));
//
//                // Find the quest layout by questId
//                ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId); // Use lastClickedQuestId to get the specific quest
//
//                if (questLayout != null) {
//                    // Get the correct TextView from the map
//                    TextView questText = questTextViews.get(lastClickedQuestId); // Get the TextView using the questId
//                    if (questText != null) {
//                        questText.setText(editQuestName.getText().toString());
//                        questRef.update("name", editQuestName.getText().toString());
//                    }
//
//                    // Update the quest description
//                    EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Correct ID
//                    if (editQuestDesc != null) {
//                        questDescriptions.put(lastClickedQuestId, editQuestDesc.getText().toString());
//                        questRef.update("description", editQuestDesc.getText().toString());
//                    }
//
//                    // Update the quest time
//                    if (editQuestTime != null) {
//                        String time = editQuestTime.getText().toString();
//                        questTimes.put(lastClickedQuestId, editQuestTime.getText().toString());
//                        questRef.update("time", time);
//
//                        long hourL = 0, minuteL = 0, secondL = 0;
//                        String[] timeParts = time.split(":");
//
//                        if (timeParts.length == 2) {
//                            String hours = timeParts[0];
//                            String minutes = timeParts[1];
//                            String seconds = "0";
//                            hourL = Long.parseLong(hours);
//                            minuteL = Long.parseLong(minutes);
//                            secondL = Long.parseLong(seconds);
//                        } else if (timeParts.length == 3) {
//                            String hours = timeParts[0];
//                            String minutes = timeParts[1];
//                            String seconds = timeParts[2];
//                            hourL = Long.parseLong(hours);
//                            minuteL = Long.parseLong(minutes);
//                            secondL = Long.parseLong(seconds);
//                        } else {
//                            Log.e("TimeSplit", "Invalid time format: " + time);
//                        }
//                        countDownTimer(hourL, minuteL, secondL, lastClickedQuestId);
//
//                    }
//
//                    // Update questRewardsOptional
//                    EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText);
//                    if (editQuestRewardsOptional != null) {
//                        questRewardOptional.put(lastClickedQuestId, editQuestRewardsOptional.getText().toString());
//                        questRef.update("rewardOptional", editQuestRewardsOptional.getText().toString());
//                    }
//
//                    questRef.update("rewardStat", questRewardStat.get(lastClickedQuestId));
//
//                    // Hide the editor after saving
//                    editQuestGroup.setVisibility(View.GONE);
//                    Toast.makeText(QuestManagement.this, "Quest " + lastClickedQuestId + " updated!", Toast.LENGTH_SHORT).show();
//                    recreate();
//                }
//            }
//        });
    }

    private void countDownTimer(long hourL, long minuteL, long secondL, int questId) {
        long ms = hourL * 3600000 + minuteL * 60000 + secondL * 1000;
        DocumentReference questRef = db.collection("quest").document(username + "Quest" + questId);
        new CountDownTimer(ms, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                long hours = secondsRemaining / 3600;
                long minutes = (secondsRemaining % 3600) / 60;
                long seconds = secondsRemaining % 60;

                String timeLeftFormatted;
                if (hours > 0) {
                    timeLeftFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                } else {
                    timeLeftFormatted = String.format("%02d:%02d", minutes, seconds);
                }
                viewQuestTime.setText(timeLeftFormatted);
                Log.d("TIME: ", "Remaining time: " + timeLeftFormatted);
                //questRef.update("time", timeLeftFormatted);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    private void saveQuestToChild(String childID) {
        db.collection("users").document(childID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            assignQuestButton.setText(document.getString("username"));
                        }
                    }
                });
        DocumentReference questRef = db.collection("quest").document(username + "Quest" + (lastClickedQuestId));
        questRef.update("childId", childID);
    }

    private void fetchQuests(String parentCode) {
        db.collection("quest").whereEqualTo("roomCode", parentCode).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int questId;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questName = document.getString("name");
                            questDescription = document.getString("description");
                            difficulty = document.getLong("difficulty").intValue();
                            time = document.getString("time");
                            rewardStat = document.getString("rewardStat");
                            questId = document.getLong("questId").intValue();
                            rewardOptional = document.getString("rewardOptional");
                            Boolean forVerif = document.getBoolean("forVerif");
                            createQuest(questName, questDescription, difficulty, time, rewardStat, rewardOptional, forVerif);
//                            updateQuestIconVerif(rewardStat, forVerif);
                            long hourL = 0, minuteL = 0, secondL = 0;
                            String[] timeParts = time.split(":");
                            if (timeParts.length == 2) {
                                String hours = timeParts[0];
                                String minutes = timeParts[1];
                                String seconds = "0";
                                hourL = Long.parseLong(hours);
                                minuteL = Long.parseLong(minutes);
                                secondL = Long.parseLong(seconds);
                            } else if (timeParts.length == 3) {
                                String hours = timeParts[0];
                                String minutes = timeParts[1];
                                String seconds = timeParts[2];
                                hourL = Long.parseLong(hours);
                                minuteL = Long.parseLong(minutes);
                                secondL = Long.parseLong(seconds);
                            } else {
                                Log.e("TimeSplit", "Invalid time format: " + time);
                            }
                            countDownTimer(hourL, minuteL, secondL, questId);

                        }
                    } else {
                        Log.d("ERROR", "NOTHING HAPPENED");
                    }
                });
    }

    private void createQuest(String questName, String questDescription, int questDiff, String questTime, String rewardStat, String rewardOptional, Boolean forVerif) {
//        private void createQuest(String questName, String questDescription, int questDiff, String questTime, String rewardStat, String questAssign, String rewardOptional) {

        // convert px to dp
        questWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 172, getResources().getDisplayMetrics());
        questHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 245, getResources().getDisplayMetrics());
        imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 161, getResources().getDisplayMetrics());
        imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 187, getResources().getDisplayMetrics());
        nameFrameWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 162, getResources().getDisplayMetrics());
        nameFrameHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 39, getResources().getDisplayMetrics());
        topMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
        bottomMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        topMarginNameFrame = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 191, getResources().getDisplayMetrics());
        bottomMarginNameFrame = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());

        //for questGroup
        // create a new LinearLayout
        newGroup = new LinearLayout(context);
        newGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newGroup.setOrientation(LinearLayout.HORIZONTAL);
        newGroup.setGravity(Gravity.CENTER);

        // create a new ConstraintLayout
        newQuest = new ConstraintLayout(context);
        ConstraintLayout.LayoutParams questParams = new ConstraintLayout.LayoutParams(questWidth, questHeight);
        if (groupCount % 2 == 0) {
            questParams.setMarginEnd(4);
        } else {
            questParams.setMarginStart(4);
        }
        newQuest.setLayoutParams(questParams);

        newQuest.setTag(questId);

        // Access the questId later from newQuest
        int retrievedQuestId = (int) newQuest.getTag();

        // create quest frame
        questFrame = new ImageView(context);
        questFrame.setId(View.generateViewId());
        int questFrameId = questFrame.getId();
        questFrame.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));
        questFrame.setAlpha(0.5f);
        questFrame.setImageResource(R.drawable.rectangle_rounded);
        //
        // create quest name frame
        questNameFrame = new ImageView(context);
        questNameFrame.setId(View.generateViewId());
        ConstraintLayout.LayoutParams nameFrameParams = new ConstraintLayout.LayoutParams(nameFrameWidth, nameFrameHeight);
        nameFrameParams.topMargin = topMarginNameFrame;
        nameFrameParams.bottomMargin = bottomMarginNameFrame;
        questNameFrame.setLayoutParams(nameFrameParams);
        questNameFrame.setAlpha(0.5f);
        questNameFrame.setImageResource(R.drawable.rectangle_rounded);

        // create quest image
        questImage = new ImageView(context);
        questImage.setId(View.generateViewId());
        ConstraintLayout.LayoutParams imageParams = new ConstraintLayout.LayoutParams(imageWidth, imageHeight);
        imageParams.topMargin = topMarginImage;
        imageParams.bottomMargin = bottomMarginImage;
        questImage.setLayoutParams(imageParams);
        //questImage.setAlpha(0.5f);

        // create quest image icon
        questImageIcon = new ImageView(context);
        questImageIcon.setId(View.generateViewId());
        ConstraintLayout.LayoutParams imageIconParams = new ConstraintLayout.LayoutParams(imageWidth, imageHeight);
        imageParams.topMargin = topMarginImage;
        imageParams.bottomMargin = bottomMarginImage;
        questImageIcon.setLayoutParams(imageIconParams);
        //questImageIcon.setImageResource(R.drawable.blank_icon);
        questImageIcon.setAlpha(1f);

        if ("strength".equals(rewardStat)) {
            questImageIcon.setImageResource(R.drawable.icon_str);
            if (forVerif) {
                questImageIcon.setImageResource(R.drawable.icon_str_pending);
            } else {
                questImageIcon.setImageResource(R.drawable.icon_str);
            }
        }
        else if ("intelligence".equals(rewardStat)) {
            questImageIcon.setImageResource(R.drawable.icon_int);
            if (forVerif) {
                questImageIcon.setImageResource(R.drawable.icon_int_pending);
            } else {
                questImageIcon.setImageResource(R.drawable.icon_int);
            }
        }

        // create quest name text
        TextView currentQuestNameText = new TextView(context);
        currentQuestNameText.setId(View.generateViewId());
        currentQuestNameText.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        currentQuestNameText.setText(questName);
        currentQuestNameText.setTextSize(20);
        currentQuestNameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        currentQuestNameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        currentQuestNameText.setTextColor(Color.parseColor("#3F3F3F"));

        questTextViews.put(questId, currentQuestNameText);

        // create quest description
        questDescriptions.put(questId, questDescription);

        // store quest rating
        questRatings.put(questId, questDiff); // Default to 0 stars

        // Store the default time
        questTimes.put(questId, questTime);

        // store quest reward stat
        questRewardStat.put(questId, rewardStat);

        // store quest assign
        questAssigned.put(questId, "questAssign");

        // store quest reward optional
        questRewardOptional.put(questId, rewardOptional);

        // store the viewQuests
        viewQuestTextViews.put(questId, currentQuestNameText);
        viewQuestDescriptions.put(questId, questDescription);
        viewQuestRatings.put(questId, questDiff);
        viewQuestTimes.put(questId, questTime);
        viewQuestRewardStat.put(questId, rewardStat);
        viewQuestRewardOptional.put(questId, rewardOptional);

        // create open quest button
        openQuestButton = new ImageButton(context);
        openQuestButton.setId(View.generateViewId());
        openQuestButton.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));
        openQuestButton.setBackground(null);

        int currentQuestId = questId; // Save unique ID to avoid issues with references
        openQuestButton.setTag(currentQuestId);

        //Removed if navgroup==gone here, if it breaks ill put it back -cent
        openQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click for the correct questId
                int clickedQuestId = (int) v.getTag();
                lastClickedQuestId = clickedQuestId;
                populateQuestEditor(clickedQuestId);

                long clickTime = System.currentTimeMillis();
                boolean isDoubleClick = (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA);
                lastClickTime = clickTime;

                if (isDoubleClick) {
//                        if ("parent".equals(role)) {
//                            Toast.makeText(QuestManagement.this, "Parent View Quest " + clickedQuestId, Toast.LENGTH_SHORT).show();
//                            viewQuestGroup.setVisibility(View.VISIBLE);
//                            viewQuestGroupButtonP.setVisibility(View.VISIBLE);
//                        }
                } else {
                    if ("child".equals(role)) {
                        viewQuestGroup.setVisibility(View.VISIBLE);
                        viewQuestGroupButtonC.setVisibility(View.VISIBLE);
                    } else if ("parent".equals(role)) {
                        if (forVerif) {
                            viewQuestGroup.setVisibility(View.VISIBLE);
                            viewQuestGroupButtonP.setVisibility(View.VISIBLE);
                        } else {
                            editQuestGroup.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }
        });
        newQuest.addView(openQuestButton);

        // add views to ConstraintLayout
        newQuest.addView(questFrame);
        newQuest.addView(questNameFrame);
        newQuest.addView(questImage);
        newQuest.addView(questImageIcon);
        newQuest.addView(currentQuestNameText);


        // set constraints programmatically
        constraintSet = new ConstraintSet();
        constraintSet.clone(newQuest);

        // align questNameFrame inside questFrame
        constraintSet.connect(questNameFrame.getId(), ConstraintSet.TOP, questFrame.getId(), ConstraintSet.TOP, topMarginNameFrame);
        constraintSet.connect(questNameFrame.getId(), ConstraintSet.BOTTOM, questFrame.getId(), ConstraintSet.BOTTOM, bottomMarginNameFrame);
        constraintSet.connect(questNameFrame.getId(), ConstraintSet.START, questFrame.getId(), ConstraintSet.START, 0);
        constraintSet.connect(questNameFrame.getId(), ConstraintSet.END, questFrame.getId(), ConstraintSet.END, 0);

        // align questImage inside questFrame
        constraintSet.connect(questImage.getId(), ConstraintSet.TOP, questFrame.getId(), ConstraintSet.TOP, topMarginImage);
        constraintSet.connect(questImage.getId(), ConstraintSet.BOTTOM, questFrame.getId(), ConstraintSet.BOTTOM, bottomMarginImage);
        constraintSet.connect(questImage.getId(), ConstraintSet.START, questFrame.getId(), ConstraintSet.START, 0);
        constraintSet.connect(questImage.getId(), ConstraintSet.END, questFrame.getId(), ConstraintSet.END, 0);

        // align questImageIcon inside questImage
        constraintSet.connect(questImageIcon.getId(), ConstraintSet.TOP, questImage.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(questImageIcon.getId(), ConstraintSet.BOTTOM, questImage.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.connect(questImageIcon.getId(), ConstraintSet.START, questImage.getId(), ConstraintSet.START, 0);
        constraintSet.connect(questImageIcon.getId(), ConstraintSet.END, questImage.getId(), ConstraintSet.END, 0);

        // align questNameText inside questNameFrame
        constraintSet.connect(currentQuestNameText.getId(), ConstraintSet.TOP, questNameFrame.getId(), ConstraintSet.TOP, 0);
        constraintSet.connect(currentQuestNameText.getId(), ConstraintSet.BOTTOM, questNameFrame.getId(), ConstraintSet.BOTTOM, 0);
        constraintSet.connect(currentQuestNameText.getId(), ConstraintSet.START, questNameFrame.getId(), ConstraintSet.START, 0);
        constraintSet.connect(currentQuestNameText.getId(), ConstraintSet.END, questNameFrame.getId(), ConstraintSet.END, 0);

        constraintSet.applyTo(newQuest);

        // add to LinearLayout
        newGroup.addView(newQuest);

        // add to GridLayout
        gridLayout.addView(newGroup);

        questTextViews.put(questId, currentQuestNameText);
        questDescriptions.put(questId, "");
        questRatings.put(questId, 0);
        questTimes.put(questId, "23:59:00");
        questRewardStat.put(questId, "None");
        questAssigned.put(questId, "");
        questRewardOptional.put(questId, "");

        // test message
        Toast.makeText(QuestManagement.this, "New Quest Added: Quest " + questId, Toast.LENGTH_SHORT).show();

        groupCount++;
        questId++;
        updateScrollViewVisibility();
    }
//else if ("parent".equals(role)) {

//    }
    private void childInit () {
        addQuestButton.setVisibility(View.GONE);
        imageView19.setVisibility(View.GONE);
        textView5.setVisibility(View.GONE);
        childBarGroup.setVisibility(View.VISIBLE);
        basePageFrame.setVisibility(View.VISIBLE);
        basePageTitle.setVisibility(View.VISIBLE);
        imageView23.setVisibility(View.GONE);
//        String childToParentCode = currentUser.getDocumentSnapshot().getString("parentCode");
//        childFetchQuest(childToParentCode, userId);
//        db.collection("Users").whereEqualTo("code",childToParentCode).get();

//            String childToParentCode = childDocument.getString("parentCode");
//            childFetchQuest(childToParentCode, userId);
//            db.collection("Users").whereEqualTo("code", childToParentCode).get()
//                    .addOnCompleteListener(tasks -> {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : tasks.getResult()) {
//                                username = document.getString("username");
//                                storedUsername = username;
//                            }
//                        }
//                    });
////          childAvatar = childDocument.getLong("childAvatar").intValue();
//            childBarName.setText(childDocument.getString("username"));
////          childBarFloorCount.setText("Floor: " + childDocument.getLong("floor").intValue());
//            List<Integer> avatarImages = new ArrayList<>();
//            avatarImages.add(R.drawable.placeholderavatar5_framed_round);
//            avatarImages.add(R.drawable.placeholderavatar1_framed_round);
//            avatarImages.add(R.drawable.placeholderavatar2_framed_round);
//            avatarImages.add(R.drawable.placeholderavatar3_framed_round);
//            avatarImages.add(R.drawable.placeholderavatar4_framed_round);
////          childBarAvatar.setImageResource(avatarImages.get(childAvatar));

    }

    private void parentInit () {
        childBarGroup.setVisibility(View.GONE);
        basePageFrame.setVisibility(View.GONE);
        basePageTitle.setVisibility(View.GONE);
        imageView23.setVisibility(View.VISIBLE);
        db.collection("users").whereEqualTo("uid", userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    username = document.getString("username");
                    parentCode = document.getString("code");
                    if (document.exists()) {
                        fetchQuests(parentCode);
                        DocumentReference docRef = db.collection("quest").document(username + "Quest" + questId);
                        docRef.get().addOnCompleteListener(tasks -> {
                            if (tasks.isSuccessful()) {
                                DocumentSnapshot documents = tasks.getResult();
                                if (documents.exists()) {
                                    childBarGroup.setVisibility(View.GONE);
                                    Boolean parentVerif = documents.getBoolean("forVerif");
                                    if (Boolean.TRUE.equals(parentVerif)) {
                                        Log.d("RECEIVED", "Child verification received");
                                    }
                                } else {
                                    Log.d("TAG", "DOCUMENT NOT EXIST");
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void populateQuestEditor(int questId) {
        // Find the specific quest layout using its questId
        ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId);

        // Find and update the quest components based on the selected quest
        if (questLayout != null) {
            // Get the correct TextView from the map
            TextView questText = questTextViews.get(lastClickedQuestId); // Get the TextView using the questId

            EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Ensure this is the correct ID
            RatingBar setDifficultyRating = findViewById(R.id.editQuestDifficulty);
            EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText); // Correct ID
            AppCompatButton rewardsDropdownButton = findViewById(R.id.rewardsDropdownButton);
            AppCompatButton viewRewardsDropdownButton = findViewById(R.id.viewRewardsDropdownButton);

            ImageView editQuestImageIcon = findViewById(R.id.editQuestImageIcon);
            ImageView viewQuestImageIcon = findViewById(R.id.viewQuestImageIcon);

            //EditText viewQuestName = findViewById(R.id.viewQuestName);
            EditText viewQuestDesc = findViewById(R.id.viewQuestDesc);
            EditText viewQuestTime = findViewById(R.id.viewQuestTime);

            RatingBar viewDifficultyRating = findViewById(R.id.viewDifficultyRating);

            // Set the editor fields with the quest's details
            if (questText != null) {
                editQuestName.setText(questText.getText().toString());
                viewQuestName.setText(questText.getText().toString());
            }
            if (editQuestDesc != null) {

                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestDesc = document.getString("description");
                                    editQuestDesc.setText(currentQuestDesc);
                                    viewQuestDesc.setText(currentQuestDesc);
                                }
                            }
                        });

            }
            if (setDifficultyRating != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    int currentQuestDifficulty = document.getLong("difficulty").intValue();
                                    setDifficultyRating.setRating(currentQuestDifficulty);
                                    viewDifficultyRating.setRating(currentQuestDifficulty);
                                }
                            }
                        });
            }
            if (editQuestTime != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestTime = document.getString("time");
                                    editQuestTime.setText(currentQuestTime);
                                    viewQuestTime.setText(currentQuestTime);
                                }
                            }
                        });
            }
            if (editQuestRewardsOptional != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestRewardOptional = document.getString("rewardOptional");
                                    viewRewardsOptionalText.setText(currentQuestRewardOptional);
                                }
                            }
                        });
            }

            String currentQuestReward = questRewardStat.get(questId);
            if (currentQuestReward != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Boolean currentQuestForVerif = document.getBoolean("forVerif");
                                    String currentQuestRewardStat = document.getString("rewardStat");
                                        if (currentQuestRewardStat.equals("intelligence")) {
                                            rewardsDropdownButton.setText("Intelligence");
                                            viewRewardsDropdownButton.setText("Intelligence");
                                            editQuestImageIcon.setImageResource(R.drawable.icon_int);
                                            viewQuestImageIcon.setImageResource(R.drawable.icon_int);

                                                if (currentQuestForVerif) {
                                                    questImageIcon.setImageResource(R.drawable.icon_int_pending);
                                                    editQuestImageIcon.setImageResource(R.drawable.icon_int_pending);
                                                    viewQuestImageIcon.setImageResource(R.drawable.icon_int_pending);
                                                }
                                            } else if (currentQuestRewardStat.equals("strength")) {
                                            rewardsDropdownButton.setText("Strength");
                                            viewRewardsDropdownButton.setText("Strength");
                                            editQuestImageIcon.setImageResource(R.drawable.icon_str);
                                            viewQuestImageIcon.setImageResource(R.drawable.icon_str);
                                            if (currentQuestForVerif) {
                                                questImageIcon.setImageResource(R.drawable.icon_str_pending);
                                                editQuestImageIcon.setImageResource(R.drawable.icon_str_pending);
                                                viewQuestImageIcon.setImageResource(R.drawable.icon_str_pending);
                                            }
                                        } else {
                                        rewardsDropdownButton.setText("Stat Increase");
                                        viewRewardsDropdownButton.setText("Stat Increase");
                                        editQuestImageIcon.setImageDrawable(null);
                                        viewQuestImageIcon.setImageDrawable(null);
                                    }
                                }
                            }
                        });
            }



            String currentQuestAssigned = questAssigned.get(questId);
            if (currentQuestAssigned.equals("Adv 1")) {
                assignDropdownButton.setText("Adv 1");
                Log.d("QuestAssigned", "Quest " + questId + ": Adv 1");
            } else if (currentQuestAssigned.equals("Adv 2")) {
                assignDropdownButton.setText("Adv 2");
                Log.d("QuestAssigned", "Quest " + questId + ": Adv 2");
            } else {
                assignDropdownButton.setText("Adventurers");
                Log.d("QuestAssigned", "Quest " + questId + ": None");
            }
//            updateQuestIcon1(currentQuestReward);
        }
    }

    private void updateQuestIcon(int questId) {
        // Find the specific quest layout using its questId
        ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId);

        // Find and update the quest components based on the selected quest
        if (questLayout != null) {
            // Get the correct TextView from the map
            ImageView editQuestImageIcon = findViewById(R.id.editQuestImageIcon);
            ImageView viewQuestImageIcon = findViewById(R.id.viewQuestImageIcon);

            String currentQuestReward = questRewardStat.get(questId);
            if (currentQuestReward != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestRewardStat = document.getString("rewardStat");
                                    if (currentQuestRewardStat.equals("intelligence")) {
                                        editQuestImageIcon.setImageResource(R.drawable.icon_int);
                                        viewQuestImageIcon.setImageResource(R.drawable.icon_int);
                                    } else if (currentQuestRewardStat.equals("strength")) {
                                        editQuestImageIcon.setImageResource(R.drawable.icon_str);
                                        viewQuestImageIcon.setImageResource(R.drawable.icon_str);
                                    } else {
                                        editQuestImageIcon.setImageDrawable(null);
                                        viewQuestImageIcon.setImageDrawable(null);
                                    }
                                }
                            }
                        });
            }
        }
    }

    private void updateQuestIconVerif(int questId, boolean forVerif) {
        // Find the specific quest layout using its questId
        ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId);

        // Find and update the quest components based on the selected quest
        if (questLayout != null) {
            // Get the correct TextView from the map

            String currentQuestReward = questRewardStat.get(questId);
            if (currentQuestReward != null) {
                db.collection("quest").document(username + "Quest" + lastClickedQuestId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestRewardStat = document.getString("rewardStat");
                                    if (currentQuestRewardStat.equals("intelligence")) {
                                        if (forVerif) {
                                            questImageIcon.setImageResource(R.drawable.icon_int_pending);
                                        } else {
                                            questImageIcon.setImageResource(R.drawable.icon_int);
                                        }
                                    } else if (currentQuestRewardStat.equals("strength")) {
                                        if (forVerif) {
                                            questImageIcon.setImageResource(R.drawable.icon_str_pending);
                                        } else {
                                            questImageIcon.setImageResource(R.drawable.icon_str);
                                        }
                                    } else {
                                        questImageIcon.setImageResource(R.drawable.blank_icon);
                                    }
                                }
                            }
                        });
            }
        }
    }

//    private void updateQuestIcon(String rewardStat, Boolean forVerif) {
//        if (questImageIcon != null) {
//            if (editQuestImageIcon != null) {
//                if (rewardStat.equals("intelligence")) {
//                    if (forVerif) {
//                        questImageIcon.setImageResource(R.drawable.icon_int_pending);
//                    } else {
//                        questImageIcon.setImageResource(R.drawable.icon_int);
//                    }
//                } else if (rewardStat.equals("strength")) {
//                    if (forVerif) {
//                        questImageIcon.setImageResource(R.drawable.icon_str_pending);
//                    } else {
//                        questImageIcon.setImageResource(R.drawable.icon_str);
//                    }
//                } else {
//                    questImageIcon.setImageResource(R.drawable.blank_icon);
//                }
//            }
//        }
//    }

//    private void updateQuestIcon1(String rewardStat) {
//        if (questImageIcon != null) {
//            if (editQuestImageIcon != null) {
//                if (rewardStat.equals("intelligence")) {
//                        editQuestImageIcon.setImageResource(R.drawable.icon_int);
//                        viewQuestImageIcon.setImageResource(R.drawable.icon_int);
//                } else if (rewardStat.equals("strength")) {
//                        editQuestImageIcon.setImageResource(R.drawable.icon_str);
//                        viewQuestImageIcon.setImageResource(R.drawable.icon_str);
////                } else {
//////                    editQuestImageIcon.setAlpha(0f);
//////                    viewQuestImageIcon.setAlpha(0f);
//                }
//            }
//        }
//    }


    private void childFetchQuest(String parentCode, String childId) {
        db.collection("quest")
                .whereEqualTo("roomCode", parentCode)
                .whereEqualTo("childId", childId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int questId;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questName = document.getString("name");
                            questDescription = document.getString("description");
                            difficulty = document.getLong("difficulty").intValue();
                            time = document.getString("time");
                            rewardStat = document.getString("rewardStat");
                            questId = document.getLong("questId").intValue();
                            rewardOptional = document.getString("rewardOptional");
                            Boolean forVerif = document.getBoolean("forVerif");
                            createQuest(questName, questDescription, difficulty, time, rewardStat, rewardOptional, forVerif);
                            long hourL = 0, minuteL = 0, secondL = 0;
                            String[] timeParts = time.split(":");
                            if (timeParts.length == 2) {
                                String hours = timeParts[0];
                                String minutes = timeParts[1];
                                String seconds = "0";
                                hourL = Long.parseLong(hours);
                                minuteL = Long.parseLong(minutes);
                                secondL = Long.parseLong(seconds);
                            } else if (timeParts.length == 3) {
                                String hours = timeParts[0];
                                String minutes = timeParts[1];
                                String seconds = timeParts[2];
                                hourL = Long.parseLong(hours);
                                minuteL = Long.parseLong(minutes);
                                secondL = Long.parseLong(seconds);
                            } else {
                                Log.e("TimeSplit", "Invalid time format: " + time);
                            }
                            countDownTimer(hourL, minuteL, secondL, questId);

                        }
                    } else {
                        // Handle the error
                        Exception e = task.getException();
                        // Log the error or display an error message
                    }
                });
    }

    private ConstraintLayout findQuestLayoutById(int questId) {
        // Loop through the grid or other layout to find the corresponding quest by questId
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            LinearLayout group = (LinearLayout) gridLayout.getChildAt(i);
            for (int j = 0; j < group.getChildCount(); j++) {
                ConstraintLayout questLayout = (ConstraintLayout) group.getChildAt(j);
                if ((int) questLayout.getTag() == questId) {
                    return questLayout;
                }
            }
        }
        return null; // Return null if quest not found
    }

    // to update scroll view visibility
    private void updateScrollViewVisibility() {
        if (groupCount >= 1) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
        }
    }

}