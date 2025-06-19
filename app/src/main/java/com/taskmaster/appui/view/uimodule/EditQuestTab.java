package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.type.DateTime;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.Calendar;

public class EditQuestTab extends FrameLayout {

    EditText editQuestName, editQuestHour, editQuestMinute, editQuestSecond, editQuestDescription;
    Button setRewardsButton, editQuestSave, editQuestCancel, editQuestAssignNext, editQuestAssignPrev;
    ImageView editQuestChildAvatar;
    ConstraintLayout editQuestContainer;

    Quest q;



    public EditQuestTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_edit_quest_tab, this);

        editQuestName = findViewById(R.id.editQuestName);
        editQuestHour = findViewById(R.id.editQuestHour);
        editQuestMinute = findViewById(R.id.editQuestMinute);
        editQuestSecond = findViewById(R.id.editQuestSecond);
        editQuestDescription = findViewById(R.id.editQuestDescription);
        setRewardsButton = findViewById(R.id.setRewardsButton);
        editQuestSave = findViewById(R.id.editQuestSave);
        editQuestCancel = findViewById(R.id.editQuestCancel);
        editQuestAssignNext = findViewById(R.id.editQuestAssignNext);
        editQuestAssignPrev = findViewById(R.id.editQuestAssignPrev);
        editQuestChildAvatar = findViewById(R.id.editQuestChildAvatar);
        editQuestContainer = findViewById(R.id.editQuestContainer);

        editQuestCancel.getBackground().setAlpha(150);

        editQuestSave.setOnClickListener(v -> {
            this.setVisibility(View.GONE);
            saveQuest();
        });

        editQuestCancel.setOnClickListener(v -> {
            this.setVisibility(View.GONE);
        });
    }

    private void saveQuest () {
        String name = editQuestName.getText().toString();
//        int hour = Integer.parseInt(editQuestHour.getText().toString());
//        int minute = Integer.parseInt(editQuestMinute.getText().toString());
//        int second = Integer.parseInt(editQuestSecond.getText().toString());
//        String description = editQuestDescription.getText().toString();

//        Calendar c = Calendar.getInstance();
//        int daynow = c.get(Calendar.DAY_OF_YEAR);
//        int hournow = c.get(Calendar.HOUR_OF_DAY);
//        int minutenow =  c.get(Calendar.MINUTE);
//        int secondnow = c.get(Calendar.SECOND);
//
//        long startDate =
//                (2025) * 100000000
//                        + daynow * 100000
//                        + hournow * 3600
//                        + minutenow * 60
//                        + secondnow;
//
//        long endDate =
//                (2025) * 100000000
//                        + (daynow + (int)(hour + hournow)/24) * 100000
//                        + ((int)(hour + hournow)%24) * 3600
//                        + ((int)(minute + minutenow)%60) * 60
//                        + ((int)(second + secondnow)%60);

        q.setName(name);
//        q.setStartDate(startDate);
//        q.setEndDate(endDate);
        // Below Unimplemented
        q.setRewardStat("DEFAULT");
        q.setRewardExtra("Extra Rewards");
        q.setAssignedUID("child");
        q.setAssignedReference(FirestoreManager.getFirestore().document("Childs/child"));

        FirestoreManager.updateQuest(q);
    }

    public void setQuest (Quest q) {
        this.q = q;
    }

    public EditText getEditQuestName() {
        return editQuestName;
    }

    public void setEditQuestName(EditText editQuestName) {
        this.editQuestName = editQuestName;
    }

    public EditText getEditQuestHour() {
        return editQuestHour;
    }

    public void setEditQuestHour(EditText editQuestHour) {
        this.editQuestHour = editQuestHour;
    }

    public EditText getEditQuestMinute() {
        return editQuestMinute;
    }

    public void setEditQuestMinute(EditText editQuestMinute) {
        this.editQuestMinute = editQuestMinute;
    }

    public EditText getEditQuestSecond() {
        return editQuestSecond;
    }

    public void setEditQuestSecond(EditText editQuestSecond) {
        this.editQuestSecond = editQuestSecond;
    }

    public EditText getEditQuestDescription() {
        return editQuestDescription;
    }

    public void setEditQuestDescription(EditText editQuestDescription) {
        this.editQuestDescription = editQuestDescription;
    }

    public Button getSetRewardsButton() {
        return setRewardsButton;
    }

    public void setSetRewardsButton(Button setRewardsButton) {
        this.setRewardsButton = setRewardsButton;
    }

    public Button getEditQuestSave() {
        return editQuestSave;
    }

    public void setEditQuestSave(Button editQuestSave) {
        this.editQuestSave = editQuestSave;
    }

    public Button getEditQuestCancel() {
        return editQuestCancel;
    }

    public void setEditQuestCancel(Button editQuestCancel) {
        this.editQuestCancel = editQuestCancel;
    }

    public Button getEditQuestAssignNext() {
        return editQuestAssignNext;
    }

    public void setEditQuestAssignNext(Button editQuestAssignNext) {
        this.editQuestAssignNext = editQuestAssignNext;
    }

    public Button getEditQuestAssignPrev() {
        return editQuestAssignPrev;
    }

    public void setEditQuestAssignPrev(Button editQuestAssignPrev) {
        this.editQuestAssignPrev = editQuestAssignPrev;
    }

    public ImageView getEditQuestChildAvatar() {
        return editQuestChildAvatar;
    }

    public void setEditQuestChildAvatar(ImageView editQuestChildAvatar) {
        this.editQuestChildAvatar = editQuestChildAvatar;
    }
}
