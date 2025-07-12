package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;

import java.util.ArrayList;
import java.util.List;

public class ChildBoxPreview extends FrameLayout {

    Child c;

    public ChildBoxPreview(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChildBoxPreview(@NonNull Context context) {
        super(context);
        init();
    }

    public ChildBoxPreview(@NonNull Context context, @Nullable Child c) {
        super(context);
        setChild(c);
        init();
    }

    private void setChild (Child c) {
        this.c = c;
    }

    List<Integer> avatarImages = new ArrayList<>();
    private void setUpAvatar() {
        avatarImages.add(R.drawable.placeholderavatar5_framed_round);
        avatarImages.add(R.drawable.placeholderavatar1_framed_round);
        avatarImages.add(R.drawable.placeholderavatar2_framed_round);
        avatarImages.add(R.drawable.placeholderavatar3_framed_round);
        avatarImages.add(R.drawable.placeholderavatar4_framed_round);
//        childBarName.setText(childDocument.getString("Username"));
//        childBarAvatar.setImageResource(avatarImages.get(childDocument.getDouble("Avatar").intValue()));
    }

    TextView viewChildName, viewChildEmail, vewChildStrength, vewChildIntelligence, viewChildFloor, viewChildQuestCompleted;
    ImageView viewChildAvatar;
    ConstraintLayout childViewCont;

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_child_view, this);

        viewChildName = findViewById(R.id.viewChildName);
        viewChildEmail = findViewById(R.id.viewChildEmail);
        vewChildStrength = findViewById(R.id.viewChildStrength);
        vewChildIntelligence = findViewById(R.id.viewChildIntelligence);
        viewChildFloor = findViewById(R.id.viewChildFloor);
        viewChildQuestCompleted = findViewById(R.id.viewChildQuestCompleted);
        viewChildAvatar = findViewById(R.id.viewChildAvatar);
        childViewCont = findViewById(R.id.viewChildContainer);

        viewChildName.setText(c.getChildData().getUsername());
        viewChildEmail.setText(c.getChildData().getEmail());
        vewChildStrength.setText("Strength: " + c.getChildData().getStrength());
        vewChildIntelligence.setText("Intelligence: " + c.getChildData().getIntelligence());
        viewChildFloor.setText("Floor: " + c.getChildData().getFloor());
        viewChildQuestCompleted.setText("Quest Completed: " + c.getChildData().getQuestsCompleted());

        setUpAvatar();
        viewChildAvatar.setImageResource(avatarImages.get(c.getChildData().getAvatar()));

        childViewCont.getBackground().setAlpha(160);

    }
}
