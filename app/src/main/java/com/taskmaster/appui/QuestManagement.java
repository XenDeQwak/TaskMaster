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
import android.view.LayoutInflater;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class QuestManagement extends AppCompatActivity {

    FirebaseFirestore db;
    ImageButton imagebutton1, imagebutton2, imagebutton3, imagebutton4, imagebutton5, openQuestButton, rewardsStrButton, rewardsIntButton;
    AppCompatButton setRewardsButton, assignQuestButton, cancelQuestEditButton, saveQuestEditButton, rewardsDropdownButton, rewardsCancelButton, rewardsConfirmButton;
    ImageView questFrame, questNameFrame, questImage, editQuestImage, popupRewardsFrameShadow, popupRewardsFrame, rewardsDropdownFrame;
    TextView questNameText, rewardsStr, rewardsInt;
    EditText editQuestTime, editQuestName, editQuestDesc;
    ScrollView scrollView;
    Group dropDownGroup, editQuestGroup, popupRewardsGroup;
    GridLayout gridLayout;
    LinearLayout newGroup;
    ConstraintLayout newQuest;
    ConstraintSet constraintSet;
    RatingBar setDifficultyRating;

    //quest count
    int groupCount = 0;
    int questId = 1;

    int questWidth, questHeight, imageWidth, imageHeight, nameFrameWidth, nameFrameHeight, topMarginImage, bottomMarginImage, topMarginNameFrame, bottomMarginNameFrame;
    Context context = this;
    View rootLayout;

    private int lastClickedQuestId = -1; // -1 or any default value to indicate no quest selected
    private int lastQuestId = -1;

    private Map<Integer, TextView> questTextViews = new HashMap<>();
    private Map<Integer, String> questDescriptions = new HashMap<>();
    private Map<Integer, Integer> questRatings = new HashMap<>();
    private Map<Integer, String> questTimes = new HashMap<>();
    private Map<Integer, String> questRewardStat = new HashMap<>();
    private Map<Integer, String> questRewardOptional = new HashMap<>();

    @SuppressLint("CutPasteId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.quest_management);

        // hide status bar and nav bar
        hideSystemBars();

        db = FirebaseFirestore.getInstance();

        SharedPreferences prefs = getSharedPreferences("User Prefs", MODE_PRIVATE);
        String roomCode = prefs.getString("roomCode", "defaultRoomCode");
        String role = prefs.getString("role", "parent");

        fetchQuests(roomCode);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        imagebutton1 = findViewById(R.id.imageButton2);
        imagebutton2 = findViewById(R.id.imageButton3);
        imagebutton3 = findViewById(R.id.imageButton4);
        imagebutton4 = findViewById(R.id.imageButton5);
        imagebutton5 = findViewById(R.id.imageButton6);
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

        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.imageView24),
                findViewById(R.id.imageView25),
                findViewById(R.id.imageView26),
                findViewById(R.id.imageView27),
                findViewById(R.id.textView7),
                findViewById(R.id.textView8),
                findViewById(R.id.textView9)
        };

        // hide popupRewardsGroup
        popupRewardsGroup.setVisibility(View.GONE);

        // hide editQuestGroup
        editQuestGroup.setVisibility(View.GONE);

        // remove scrollview visibility initially, keep this cause dropdown exit doesn't function as intended
        scrollView.setVisibility(View.GONE);

        // hide dropdown group
        dropDownGroup.setVisibility(View.GONE);

        // view dropdown group
        imagebutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dropDownGroup.getVisibility() == View.VISIBLE) {
                    dropDownGroup.setVisibility(View.GONE);
                } else {
                    dropDownGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        imagebutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (groupCount >= 4) { // limit quests
                    Toast.makeText(QuestManagement.this, "Max quests reached!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // convert px to dp
                questWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 172, getResources().getDisplayMetrics());
                questHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 285, getResources().getDisplayMetrics());
                imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 161, getResources().getDisplayMetrics());
                imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 227, getResources().getDisplayMetrics());
                nameFrameWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 162, getResources().getDisplayMetrics());
                nameFrameHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 39, getResources().getDisplayMetrics());
                topMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7, getResources().getDisplayMetrics());
                bottomMarginImage = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                topMarginNameFrame = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 241, getResources().getDisplayMetrics());
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
                Toast.makeText(context, "Quest ID: " + retrievedQuestId, Toast.LENGTH_SHORT).show();

                // create quest frame
                questFrame = new ImageView(context);
                questFrame.setId(View.generateViewId());
                int questFrameId = questFrame.getId();
                Toast.makeText(context, "QuestFrame ID: " + questFrameId, Toast.LENGTH_SHORT).show();
                questFrame.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.MATCH_PARENT,
                        ConstraintLayout.LayoutParams.MATCH_PARENT
                ));
                questFrame.setAlpha(0.5f);
                questFrame.setImageResource(R.drawable.rectangle_rounded);

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
                questImage.setAlpha(0.5f);
                questImage.setImageResource(R.drawable.rectangle_rounded);

                // create quest name text
                TextView currentQuestNameText = new TextView(context);
                currentQuestNameText.setId(View.generateViewId());
                currentQuestNameText.setLayoutParams(new ConstraintLayout.LayoutParams(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                ));
                currentQuestNameText.setText("Quest Name " + questId);
                currentQuestNameText.setTextSize(20);
                currentQuestNameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
                currentQuestNameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                questTextViews.put(questId, currentQuestNameText);

                // create quest description
                questDescriptions.put(questId, "");

                // store quest rating
                questRatings.put(questId, 0); // Default to 0 stars

                // Store the default time
                questTimes.put(questId, "23:59:00");

                // store quest reward stat
                questRewardStat.put(questId, "None");

                // store quest reward optional
                questRewardOptional.put(questId, "");

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
                            Toast.makeText(QuestManagement.this, "Edit Quest " + currentQuestId, Toast.LENGTH_SHORT).show();

                            int clickedQuestId = (int) v.getTag();

                            // Keep track of the clicked quest layout (this will be passed to populateQuestEditor)
                            lastClickedQuestId = clickedQuestId; // Store the clicked quest layout

                            // Populate the editor fields
                            populateQuestEditor(clickedQuestId);

                            // Show the edit panel
                            editQuestGroup.setVisibility(View.VISIBLE);
                        }
                    }
                });

                // add views to ConstraintLayout
                newQuest.addView(questFrame);
                newQuest.addView(questNameFrame);
                newQuest.addView(questImage);
                newQuest.addView(currentQuestNameText);
                newQuest.addView(openQuestButton);

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

                // test message
                Toast.makeText(QuestManagement.this, "New Quest Added: Quest " + questId, Toast.LENGTH_SHORT).show();

                groupCount++;
                questId++;
                updateScrollViewVisibility();
            }
        });

        imagebutton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "ur here", Toast.LENGTH_SHORT).show();
            }
        });

        imagebutton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "Move", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, ManageChild.class);
                startActivity(intent);
            }
        });

        imagebutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "Log Out", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuestManagement.this, Splash.class);
                startActivity(intent);
            }
        });

        ImageButton createQuestButton = findViewById(R.id.imageButton3);

        if ("child".equals(role)) {
            createQuestButton.setVisibility(View.GONE);
        }

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
            int defaultHour = 23;
            int defaultMinute = 59;

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
                Toast.makeText(QuestManagement.this, "open reward thing", Toast.LENGTH_SHORT).show();
            }
        });

        // assign quest
        assignQuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QuestManagement.this, "assign one child", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(QuestManagement.this, "set to int", Toast.LENGTH_SHORT).show();
            }
        });

        rewardsStrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questRewardStat.put(lastClickedQuestId, "strength");
                Log.d("QuestReward", "Quest " + lastClickedQuestId + ": set to strength");
                Toast.makeText(QuestManagement.this, "set to str", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(QuestManagement.this, "cancel rewards", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(QuestManagement.this, "confirm rewards", Toast.LENGTH_SHORT).show();
            }
        });

        setDifficultyRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    int intRating = (int) rating; // Cast to int
                    Toast.makeText(QuestManagement.this, "New rating: " + rating, Toast.LENGTH_SHORT).show();
                    questRatings.put(lastClickedQuestId, intRating);
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
                // Find the quest layout by questId
                ConstraintLayout questLayout = findQuestLayoutById(lastClickedQuestId); // Use lastClickedQuestId to get the specific quest

                if (questLayout != null) {
                    // Get the correct TextView from the map
                    TextView questText = questTextViews.get(lastClickedQuestId); // Get the TextView using the questId
                    if (questText != null) {
                        questText.setText(editQuestName.getText().toString());
                    }

                    // Update the quest description
                    EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Correct ID
                    if (editQuestDesc != null) {
                        questDescriptions.put(lastClickedQuestId, editQuestDesc.getText().toString()); // Update the description in the map
                    }

                    // Update the quest time
                    if (editQuestTime != null) {
                        questTimes.put(lastClickedQuestId, editQuestTime.getText().toString());
                    }

                    // Update the quest description
                    EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText); // Correct ID
                    if (editQuestRewardsOptional != null) {
                        questRewardOptional.put(lastClickedQuestId, editQuestRewardsOptional.getText().toString()); // Update the description in the map
                    }

                    // Hide the editor after saving
                    editQuestGroup.setVisibility(View.GONE);
                    Toast.makeText(QuestManagement.this, "Quest " + lastClickedQuestId + " updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateQuestEditor(int questId) {
        // Find the specific quest layout using its questId
        ConstraintLayout questLayout = findQuestLayoutById(questId);

        // Find and update the quest components based on the selected quest
        if (questLayout != null) {
            // Get the correct TextView from the map
            TextView questText = questTextViews.get(questId); // Get the TextView using the questId

            EditText editQuestDesc = findViewById(R.id.editQuestDesc); // Ensure this is the correct ID
            RatingBar setDifficultyRating = findViewById(R.id.setDifficultyRating);
            EditText editQuestRewardsOptional = findViewById(R.id.rewardsOptionalText); // Correct ID


            // Set the editor fields with the quest's details
            if (questText != null) {
                editQuestName.setText(questText.getText().toString());
            }
            if (editQuestDesc != null) {
                // Get the correct description from the map
                String currentQuestDesc = questDescriptions.get(questId);
                editQuestDesc.setText(currentQuestDesc);
            }
            if (setDifficultyRating != null) {
                // Get the correct rating from the map
                int currentQuestRating = questRatings.get(questId);
                setDifficultyRating.setRating(currentQuestRating);
            }
            if (editQuestTime != null) {
                // Get the correct time from the map
                String currentQuestTime = questTimes.get(questId);
                editQuestTime.setText(currentQuestTime);
            }
            if (editQuestRewardsOptional != null) {
                // Get the correct reward optional from the map
                String currentQuestRewardsOptional = questRewardOptional.get(questId);
                editQuestRewardsOptional.setText(currentQuestRewardsOptional);
            }

            String currentQuestReward = questRewardStat.get(questId);
            if(currentQuestReward.equals("intelligence")) {
                Log.d("QuestReward", "Quest " + questId + ": Intelligence");
            } else if (currentQuestReward.equals("strength")) {
                Log.d("QuestReward", "Quest " + questId + ": Strength");
            } else {
                Log.d("QuestReward", "Quest " + questId + ": None");
            }
        }
    }

    private void fetchQuests(String roomCode) {
        db.collection("quests")
                .whereEqualTo("roomCode", roomCode) // Filter by roomCode
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Quest quest = document.toObject(Quest.class); // Convert document to Quest object
                            displayQuest(quest); // Call method to display the quest
                        }
                    } else {
                        Toast.makeText(this, "Error fetching quests: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayQuest(Quest quest) {
        // Create a new LinearLayout for the quest
        LinearLayout newGroup = new LinearLayout(context);
        newGroup.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        newGroup.setOrientation(LinearLayout.HORIZONTAL);
        newGroup.setGravity(Gravity.CENTER);

        // Create a new ConstraintLayout for the quest
        ConstraintLayout newQuest = new ConstraintLayout(context);
        newQuest.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));

        // Create quest frame
        ImageView questFrame = new ImageView(context);
        questFrame.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT
        ));
        questFrame.setAlpha(0.5f);
        questFrame.setImageResource(R.drawable.rectangle_rounded);

        // Create quest name text
        TextView questNameText = new TextView(context);
        questNameText.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        ));
        questNameText.setText(quest.getName());
        questNameText.setTextSize(20);
        questNameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        questNameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Add views to ConstraintLayout
        newQuest.addView(questFrame);
        newQuest.addView(questNameText);

        // Add to LinearLayout
        newGroup.addView(newQuest);

        // Add to GridLayout
        gridLayout.addView(newGroup);
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