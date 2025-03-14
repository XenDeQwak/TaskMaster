package com.taskmaster.appui;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestManagement extends AppCompatActivity {

    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;
    private FirebaseAuth auth;
    ConstraintLayout newParentLayout;
    FirebaseFirestore db;
    CollectionReference questInfo;
    String storedUsername;
    String userId;
    ImageButton imagebutton1, imagebutton3, imagebutton4, imagebutton5, openQuestButton, rewardsStrButton, rewardsIntButton, assignAdvButton1, assignAdvButton2;
    AppCompatButton addQuestButton, dropdownNavButton, navQuestPage, navManageAdv, navLogOut, assignAdv, assignPrevBtn, assignNextBtn, setRewardsButton, viewRewardsDropdownButton, assignQuestButton, cancelQuestEditButton, saveQuestEditButton, rewardsDropdownButton, rewardsCancelButton, rewardsConfirmButton, viewRewardsButton, viewRewardsExitButton, cancelQuestViewButton, finishQuestViewButton, childBarName, childBarFloorCount, childBarStatsButton, cancelQuestViewButtonC,finishQuestViewButtonC, cancelQuestViewButtonP,approveQuestViewButtonP,rejectQuestViewButtonP, viewNotifOkayButton, assignDropdownButton, assignCancelButton, assignConfirmButton;
    ImageView questFrame, questNameFrame, questImage, questImageIcon, imageView23, imageView18, assignDropdownFrame, popupAssignFrame, editQuestImageIcon, viewQuestImageIcon, imageView19, basePageFrame, popupRewardsFrameShadow, popupRewardsFrame, rewardsDropdownFrame, viewQuestFrame, viewQuestImage, viewDifficultyBG, childBarFrame, childBarAvatar;
    TextView questNameText, rewardsStr, rewardsInt, textView8, viewNotifTextMsg, textView5, basePageTitle;
    EditText editQuestTime, editQuestName, editQuestDesc, viewQuestName, viewQuestTime, viewQuestDesc;
    ScrollView scrollView;
    Group dropDownGroup, editQuestGroup, popupRewardsGroup, viewQuestGroup, childBarGroup, viewQuestGroupButtonC, viewQuestGroupButtonP, popupViewRewardsGroup, popupViewNotif, popupAssignGroup;
    GridLayout gridLayout;
    LinearLayout newGroup;
    ConstraintLayout newQuest;
    ConstraintSet constraintSet;
    RatingBar setDifficultyRating, viewDifficultyRating;
    String role;
    String questName, questDescription, time, rewardStat, rewardOptional;
    int difficulty;
    DocumentReference questRef;
    int questDifficulty;
    String rewardType;
    //quest count
    int groupCount = 0;
    int questId = 1;
    List<String> childIds;
    int defaultHour, defaultMinute;
    int childAvatar;

    int questWidth, questHeight, imageWidth, imageHeight, nameFrameWidth, nameFrameHeight, topMarginImage, bottomMarginImage, topMarginNameFrame, bottomMarginNameFrame;
    Context context = this;
    View rootLayout;
    private int currentIndex;

    private int lastClickedQuestId = -1;

    private Map<Integer, TextView> questTextViews = new HashMap<>();
    private Map<Integer, String> questDescriptions = new HashMap<>();
    private Map<Integer, Integer> questRatings = new HashMap<>();
    private Map<Integer, String> questTimes = new HashMap<>();
    private Map<Integer, String> questRewardStat = new HashMap<>();
    private Map<Integer, String> questAssigned = new HashMap<>();
    private Map<Integer, String> questRewardOptional = new HashMap<>();
    private Map<Integer, TextView> viewQuestTextViews = new HashMap<>();
    private Map<Integer, String> viewQuestDescriptions = new HashMap<>();
    private Map<Integer, Integer> viewQuestRatings = new HashMap<>();
    private Map<Integer, String> viewQuestTimes = new HashMap<>();
    private Map<Integer, String> viewQuestRewardStat = new HashMap<>();
    private Map<Integer, String> viewQuestRewardOptional = new HashMap<>();
    private Map<String, Object> questData = new HashMap<>();
    private String username;
    private String parentCode;
    @SuppressLint("CutPasteId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quest_management);

        //database init
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        role = prefs.getString("role", "");

        FirebaseUser user = auth.getCurrentUser();
        userId = user.getUid();

        db.collection("users").document(userId).get()
                        .addOnCompleteListener(task -> {
                           if (task.isSuccessful()) {
                               DocumentSnapshot document = task.getResult();
                               if (document.exists()) {
                                   String code = document.getString("code");
                                   db.collection("users").whereEqualTo("parentCode", code).get()
                                           .addOnCompleteListener(tasks -> {
                                              if (tasks.isSuccessful()) {
                                                  childIds = new ArrayList<>();
                                                  for (QueryDocumentSnapshot documentSnapshot : tasks.getResult()) {
                                                      String childId = documentSnapshot.getId();
                                                      childIds.add(childId);
                                                  }
                                              }
                                           });
                               } else {
                                   Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                               }
                           }
                        });

        //child data init
        if ("child".equals(role)) {
            db.collection("users").document(userId).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot childDocument = task.getResult();
                            if (childDocument.exists()) {
                                String childToParentCode = childDocument.getString("parentCode");
                                childFetchQuest(childToParentCode, userId);
                                childAvatar = childDocument.getLong("childAvatar").intValue();
                                addQuestButton.setVisibility(View.GONE);
                                childBarGroup.setVisibility(View.VISIBLE);
                                childBarName.setText(childDocument.getString("username"));
                                childBarFloorCount.setText("Floor: " + childDocument.getLong("floor").intValue());

                                List<Integer> avatarImages = new ArrayList<>();
                                avatarImages.add(R.drawable.rectangle_rounded);
                                avatarImages.add(R.drawable.placeholderavatar1_framed);
                                avatarImages.add(R.drawable.placeholderavatar2_framed);
                                avatarImages.add(R.drawable.placeholderavatar3_framed);
                                avatarImages.add(R.drawable.placeholderavatar4_framed);

                                childBarAvatar.setImageResource(avatarImages.get(childAvatar));
                            } else {
                                Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                            }
                        } else {
                            Log.d("DEBUG", "Error getting parent document", task.getException());
                        }
                    });

        } else {
            //parent data init
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

        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        dropdownNavButton = findViewById(R.id.dropdownNavButton);
        addQuestButton = findViewById(R.id.addQuestButton);
        navQuestPage = findViewById(R.id.navQuestPage);
        navManageAdv = findViewById(R.id.navManageAdv);
        navLogOut = findViewById(R.id.navLogOut);

        dropDownGroup = findViewById(R.id.dropdownGroup);
        gridLayout = findViewById(R.id.gridLayout);
        rootLayout = findViewById(R.id.main);
        scrollView = findViewById(R.id.scrollView1);

        editQuestTime = findViewById(R.id.editQuestTime);
        editQuestName = findViewById(R.id.editQuestName);
        setRewardsButton = findViewById(R.id.setRewardsButton);
        assignQuestButton = findViewById(R.id.assignQuest);
        cancelQuestEditButton = findViewById(R.id.cancelQuestEditButton);
        saveQuestEditButton = findViewById(R.id.saveQuestEditButton);
        editQuestGroup = findViewById(R.id.editQuestGroup);
        setDifficultyRating = findViewById(R.id.setDifficultyRating);

        popupRewardsGroup = findViewById(R.id.popupRewardsGroup);
        popupRewardsFrameShadow = findViewById(R.id.popupRewardsFrameShadow);
        popupRewardsFrame = findViewById(R.id.popupRewardsFrame);
        rewardsDropdownFrame = findViewById(R.id.rewardsDropdownFrame);
        rewardsStrButton = findViewById(R.id.rewardsStrButton);
        rewardsIntButton = findViewById(R.id.rewardsIntButton);
        rewardsCancelButton = findViewById(R.id.rewardsCancelButton);
        rewardsConfirmButton = findViewById(R.id.rewardsConfirmButton);
        rewardsInt = findViewById(R.id.rewardsInt);
        rewardsStr = findViewById(R.id.rewardsStr);
        rewardsDropdownButton = findViewById(R.id.rewardsDropdownButton);

        viewQuestGroup = findViewById(R.id.viewQuestGroup);
        viewQuestFrame = findViewById(R.id.viewQuestFrame);
        viewQuestName = findViewById(R.id.viewQuestName);
        viewQuestTime = findViewById(R.id.viewQuestTime);
        viewQuestDesc = findViewById(R.id.viewQuestDesc);
        viewDifficultyBG = findViewById(R.id.viewDifficultyBG);
        viewDifficultyRating = findViewById(R.id.viewDifficultyRating);
        viewRewardsButton = findViewById(R.id.viewRewardsButton);

        editQuestImageIcon = findViewById(R.id.editQuestImageIcon);
        viewQuestImageIcon = findViewById(R.id.viewQuestImageIcon);

        popupViewRewardsGroup = findViewById(R.id.popupViewRewardsGroup);
        viewRewardsExitButton = findViewById(R.id.viewRewardsExitButton);

        viewQuestGroupButtonP = findViewById(R.id.viewQuestGroupButtonP);

        cancelQuestViewButtonP = findViewById(R.id.cancelQuestViewButtonP);
        approveQuestViewButtonP = findViewById(R.id.approveQuestViewButtonP);
        rejectQuestViewButtonP = findViewById(R.id.rejectQuestViewButtonP);

        childBarName = findViewById(R.id.childBarName);
        viewQuestGroupButtonC = findViewById(R.id.viewQuestGroupButtonC);

        cancelQuestViewButtonC = findViewById(R.id.cancelQuestViewButtonC);
        finishQuestViewButtonC = findViewById(R.id.finishQuestViewButtonC);


        popupViewNotif = findViewById(R.id.popupViewNotif);
        viewNotifTextMsg = findViewById(R.id.viewNotifTextMsg);
        viewNotifOkayButton = findViewById(R.id.viewNotifOkayButton);

        childBarGroup = findViewById(R.id.childBarGroup);
        childBarFrame = findViewById(R.id.childBarFrame);
        childBarName = findViewById(R.id.childBarName);
        childBarFloorCount = findViewById(R.id.childBarFloorCount);
        childBarStatsButton = findViewById(R.id.childBarStatsButton);
        childBarAvatar = findViewById(R.id.childBarAvatar);
        childBarAvatar = findViewById(R.id.childBarAvatar);

        basePageFrame = findViewById(R.id.basePageFrame);
        basePageTitle = findViewById(R.id.basePageTitle);
        textView5 = findViewById(R.id.textView5);
        imageView19 = findViewById(R.id.imageView19);
        imageView23 = findViewById(R.id.imageView23);

        popupAssignGroup = findViewById(R.id.popupAssignGroup);
        assignDropdownFrame = findViewById(R.id.assignDropdownFrame);
        assignDropdownButton = findViewById(R.id.assignDropdownButton);
        assignCancelButton = findViewById(R.id.assignCancelButton);
        assignConfirmButton = findViewById(R.id.assignConfirmButton);
        assignAdv = findViewById(R.id.assignAdv);
        popupAssignFrame = findViewById(R.id.popupAssignFrame);
        assignPrevBtn = findViewById(R.id.assignPrevBtn);
        assignNextBtn = findViewById(R.id.assignNextBtn);
        viewRewardsDropdownButton = findViewById(R.id.viewRewardsDropdownButton);

        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.navFrame)
        };

        // hide from child
        if ("child".equals(role)) {
            addQuestButton.setVisibility(View.GONE);
            imageView19.setVisibility(View.GONE);
            textView5.setVisibility(View.GONE);
            childBarGroup.setVisibility(View.VISIBLE);
            basePageFrame.setVisibility(View.VISIBLE);
            basePageTitle.setVisibility(View.VISIBLE);
            imageView23.setVisibility(View.GONE);
            navManageAdv.setText("Weekly Boss");
        } else if ("parent".equals(role)) {
            childBarGroup.setVisibility(View.GONE);
            basePageFrame.setVisibility(View.GONE);
            basePageTitle.setVisibility(View.GONE);
            imageView23.setVisibility(View.VISIBLE);
        }

        // hide popupRewardsGroup
        popupRewardsGroup.setVisibility(View.GONE);

        // hide editQuestGroup
        editQuestGroup.setVisibility(View.GONE);

        // hide viewQuestGroup
        viewQuestGroup.setVisibility(View.GONE);

        // remove scrollview visibility initially, keep this cause dropdown exit doesn't function as intended
        scrollView.setVisibility(View.GONE);

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);

        // view dropdown group
        dropdownNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownGroup.getVisibility() == View.VISIBLE) {
                    dropDownGroup.setVisibility(View.GONE);
                } else {
                    dropDownGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        assignNextBtn.setOnClickListener(e -> {
            currentIndex++;
            if (currentIndex >= childIds.size())
                currentIndex = 0;
            saveQuestToChild(childIds.get(currentIndex));
        });

        assignPrevBtn.setOnClickListener(e -> {
            currentIndex--;
            if (currentIndex < 0)
                currentIndex = childIds.size() - 1;
            saveQuestToChild(childIds.get(currentIndex));
        });



        addQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (childIds != null) {
                    if (groupCount >= 4) { // limit quests
                        Toast.makeText(QuestManagement.this, "Max quests reached!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    db.collection("users").whereEqualTo("uid", userId).get().addOnCompleteListener(
                            new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            parentCode = document.getString("code");
                                            username = document.getString("username");
                                            questInfo = db.collection("quest");
                                            questData.put("name", "");
                                            questData.put("description", "");
                                            questData.put("difficulty", 0);
                                            questData.put("time", "");
                                            questData.put("questTime", "");
                                            questData.put("rewardStat", "");
                                            questData.put("forVerif", false);
                                            questData.put("childId", "");
                                            questData.put("rewardOptional", "");
                                            questData.put("roomCode", parentCode);
                                            questData.put("questId", questId);
                                            questInfo.document(username + "Quest" + questId).set(questData);
                                            createQuest("", "", 0, "", "", "", false);

                                        }
                                    }
                                }

                            });
                } else {
                    viewNotifTextMsg.setText("You cannot make quests\nwithout your adventurers.");
                    popupViewNotif.setVisibility(View.VISIBLE);
                }

            }
        });

        navQuestPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "ur here", Toast.LENGTH_SHORT).show();
            }
        });

        navManageAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change dropdown specific for child
                if ("child".equals(role)) {
                    Toast.makeText(QuestManagement.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QuestManagement.this, WeeklyBoss.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(QuestManagement.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(QuestManagement.this, ManageChild.class);
                    startActivity(intent);
                }
            }
        });

        navLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, Splash.class);
                questData.clear();
                startActivity(intent);
            }
        });

        // cancel quest view child
        cancelQuestViewButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewQuestGroup.setVisibility(View.GONE);
                viewQuestGroupButtonC.setVisibility(View.GONE);
            }
        });

        // quest finish
        finishQuestViewButtonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if quest status pending false
                viewQuestGroup.setVisibility(View.GONE);
                viewQuestGroupButtonC.setVisibility(View.GONE);
                // quest status pending true
                DocumentReference docRef = db.collection("quest").document(storedUsername + "Quest" + (questId - 1));
                docRef.update("forVerif", true);
                docRef.get().addOnCompleteListener(tasks -> {
                    if (tasks.isSuccessful()) {
                        DocumentSnapshot document = tasks.getResult();
                        if (document.exists()) {
                            Boolean parentVerif = document.getBoolean("forVerif");
                            if (Boolean.TRUE.equals(parentVerif)) {
                                viewNotifTextMsg.setText("Quest status: Pending\nPlease wait\nfor confirmation!");
                                popupViewNotif.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                });
            }
        });

        // exit quest view parent
        cancelQuestViewButtonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewQuestGroup.setVisibility(View.GONE);
                viewQuestGroupButtonP.setVisibility(View.GONE);
                Toast.makeText(QuestManagement.this, "exit", Toast.LENGTH_SHORT).show();
            }
        });

        // quest view reject parent
        rejectQuestViewButtonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if quest status pending true

                // quest status pending false

                // else
                // show pop up "This quest is in progress"
                //viewNotifTextMsg.setText("Quest status: In Progress\\nPlease wait\\nuntil finished!");
                //popupViewNotif.setVisibility(View.VISIBLE);
            }
        });

        // quest view approval parent
        approveQuestViewButtonP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if quest status pending
                viewQuestGroup.setVisibility(View.GONE);
                viewQuestGroupButtonP.setVisibility(View.GONE);

                db.collection("quest").document(username + "Quest" + (questId - 1)).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Boolean isVerifTrue = document.getBoolean("forVerif");
                                    if (Boolean.TRUE.equals(isVerifTrue)) {
                                        questDifficulty = document.getLong("difficulty").intValue();
                                        rewardType = document.getString("rewardStat");

                                        //access child data
                                        db.collection("users").document(userId).get()
                                                .addOnCompleteListener(tasks -> {
                                                    if (tasks.isSuccessful()) {
                                                        DocumentSnapshot parentDocument = tasks.getResult();
                                                        if (parentDocument.exists()) {
                                                            if (childIds != null) {
                                                                for (String childId : childIds) {
                                                                    db.collection("users").document(childId).get()
                                                                            .addOnCompleteListener(childTask -> {
                                                                                if (childTask.isSuccessful()) {
                                                                                    DocumentSnapshot childDocument = childTask.getResult();
                                                                                    if (childDocument.exists()) {
                                                                                        Log.d("CHILD", childId);
                                                                                        DocumentReference childRef = db.collection("users").document(childId);
                                                                                        if ("strength".equals(rewardType)) {
                                                                                            childRef.update("childStr", childDocument.getLong("childStr").intValue() + questDifficulty);
                                                                                        } else {
                                                                                            childRef.update("childInt", childDocument.getLong("childInt").intValue() + questDifficulty);
                                                                                        }
                                                                                        childRef.update("questCount", childDocument.getLong("questCount").intValue() + 1);



                                                                                    } else {
                                                                                        Log.d("DEBUG", "CHILD DOCUMENT DOES NOT EXIST");
                                                                                    }
                                                                                } else {
                                                                                    Log.e("DEBUG", "Error getting child document", childTask.getException());
                                                                                }
                                                                            });
                                                                }
                                                            } else {
                                                                Log.d("DEBUG", "NO CHILDREN");
                                                            }
                                                        } else {
                                                            Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                                                        }
                                                    } else {
                                                        Log.d("DEBUG", "Error getting parent document", task.getException());
                                                    }
                                                });
                                        db.collection("quest").document(username + "Quest" + (questId - 1))
                                                .delete()
                                                .addOnSuccessListener(success -> {
                                                    Log.d("TAG", "Deletion success");
                                                })
                                                .addOnFailureListener(fail -> {
                                                    Log.d("TAG", "Can't delete");
                                                });
                                    } else {
                                        viewNotifTextMsg.setText("Quest status: In Progress\nPlease wait\nuntil finished!");
                                        popupViewNotif.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        });
                // grant stat reward
                // increase quest finished count
                // del quest

                // else
                // show pop up "This quest is in progress"
            }
        });

        viewNotifOkayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupViewNotif.setVisibility(View.GONE);
                Toast.makeText(QuestManagement.this, "exit", Toast.LENGTH_SHORT).show();
            }
        });

        // view reward
        viewRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupViewRewardsGroup.setVisibility(View.VISIBLE);
                viewRewardsDropdownButton.setText(rewardStat);
            }
        });

        viewRewardsExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupViewRewardsGroup.setVisibility(View.GONE);
            }
        });

        childBarStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "stats", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, ProgressionPage.class);
                startActivity(intent);
            }
        });

        // exit dropdown
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (dropDownGroup.getVisibility() == View.VISIBLE && event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isInsideDropdown = false;
                    for (View element : dropDownElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }
                    if (!isInsideDropdown) {
                        dropDownGroup.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });

        // quest time
        editQuestTime.setOnClickListener(v -> {
            defaultHour = 23;
            defaultMinute = 59;

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    context,
                    (view, hourOfDay, minute1) -> {
                        int displayHour = Math.min(hourOfDay, 24);
                        String selectedTime = String.format("%02d:%02d:00", displayHour, minute1);
                        editQuestTime.setText(selectedTime);
                        questTimes.put(lastClickedQuestId, selectedTime);
                    },
                    defaultHour, // Use the default hour
                    defaultMinute, // Use the default minute
                    true
            );
            timePickerDialog.show();
        });

        // set rewards
        setRewardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupRewardsGroup.setVisibility(View.VISIBLE);
                rewardsDropdownFrame.setVisibility(View.GONE);
                rewardsInt.setVisibility(View.GONE);
                rewardsStr.setVisibility(View.GONE);
                rewardsIntButton.setVisibility(View.GONE);
                rewardsStrButton.setVisibility(View.GONE);


            }
        });

        // assign quest
        assignDropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        rewardsDropdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rewardsDropdownFrame.getVisibility() == View.VISIBLE) {
                    rewardsDropdownFrame.setVisibility(View.GONE);
                    rewardsInt.setVisibility(View.GONE);
                    rewardsStr.setVisibility(View.GONE);
                    rewardsIntButton.setVisibility(View.GONE);
                    rewardsStrButton.setVisibility(View.GONE);
                    Toast.makeText(QuestManagement.this, "close", Toast.LENGTH_SHORT).show();
                } else {
                    rewardsDropdownFrame.setVisibility(View.VISIBLE);
                    rewardsInt.setVisibility(View.VISIBLE);
                    rewardsStr.setVisibility(View.VISIBLE);
                    rewardsIntButton.setVisibility(View.VISIBLE);
                    rewardsStrButton.setVisibility(View.VISIBLE);
                    Toast.makeText(QuestManagement.this, "open", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rewardsIntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questRewardStat.put(lastClickedQuestId, "intelligence");
                Log.d("QuestReward", "Quest " + lastClickedQuestId + ": set to intelligence");
                rewardsDropdownButton.setText("Intelligence");
                rewardsDropdownFrame.setVisibility(View.GONE);
                rewardsInt.setVisibility(View.GONE);
                rewardsStr.setVisibility(View.GONE);
                rewardsIntButton.setVisibility(View.GONE);
                rewardsStrButton.setVisibility(View.GONE);
                questImageIcon.setImageResource(R.drawable.icon_int);
                editQuestImageIcon.setImageResource(R.drawable.icon_int);
                viewQuestImageIcon.setImageResource(R.drawable.icon_int);
            }
        });

        rewardsStrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questRewardStat.put(lastClickedQuestId, "strength");
                Log.d("QuestReward", "Quest " + lastClickedQuestId + ": set to strength");
                rewardsDropdownButton.setText("Strength");
                rewardsDropdownFrame.setVisibility(View.GONE);
                rewardsInt.setVisibility(View.GONE);
                rewardsStr.setVisibility(View.GONE);
                rewardsIntButton.setVisibility(View.GONE);
                rewardsStrButton.setVisibility(View.GONE);
                questImageIcon.setImageResource(R.drawable.icon_str);
                editQuestImageIcon.setImageResource(R.drawable.icon_str);
                viewQuestImageIcon.setImageResource(R.drawable.icon_str);
            }
        });

        // redundant?
        rewardsCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardsDropdownFrame.setVisibility(View.GONE);
                rewardsInt.setVisibility(View.GONE);
                rewardsStr.setVisibility(View.GONE);
                rewardsIntButton.setVisibility(View.GONE);
                rewardsStrButton.setVisibility(View.GONE);
                popupRewardsGroup.setVisibility(View.GONE);
            }
        });

        // redundant?
        rewardsConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardsDropdownFrame.setVisibility(View.GONE);
                rewardsInt.setVisibility(View.GONE);
                rewardsStr.setVisibility(View.GONE);
                rewardsIntButton.setVisibility(View.GONE);
                rewardsStrButton.setVisibility(View.GONE);
                popupRewardsGroup.setVisibility(View.GONE);
            }
        });

        setDifficultyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    questRef = db.collection("quest").document(username + "Quest" + (questId - 1));
                    int intRating = (int) rating; // Cast to int
                    Toast.makeText(QuestManagement.this, "New rating: " + rating, Toast.LENGTH_SHORT).show();
                    questRatings.put(lastClickedQuestId, intRating);
                    viewQuestRatings.put(lastClickedQuestId, intRating);
                    questRef.update("difficulty", intRating);
                }
            }
        });

        // cancel quest edit
        cancelQuestEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "cancel quest edit", Toast.LENGTH_SHORT).show();
                editQuestGroup.setVisibility(View.GONE);
            }
        });

        // create quest edit
        saveQuestEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use lastClickedQuestId to update the correct quest
                questRef = db.collection("quest").document(username + "Quest" + (lastClickedQuestId));

                // Find the quest layout by questId
                ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId); // Use lastClickedQuestId to get the specific quest

                if (questLayout != null) {
                    // Get the correct TextView from the map
                    TextView questText = questTextViews.get(lastClickedQuestId); // Get the TextView using the questId
                    if (questText != null) {
                        questText.setText(editQuestName.getText().toString());
                        questRef.update("name", editQuestName.getText().toString());
                    }

                    // Update the quest description
                    EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Correct ID
                    if (editQuestDesc != null) {
                        questDescriptions.put(lastClickedQuestId, editQuestDesc.getText().toString());
                        questRef.update("description", editQuestDesc.getText().toString());
                    }

                    // Update the quest time
                    if (editQuestTime != null) {
                        questTimes.put(lastClickedQuestId, editQuestTime.getText().toString());
                        questRef.update("time", editQuestTime.getText().toString());
                    }

                    // Update questRewardsOptional
                    EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText);
                    if (editQuestRewardsOptional != null) {
                        questRewardOptional.put(lastClickedQuestId, editQuestRewardsOptional.getText().toString());
                        questRef.update("rewardOptional", editQuestRewardsOptional.getText().toString());
                    }

                    questRef.update("rewardStat", questRewardStat.get(lastClickedQuestId));

                    // Hide the editor after saving
                    editQuestGroup.setVisibility(View.GONE);
                    Toast.makeText(QuestManagement.this, "Quest " + lastClickedQuestId + " updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        DocumentReference questRef = db.collection("quest").document(username + "Quest" + (questId - 1));
        questRef.update("childId", childID);
    }

    private void fetchQuests(String parentCode) {
        db.collection("quest").whereEqualTo("roomCode", parentCode).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questName = document.getString("name");
                            questDescription = document.getString("description");
                            difficulty = document.getLong("difficulty").intValue();
                            time = document.getString("time");
                            rewardStat = document.getString("rewardStat");
                            rewardOptional = document.getString("rewardOptional");
                            Boolean forVerif = document.getBoolean("forVerif");
//                            questAssign = document.getString("questAssign");
                            Log.d("TAG", questName + questDescription + difficulty + time + rewardStat + rewardOptional);
                            // make quests depending on database
                            createQuest(questName, questDescription, difficulty, time, rewardStat, rewardOptional, forVerif);
//                            createQuest(questName, questDescription, difficulty, time, rewardStat, null, rewardOptional);
                            updateQuestIcon(rewardStat, forVerif);
                        }
                    } else {
                        Log.d("ERROR", "NOTHING HAPPENED");
                        return;
                    }
                });
    }


    public QuestManagement() {
        super();
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
        if ("strength".equals(rewardStat))
            questImage.setImageResource(R.drawable.icon_str);
        else if ("intelligence".equals(rewardStat))
            questImage.setImageResource(R.drawable.icon_int);


        // create quest image icon
        questImageIcon = new ImageView(context);
        questImageIcon.setId(View.generateViewId());
        ConstraintLayout.LayoutParams imageIconParams = new ConstraintLayout.LayoutParams(imageWidth, imageHeight);
        imageParams.topMargin = topMarginImage;
        imageParams.bottomMargin = bottomMarginImage;
        questImageIcon.setLayoutParams(imageIconParams);
        questImageIcon.setImageResource(R.drawable.blank_icon);
        questImageIcon.setAlpha(1f);

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
        openQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click for the correct questId
                if (dropDownGroup.getVisibility() == View.GONE) {

                    int clickedQuestId = (int) v.getTag();

                    // Keep track of the clicked quest layout (this will be passed to populateQuestEditor)
                    lastClickedQuestId = clickedQuestId; // Store the clicked quest layout

                    // Populate the editor fields
                    populateQuestEditor(clickedQuestId);

                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                        // Double click
                        if ("parent".equals(role)) {
                            Toast.makeText(QuestManagement.this, "Parent View Quest " + clickedQuestId, Toast.LENGTH_SHORT).show();
                            viewQuestGroup.setVisibility(View.VISIBLE); // for parent
                            viewQuestGroupButtonP.setVisibility(View.VISIBLE); // for parent

                        } else if ("child".equals(role)) {
                            viewQuestGroup.setVisibility(View.VISIBLE); // for child
                            viewQuestGroupButtonC.setVisibility(View.VISIBLE); // for child
                        }
                    } else {
                        // Single click
                        // move edit somewhere else
                        if ("parent".equals(role)) {
                            if (forVerif) {
                                viewQuestGroup.setVisibility(View.VISIBLE);
                                viewQuestGroupButtonP.setVisibility(View.VISIBLE);
                            }
                            else
                                editQuestGroup.setVisibility(View.VISIBLE);
                        }
                    }
                    lastClickTime = clickTime;
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

    private void populateQuestEditor(int questId) {
        // Find the specific quest layout using its questId
        ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId);

        // Find and update the quest components based on the selected quest
        if (questLayout != null) {
            // Get the correct TextView from the map
            TextView questText = questTextViews.get(questId); // Get the TextView using the questId

            EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Ensure this is the correct ID
            RatingBar setDifficultyRating = findViewById(R.id.setDifficultyRating);
            EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText); // Correct ID
            AppCompatButton rewardsDropdownButton = findViewById(R.id.rewardsDropdownButton);

            EditText viewQuestName = findViewById(R.id.viewQuestName);
            EditText viewQuestDesc = findViewById(R.id.viewQuestDesc);
            EditText viewQuestTime = findViewById(R.id.viewQuestTime);

            RatingBar viewDifficultyRating = findViewById(R.id.viewDifficultyRating);

            // Set the editor fields with the quest's details
            if (questText != null) {
                editQuestName.setText(questText.getText().toString());
                viewQuestName.setText(questText.getText().toString());
            }
            if (editQuestDesc != null) {

                db.collection("quest").document(username + "Quest" + questId).get()
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

                db.collection("quest").document(username + "Quest" + questId).get()
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
                db.collection("quest").document(username + "Quest" + questId).get()
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
                db.collection("quest").document(username + "Quest" + questId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String currentQuestRewardOptional = document.getString("rewardOptional");
                                    editQuestRewardsOptional.setText(currentQuestRewardOptional);
                                }
                            }
                        });
            }

            String currentQuestReward = questRewardStat.get(questId);
            if (currentQuestReward.equals("intelligence")) {
                rewardsDropdownButton.setText("Intelligence");
            } else if (currentQuestReward.equals("strength")) {
                rewardsDropdownButton.setText("Strength");
            } else {
                rewardsDropdownButton.setText("Stat Increase");
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
        }
    }

    private void updateQuestIcon(String rewardStat, Boolean forVerif) {
        if (questImageIcon != null) {
            if (editQuestImageIcon != null) {
                if (rewardStat.equals("intelligence")) {
                    if (forVerif) {
                        questImageIcon.setImageResource(R.drawable.icon_int_pending);
                        editQuestImageIcon.setImageResource(R.drawable.icon_int_pending);
                        viewQuestImageIcon.setImageResource(R.drawable.icon_int_pending);
                    } else {
                        questImageIcon.setImageResource(R.drawable.icon_int);
                        editQuestImageIcon.setImageResource(R.drawable.icon_int);
                        viewQuestImageIcon.setImageResource(R.drawable.icon_int);
                    }
                } else if (rewardStat.equals("strength")) {
                    if (forVerif) {
                        questImageIcon.setImageResource(R.drawable.icon_str_pending);
                        editQuestImageIcon.setImageResource(R.drawable.icon_str_pending);
                        viewQuestImageIcon.setImageResource(R.drawable.icon_str_pending);
                    } else {
                        questImageIcon.setImageResource(R.drawable.icon_str);
                        editQuestImageIcon.setImageResource(R.drawable.icon_str);
                        viewQuestImageIcon.setImageResource(R.drawable.icon_str);
                    }
                } else {
                    questImageIcon.setImageResource(R.drawable.blank_icon);
                    editQuestImageIcon.setImageResource(R.drawable.blank_icon);
                    viewQuestImageIcon.setImageResource(R.drawable.blank_icon);
                }
            }

            // add for verif icon logic
//            if (forVerif && rewardStat.equals("intelligence")) {
//
//            } else if (forVerif && rewardStat.equals("strength")) {
//                questImageIcon.setImageResource(R.drawable.icon_str_pending);
//            } else {
//                questImageIcon.setImageResource(R.drawable.blank_icon);
//            }

        }
    }


    private void childFetchQuest(String parentCode, String childId) {
        db.collection("quest")
                .whereEqualTo("roomCode", parentCode)
                .whereEqualTo("childId", childId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            questName = document.getString("name");
                            questDescription = document.getString("description");
                            difficulty = document.getLong("difficulty").intValue();
                            time = document.getString("time");
                            rewardStat = document.getString("rewardStat");
                            rewardOptional = document.getString("rewardOptional");
                            Boolean forVerif = document.getBoolean("forVerif");
                            createQuest(questName, questDescription, difficulty, time, rewardStat, rewardOptional, forVerif);
                            updateQuestIcon(rewardStat, forVerif);
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

    // exclude elems within dropdown
    private boolean isViewTouched(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return event.getRawX() >= x && event.getRawX() <= x + view.getWidth()
                && event.getRawY() >= y && event.getRawY() <= y + view.getHeight();
    }

    // to update scroll view visibility
    private void updateScrollViewVisibility() {
        if (groupCount >= 1) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
        }
    }

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide the nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}