package com.taskmaster.appui;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageChild extends AppCompatActivity {
    AppCompatButton addChildButton, dropdownNavButton, navQuestPage, navManageAdv, navLogOut, copyButton, exitButton;
    ImageButton openChildPage;
    TextView codeText, childName;
    Context context = this;
    Group dropDownGroup, popUpGroup;
    GridLayout gridLayout, gridLayout1;
    String tavernCode;
    View rootLayout;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String username;
    List<String> childIds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.manage_child);

        // hide status bar and nav bar
        hideSystemBars();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        dropdownNavButton = findViewById(R.id.dropdownNavButton);
        addChildButton = findViewById(R.id.addChildButton);
        navQuestPage = findViewById(R.id.navQuestPage);
        navManageAdv = findViewById(R.id.navManageAdv);
        navLogOut = findViewById(R.id.navLogOut);

        dropDownGroup = findViewById(R.id.dropdownGroup);
        popUpGroup = findViewById(R.id.pop_up_tavern_code);
        copyButton = findViewById(R.id.copy_button);
        exitButton = findViewById(R.id.exit_button);
        codeText = findViewById(R.id.code_text);
        gridLayout = findViewById(R.id.gridLayout);
        rootLayout = findViewById(R.id.main);
        gridLayout1 = findViewById(R.id.gridLayout1);

        // exclude elems within dropdown
        View[] dropDownElements = {
                findViewById(R.id.navFrame)
        };

        // hide dropdown group and initial icons
        dropDownGroup.setVisibility(View.GONE);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

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

                                                for (String childID : childIds) {
                                                    db.collection("users").document(childID).get()
                                                            .addOnCompleteListener(childTask -> {
                                                                if (childTask.isSuccessful()) {
                                                                    DocumentSnapshot childDocument = childTask.getResult();
                                                                    if (childDocument.exists()) {
                                                                        String childName = childDocument.getString("username");
                                                                        int childStr = childDocument.getLong("childStr").intValue();
                                                                        int childInt = childDocument.getLong("childInt").intValue();
                                                                        int childAvatar = childDocument.getLong("childAvatar").intValue();
                                                                        int floorCount = childDocument.getLong("floor").intValue();
                                                                        createChildFrame(childName, childStr, childInt, childAvatar, floorCount);
                                                                    } else {
                                                                        Log.d("DEBUG", "CHILD DOCUMENT DOES NOT EXIST");
                                                                    }
                                                                } else {
                                                                    Log.e("DEBUG", "Error getting child document", childTask.getException());
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });
                        } else {
                            Log.d("DEBUG", "PARENT DOCUMENT DOES NOT EXIST");
                        }
                    }
                });


        //parent data init
        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot parentDocument = task.getResult();
                        if (parentDocument.exists()) {
                            tavernCode = parentDocument.getString("code");
                            codeText.setText("Tavern Code: " + tavernCode);
                            if (childIds != null) {

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

        // exclude elems within popup
        View[] popupElements = {
                findViewById(R.id.pop_up_frame),
                findViewById(R.id.notice_text),
                findViewById(R.id.notice_shadow_overlay),
                copyButton,
                exitButton,
                codeText
        };

        // hide popup group
        popUpGroup.setVisibility(View.GONE);

        // view popup group
        addChildButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.VISIBLE) {
                    popUpGroup.setVisibility(View.GONE);
                } else {
                    popUpGroup.setVisibility(View.VISIBLE);
                }
            }
        });

//        openChildPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (popUpGroup.getVisibility() == View.GONE) {
//                    Toast.makeText(ManageChild.this, "Open Child Page ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        navManageAdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "ur here", Toast.LENGTH_SHORT).show();
                }
            }
        });

        navQuestPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "Move", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManageChild.this, QuestManagement.class);
                    startActivity(intent);
                }
            }
        });

        navLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popUpGroup.getVisibility() == View.GONE) {
                    Toast.makeText(ManageChild.this, "Log Out", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ManageChild.this, Splash.class);
                    startActivity(intent);
                }
            }
        });

        // exit dropdown & popup group
        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isInsideDropdown = false;

                    for (View element : dropDownElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }

                    for (View element : popupElements) {
                        if (isViewTouched(element, event)) {
                            isInsideDropdown = true;
                            break;
                        }
                    }

                    if (!isInsideDropdown) {
                        dropDownGroup.setVisibility(View.GONE);
                        popUpGroup.setVisibility(View.GONE);
                    }
                }
                return false;
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codeToCopy = tavernCode;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Tavern Code", codeToCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ManageChild.this, "Copied Tavern Code", Toast.LENGTH_SHORT).show();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpGroup.setVisibility(View.GONE);
                Toast.makeText(ManageChild.this, "Exit", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void createChildFrame(String childName, int strStat, int intStat, int childAvatar, int floorCount) {

        int frameH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
        int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int childFrameH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 84, getResources().getDisplayMetrics());
        int childFrameW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 316, getResources().getDisplayMetrics());
        int childMarginEnd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
        int childMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, getResources().getDisplayMetrics());
        int childMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 137, getResources().getDisplayMetrics());
        int childMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        int childAvatarW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
        int childAvatarH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 107, getResources().getDisplayMetrics());
        int childAvatarStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());
        int strMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, getResources().getDisplayMetrics());
        int strMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int intMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 195, getResources().getDisplayMetrics());
        int intMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int nameMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 125, getResources().getDisplayMetrics());
        int nameMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        int floorMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 258, getResources().getDisplayMetrics());
        int floorMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());


        //new framelayout
        FrameLayout newFrame = new FrameLayout(this);
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                frameH
        );
        frameParams.gravity = Gravity.CENTER;
        frameParams.bottomMargin = marginBottom;
        newFrame.setLayoutParams(frameParams);

        //child frame
        ImageView childFrame = new ImageView(context);
        FrameLayout.LayoutParams childFrameParams = new FrameLayout.LayoutParams(
                childFrameW,
                childFrameH
        );
        childFrameParams.setMarginStart(childMarginStart);
        childFrameParams.setMarginEnd(childMarginEnd);
        childFrameParams.topMargin = childMarginTop;
        childFrameParams.bottomMargin = childMarginBottom;
        childFrame.setLayoutParams(childFrameParams);
        childFrame.setImageResource(R.drawable.rectangle_rounded);

        //child frame avatar
        ImageView childFrameAvatar = new ImageView(context);
        FrameLayout.LayoutParams childFrameAvatarParams = new FrameLayout.LayoutParams(
                childAvatarW,
                childAvatarH
        );
        childFrameAvatarParams.setMarginStart(childAvatarStart);
        childFrameAvatar.setLayoutParams(childFrameAvatarParams);

        List<Integer>avatarImages = new ArrayList<>();
        avatarImages.add(R.drawable.rectangle_rounded);
        avatarImages.add(R.drawable.placeholderavatar1_framed);
        avatarImages.add(R.drawable.placeholderavatar2_framed);
        avatarImages.add(R.drawable.placeholderavatar3_framed);
        avatarImages.add(R.drawable.placeholderavatar4_framed);

        childFrameAvatar.setImageResource(avatarImages.get(childAvatar));

        // STR Text
        TextView strText = new TextView(context);
        FrameLayout.LayoutParams strTextParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        strTextParams.setMarginStart(strMarginStart);
        strTextParams.topMargin = strMarginTop;
        strText.setLayoutParams(strTextParams);
        strText.setText("STR " + String.valueOf(strStat));
        strText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        strText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        strText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // INT Text
        TextView intText = new TextView(context);
        FrameLayout.LayoutParams intTextParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        intTextParams.setMarginStart(intMarginStart);
        intTextParams.topMargin = intMarginTop;
        intText.setLayoutParams(intTextParams);
        intText.setText("INT " + String.valueOf(intStat));
        intText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        intText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        intText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Name Text
        TextView nameText = new TextView(context);
        FrameLayout.LayoutParams nameTextParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        nameTextParams.setMarginStart(nameMarginStart);
        nameTextParams.topMargin = nameMarginTop;
        nameText.setLayoutParams(nameTextParams);
        nameText.setText(childName);
        nameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Floor Text
        TextView floorText = new TextView(context);
        FrameLayout.LayoutParams floorTextParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        floorTextParams.setMarginStart(floorMarginStart);
        floorTextParams.topMargin = floorMarginTop;
        floorText.setLayoutParams(floorTextParams);
        floorText.setText("Floor: " + floorCount);
        floorText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        floorText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        floorText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        // Add views to newFrame
        newFrame.addView(childFrame);
        newFrame.addView(childFrameAvatar);
        newFrame.addView(strText);
        newFrame.addView(intText);
        newFrame.addView(nameText);
        newFrame.addView(floorText);
        gridLayout1.addView(newFrame);

    }

    // exclude elems within dropdown and popup
    private boolean isViewTouched(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        return event.getRawX() >= x && event.getRawX() <= x + view.getWidth()
                && event.getRawY() >= y && event.getRawY() <= y + view.getHeight();
    }

    private void hideSystemBars() {
        // hide status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // hide nav bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}