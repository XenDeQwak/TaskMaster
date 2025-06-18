package com.taskmaster.appui.view.parent;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.taskmaster.appui.R;
import com.taskmaster.appui.util.DropdownUtil;
import com.taskmaster.appui.util.NavUtil;

import java.util.ArrayList;
import java.util.List;

public class ManageChild extends AppCompatActivity {
    AppCompatButton addChildButton, copyButton, exitButton;
    ImageButton openChildPage;
    TextView codeText, childName;
    Context context = this;
    Group popUpGroup;
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
        NavUtil.hideSystemBars(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.statContainer), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // hooks
        addChildButton = findViewById(R.id.addChildButton);
        popUpGroup = findViewById(R.id.pop_up_tavern_code);
        copyButton = findViewById(R.id.copy_button);
        exitButton = findViewById(R.id.exit_button);
        codeText = findViewById(R.id.code_text);
        gridLayout = findViewById(R.id.gridLayout);
        rootLayout = findViewById(R.id.statContainer);
        gridLayout1 = findViewById(R.id.gridLayout1);
        DropdownUtil.dropdownSetup(this,rootLayout);

        // hide dropdown group and initial icons
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
                                            for (QueryDocumentSnapshot documentSnapshot : tasks.getResult()) {
                                                String childId = documentSnapshot.getId();
                                                db.collection("users").document(childId).get()
                                                        .addOnCompleteListener(childTask -> {
                                                            if (childTask.isSuccessful()) {
                                                                DocumentSnapshot childDocument = childTask.getResult();
                                                                if (childDocument.exists()) {
                                                                    String childName = childDocument.getString("username");
                                                                    int childStr = childDocument.getLong("childStr").intValue();
                                                                    int childInt = childDocument.getLong("childInt").intValue();
                                                                    int childAvatar = childDocument.getLong("childAvatar").intValue();
                                                                    int floorCount = childDocument.getLong("floor").intValue();
                                                                    int questCount = childDocument.getLong("questCount").intValue();
                                                                    createChildFrame(childName, childStr, childInt, childAvatar, floorCount, questCount);
                                                                } else {
                                                                    Log.d("DEBUG", "CHILD DOCUMENT DOES NOT EXIST");
                                                                }
                                                            } else {
                                                                Log.e("DEBUG", "Error getting child document", childTask.getException());
                                                            }
                                                        });
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

        // hide popup group
        popUpGroup.setVisibility(View.GONE);

        // view popup group
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ChildManager cc = new ChildManager(context);
                //cc.addChild(); // Keep empty for testing

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


    private void createChildFrame(String childName, int strStat, int intStat, int childAvatar, int floorCount, int questCount) {

        int frameH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
        int marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int childFrameH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 94, getResources().getDisplayMetrics());
        int childFrameW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 316, getResources().getDisplayMetrics());
        int childMarginEnd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics());
        int childMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        int childMarginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 137, getResources().getDisplayMetrics());
        int childMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        int childAvatarW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 108, getResources().getDisplayMetrics());
        int childAvatarH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 107, getResources().getDisplayMetrics());
        int childAvatarStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 11, getResources().getDisplayMetrics());

        int statH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int statW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, getResources().getDisplayMetrics());
        int strMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 132, getResources().getDisplayMetrics());
        int strMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        int intMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 132, getResources().getDisplayMetrics());
        int intMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        int floorMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 232, getResources().getDisplayMetrics());
        int floorMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, getResources().getDisplayMetrics());
        int questMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 232, getResources().getDisplayMetrics());
        int questMarginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());

        int nameMarginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 132, getResources().getDisplayMetrics());
        int nameMargingBot = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 89, getResources().getDisplayMetrics());
        int nameMarginEnd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics());
        int nameH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int nameW = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics());

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
        childFrame.setImageResource(R.drawable.button_shadowed);

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
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);

        childFrameAvatar.setImageResource(avatarImages.get(childAvatar));

        // STR Text
        AppCompatButton strText = new AppCompatButton(context);
        FrameLayout.LayoutParams strTextParams = new FrameLayout.LayoutParams(
                statW,
                statH
        );
        strTextParams.setMarginStart(strMarginStart);
        strTextParams.topMargin = strMarginTop;
        strText.setLayoutParams(strTextParams);
        strText.setText("STR: " + String.valueOf(strStat));
        strText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        strText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        strText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        strText.setTextColor(Color.parseColor("#3F3F3F"));
        strText.setAllCaps(false);
        strText.setBackgroundResource(R.drawable.button_shadowed);
        strText.setStateListAnimator(null);

        // INT Text
        AppCompatButton intText = new AppCompatButton(context);
        FrameLayout.LayoutParams intTextParams = new FrameLayout.LayoutParams(
                statW,
                statH
        );
        intTextParams.setMarginStart(intMarginStart);
        intTextParams.topMargin = intMarginTop;
        intText.setLayoutParams(intTextParams);
        intText.setText("INT: " + String.valueOf(intStat));
        intText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        intText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        intText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        intText.setTextColor(Color.parseColor("#3F3F3F"));
        intText.setAllCaps(false);
        intText.setBackgroundResource(R.drawable.button_shadowed);
        intText.setStateListAnimator(null);

        // Name Text
        AppCompatButton nameText = new AppCompatButton(context);
        FrameLayout.LayoutParams nameTextParams = new FrameLayout.LayoutParams(
                nameW,
                nameH
        );
        nameTextParams.setMarginStart(nameMarginStart);
        nameTextParams.bottomMargin = nameMargingBot;
        nameTextParams.setMarginEnd(nameMarginEnd);
        nameText.setLayoutParams(nameTextParams);
        nameText.setText(childName);
        nameText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        nameText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        nameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        nameText.setTextColor(Color.parseColor("#3F3F3F"));
        nameText.setAllCaps(false);
        nameText.setBackgroundResource(R.drawable.button_shadowed);
        nameText.setStateListAnimator(null);

        // Quest Text
        AppCompatButton questText = new AppCompatButton(context);
        FrameLayout.LayoutParams questTextParams = new FrameLayout.LayoutParams(
                statW,
                statH
        );
        questTextParams.setMarginStart(questMarginStart);
        questTextParams.topMargin = questMarginTop;
        questText.setLayoutParams(questTextParams);
        questText.setText("Quest: " + questCount);
        questText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        questText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        questText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        questText.setTextColor(Color.parseColor("#3F3F3F"));
        questText.setAllCaps(false);
        questText.setBackgroundResource(R.drawable.button_shadowed);
        questText.setStateListAnimator(null);

        // Floor Text
        AppCompatButton floorText = new AppCompatButton(context);
        FrameLayout.LayoutParams floorTextParams = new FrameLayout.LayoutParams(
                statW,
                statH
        );
        floorTextParams.setMarginStart(floorMarginStart);
        floorTextParams.topMargin = floorMarginTop;
        floorText.setLayoutParams(floorTextParams);
        floorText.setText("Floor: " + floorCount);
        floorText.setTypeface(ResourcesCompat.getFont(context, R.font.eb_garamond_semibold));
        floorText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        floorText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        floorText.setTextColor(Color.parseColor("#3F3F3F"));
        floorText.setAllCaps(false);
        floorText.setBackgroundResource(R.drawable.button_shadowed);
        floorText.setStateListAnimator(null);

        // Add views to newFrame
        newFrame.addView(childFrame);
        newFrame.addView(childFrameAvatar);
        newFrame.addView(strText);
        newFrame.addView(intText);
        newFrame.addView(nameText);
        newFrame.addView(questText);
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

}