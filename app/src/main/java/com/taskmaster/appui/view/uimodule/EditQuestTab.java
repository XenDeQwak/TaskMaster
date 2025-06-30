package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;

import java.util.ArrayList;
import java.util.List;

import java.time.*;

public class EditQuestTab extends FrameLayout {

    EditText editQuestName, editQuestHour, editQuestMinute, editQuestSecond, editQuestDescription, editQuestRewardExtra;
    Button editQuestSave, editQuestCancel;
    Spinner editQuestRewardPicker, editQuestChildPicker;
    ImageView editQuestChildAvatar;
    ConstraintLayout editQuestContainer;
    RatingBar editQuestDifficulty;

    Quest q;

    public EditQuestTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressWarnings("newApi")
    private void init () {
        LayoutInflater.from(getContext()).inflate(R.layout.module_edit_quest_tab, this);

        editQuestName = findViewById(R.id.editQuestName);
        editQuestHour = findViewById(R.id.editQuestHour);
        editQuestMinute = findViewById(R.id.editQuestMinute);
        editQuestSecond = findViewById(R.id.editQuestSecond);
        editQuestDescription = findViewById(R.id.editQuestDescription);
        editQuestRewardPicker = findViewById(R.id.editQuestRewardPicker);
        editQuestSave = findViewById(R.id.editQuestSave);
        editQuestCancel = findViewById(R.id.editQuestCancel);
        editQuestRewardExtra = findViewById(R.id.editQuestRewardExtra);
        editQuestChildPicker = findViewById(R.id.editQuestChildPicker);
        editQuestChildAvatar = findViewById(R.id.editQuestChildAvatar);
        editQuestContainer = findViewById(R.id.editQuestContainer);
        editQuestDifficulty = findViewById(R.id.editQuestDifficulty);

        List<String> rewardStatList = new ArrayList<>();
        rewardStatList.add("STRENGTH");
        rewardStatList.add("INTELLIGENCE");
        ArrayAdapter<String> rewardStatAdapter = new ArrayAdapter<>(
                this.getContext(),
                android.R.layout.simple_spinner_item,
                rewardStatList
        );
        rewardStatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editQuestRewardPicker.setAdapter(rewardStatAdapter);

        List<Child> childList = new ArrayList<>();
        ChildManager.injectToList(childList, e -> {
            ArrayAdapter<String> childAdapter = new ArrayAdapter<>(
                    this.getContext(),
                    android.R.layout.simple_spinner_item,
                    childList.stream().map(Child::getChildEmail).toList()
            );
            childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            editQuestChildPicker.setAdapter(childAdapter);
        });

        editQuestCancel.getBackground().setAlpha(150);

        editQuestSave.setOnClickListener(v -> {
            this.setVisibility(View.GONE);
            saveQuest();
        });

        editQuestCancel.setOnClickListener(v -> {
            this.setVisibility(View.GONE);
        });
    }

    public void setQuest (Quest q) {
        this.q = q;
    }

    @SuppressWarnings("NewApi")
    private void saveQuest () {

        if (
                editQuestName.getText().toString().equals("")
                || editQuestHour.getText().toString().equals("")
                || editQuestMinute.getText().toString().equals("")
                || editQuestSecond.getText().toString().equals("")
                || editQuestDescription.getText().toString().equals("")
                || editQuestRewardExtra.getText().toString().equals("")
        ) {
            Toast.makeText(getContext(), "Please fill up all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = editQuestName.getText().toString();
        int hour = Integer.parseInt(editQuestHour.getText().toString());
        int minute = Integer.parseInt(editQuestMinute.getText().toString());
        int second = Integer.parseInt(editQuestSecond.getText().toString());
        String description = editQuestDescription.getText().toString();
        String rewardStat = editQuestRewardPicker.getSelectedItem().toString();
        String rewardExtra = editQuestRewardExtra.getText().toString();
        String assigneeEmail = editQuestChildPicker.getSelectedItem().toString();
        Long difficulty = (long) editQuestDifficulty.getRating();


        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC"));
        System.out.println(zdt);
        long startDate = zdt.toEpochSecond();
        long endDate = zdt.plusHours(hour).plusMinutes(minute).plusSeconds(second).toEpochSecond();

        q.setName(name);
        q.setDescription(description);
        q.setStartDate(startDate);
        q.setEndDate(endDate);
        q.setRewardStat(rewardStat);
        q.setRewardExtra(rewardExtra);
        q.setDifficulty(difficulty);

        Task<QuerySnapshot> fetchAssigneeDetails = FirestoreManager
                .getFirestore()
                .collection("Childs")
                .whereEqualTo("Email", assigneeEmail)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot c = task.getResult().getDocuments().get(0);
                    q.setAssignedUID(c.getId());
                    q.setAssignedReference(c.getReference());

                    FirestoreManager.updateQuest(q);
                });

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

    public EditText getEditQuestRewardExtra() {
        return editQuestRewardExtra;
    }

    public void setEditQuestRewardExtra(EditText editQuestRewardExtra) {
        this.editQuestRewardExtra = editQuestRewardExtra;
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

    public Spinner getEditQuestRewardPicker() {
        return editQuestRewardPicker;
    }

    public void setEditQuestRewardPicker(Spinner editQuestRewardPicker) {
        this.editQuestRewardPicker = editQuestRewardPicker;
    }

    public Spinner getEditQuestChildPicker() {
        return editQuestChildPicker;
    }

    public void setEditQuestChildPicker(Spinner editQuestChildPicker) {
        this.editQuestChildPicker = editQuestChildPicker;
    }

    public ImageView getEditQuestChildAvatar() {
        return editQuestChildAvatar;
    }

    public void setEditQuestChildAvatar(ImageView editQuestChildAvatar) {
        this.editQuestChildAvatar = editQuestChildAvatar;
    }

    public ConstraintLayout getEditQuestContainer() {
        return editQuestContainer;
    }

    public void setEditQuestContainer(ConstraintLayout editQuestContainer) {
        this.editQuestContainer = editQuestContainer;
    }
}
