package com.taskmaster.appui.view.uimodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.taskmaster.appui.R;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.entity.Quest;
import com.taskmaster.appui.manager.entitymanager.ChildManager;
import com.taskmaster.appui.manager.entitymanager.QuestManager;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.util.DateTimeUtil;

import java.util.ArrayList;
import java.util.List;

public class EditQuestTab extends FrameLayout {

    EditText editQuestName, editQuestHour, editQuestMinute, editQuestSecond, editQuestDescription, editQuestRewardExtra;
    Button editQuestSave, editQuestCancel;
    Spinner editQuestRewardPicker, editQuestChildPicker;
    ImageView editQuestChildAvatar;
    ConstraintLayout editQuestContainer;
    RatingBar editQuestDifficulty;

    Quest q;

    List<String> rewardStatList;
    List<Child> childList;

    public EditQuestTab(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //init();
    }

    public EditQuestTab(@NonNull Context context) {
        super(context);
        //init();
    }

    @SuppressWarnings("newApi")
//    private void init () {
//        LayoutInflater.from(getContext()).inflate(R.layout.module_edit_quest_tab, this);
//
//        editQuestName = findViewById(R.id.editQuestName);
//        editQuestHour = findViewById(R.id.editQuestHour);
//        editQuestMinute = findViewById(R.id.editQuestMinute);
//        editQuestSecond = findViewById(R.id.editQuestSecond);
//        editQuestDescription = findViewById(R.id.editQuestDescription);
//        editQuestRewardPicker = findViewById(R.id.editQuestRewardPicker);
//        editQuestSave = findViewById(R.id.editQuestSave);
//        editQuestCancel = findViewById(R.id.editQuestCancel);
//        editQuestRewardExtra = findViewById(R.id.editQuestRewardExtra);
//        editQuestChildPicker = findViewById(R.id.editQuestChildPicker);
//        editQuestChildAvatar = findViewById(R.id.editQuestChildAvatar);
//        editQuestContainer = findViewById(R.id.editQuestContainer);
//        editQuestDifficulty = findViewById(R.id.editQuestDifficulty);
//
//        rewardStatList = new ArrayList<>();
//        rewardStatList.add("STRENGTH");
//        rewardStatList.add("INTELLIGENCE");
//        ArrayAdapter<String> rewardStatAdapter = new ArrayAdapter<>(
//                this.getContext(),
//                android.R.layout.simple_spinner_item,
//                rewardStatList
//        );
//        rewardStatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        editQuestRewardPicker.setAdapter(rewardStatAdapter);
//
//        childList = new ArrayList<>();
//        FirebaseUser currentUser = AuthManager.getAuth().getCurrentUser();
//        ChildManager.injectToList(currentUser.getUid(), childList, e -> {
//            ArrayAdapter<String> childAdapter = new ArrayAdapter<>(
//                    this.getContext(),
//                    android.R.layout.simple_spinner_item,
//                    childList.stream().map(Child::getChildEmail).toList()
//            );
//            childAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            editQuestChildPicker.setAdapter(childAdapter);
//        });
//
//        editQuestSave.setOnClickListener(v -> {
//            saveQuest();
//        });
//
//        editQuestCancel.setOnClickListener(v -> {
//            ViewGroup parent = (ViewGroup) this.getParent();
//            parent.removeView(this);
//        });
//    }

//    @SuppressWarnings("newApi")
//    public void setQuest (Quest q) {
//        this.q = q;
//        if (q.getStatus().equalsIgnoreCase("Awaiting Configuration")) return;
//
//        editQuestName.setText(q.getName());
//        editQuestDescription.setText(q.getDescription());
//        editQuestRewardPicker.setSelection(rewardStatList.indexOf(q.getRewardStat().toUpperCase()));
//        editQuestRewardExtra.setText(q.getRewardExtra());
//        editQuestDifficulty.setRating(q.getDifficulty().intValue());
//
//        q.getAssignedReference().get().addOnCompleteListener(task -> {
//            Object email = task.getResult().get("Email");
//            if (email == null) return;
//            editQuestChildPicker.setSelection(
//                    childList.stream().map(Child::getChildEmail).toList().indexOf(email.toString())
//            );
//        });
//
//    }

//    @SuppressWarnings("NewApi")
//    private void saveQuest () {
//
//        if (
//                editQuestName.getText().toString().equals("")
//                || editQuestHour.getText().toString().equals("")
//                || editQuestMinute.getText().toString().equals("")
//                || editQuestSecond.getText().toString().equals("")
//                || editQuestDescription.getText().toString().equals("")
//                || editQuestDifficulty.getRating() < 1
//        ) {
//            Toast.makeText(getContext(), "Please fill up all fields!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String name = editQuestName.getText().toString();
//        int hour = Integer.parseInt(editQuestHour.getText().toString());
//        int minute = Integer.parseInt(editQuestMinute.getText().toString());
//        int second = Integer.parseInt(editQuestSecond.getText().toString());
//        String description = editQuestDescription.getText().toString();
//        String rewardStat = editQuestRewardPicker.getSelectedItem().toString();
//        String rewardExtra = editQuestRewardExtra.getText().toString();
//        String assigneeEmail = editQuestChildPicker.getSelectedItem().toString();
//        Long difficulty = (long) editQuestDifficulty.getRating();
//
//
//        long startDate = DateTimeUtil.getDateTimeNow().toEpochSecond();
//        long endDate = DateTimeUtil.getDateTimeFromNow(hour, minute, second).toEpochSecond();
//
//        q.setName(name);
//        q.setDescription(description);
//        q.setStartDate(startDate);
//        q.setEndDate(endDate);
//        q.setRewardStat(rewardStat);
//        q.setRewardExtra(rewardExtra);
//        q.setDifficulty(difficulty);
//
//        FirestoreManager.getFirestore()
//                .collection("Childs")
//                .whereEqualTo("Email", assigneeEmail)
//                .limit(1)
//                .get()
//                .addOnCompleteListener(task -> {
//                    DocumentSnapshot c = task.getResult().getDocuments().get(0);
//                    q.setAssignedUID(c.getId());
//                    q.setAssignedReference(c.getReference());
//                    q.setStatus("Ongoing");
//
//                    FirestoreManager.updateQuest(q);
//                });
//
//        ViewGroup parent = (ViewGroup) this.getParent();
//        parent.removeView(this);
//    }

    private void clearFields () {
        editQuestName.setText("");
        editQuestHour.setText("");
        editQuestMinute.setText("");
        editQuestSecond.setText("");
        editQuestDescription.setText("");
        editQuestRewardPicker.setSelection(0);
        editQuestRewardExtra.setText("");
        editQuestChildPicker.setSelection(0);
        editQuestDifficulty.setRating(0);
    }
}
