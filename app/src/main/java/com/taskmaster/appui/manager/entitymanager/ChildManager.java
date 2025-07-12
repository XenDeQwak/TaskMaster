package com.taskmaster.appui.manager.entitymanager;


import android.content.Context;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.view.uimodule.ChildViewPreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChildManager {

    private ArrayList<Child> childList;
    private Context context;

    private TemporaryConnectionManager tempConnectionManager;


    public ChildManager(ArrayList<Child> childList, Context context) {
        this.childList = childList;
        this.context = context;
    }

    public ChildManager(Context context) {
        this(new ArrayList<>(), context);
    }

    public static Child parseChildData(Map<String, Object> cd) {

        List<String> OwnedItems = (List<String>) cd.get("OwnedItems");

        return new Child(
                (String) cd.get("Email"),
                (String) cd.getOrDefault("Password", null),
                (String) cd.get("Username"),
                (String) cd.get("Firstname"),
                (String) cd.get("Lastname"),
                (String) cd.get("ParentUID"),
                (DocumentReference) cd.get("ParentRef"),
                ((Number) cd.get("Strength")).intValue(),
                ((Number) cd.get("Intelligence")).intValue(),
                ((Number) cd.get("Avatar")).intValue(),
                (Long) cd.get("BossTimer"),
                (Boolean) cd.get("BossAlive"),
                (Integer) cd.get("Floor"),
                (Integer) cd.get("Gold"),
                (Integer) cd.get("QuestCompleted"),
                (List<String>) cd.get("OwnedItems")
        );
    }

    public static Map<String, Object> packChildData(Child c) {
        Map<String, Object> cd = new HashMap<>();
        cd.put("Email", c.getChildEmail());
        cd.put("Password", c.getChildPassword());
        cd.put("Username", c.getChildUsername());
        cd.put("Firstname", c.getChildFirstname());
        cd.put("Lastname", c.getChildLastname());
        cd.put("ParentUID", c.getParentUID());
        cd.put("ParentRef", c.getParentRef());
        cd.put("Strength", c.getStrength());
        cd.put("Intelligence", c.getIntelligence());
        cd.put("Avatar", c.getAvatar());
        cd.put("BossTimer", c.getBossTimer());
        cd.put("BossAlive", c.getBossAlive());
        cd.put("Floor", c.getFloor());
        cd.put("Gold", c.getGold());
        cd.put("QuestCompleted", c.getQuestCompleted());
        cd.put("OwnedItems", c.getOwnedItems());
        return cd;
    }

    public static Child createTestChild() {
        Random r = new Random();
        return new Child(
                "testchild" + r.nextInt(100) + "@email.com",
                "testchild",
                "testchild",
                "test",
                "child",
                "currentUser",
                FirebaseFirestore.getInstance().document("Users/child"),
                0,
                0,
                0,
                0L,
                true,
                1,
                0,
                0,
                List.of("Armorless"));
    }

    public void addChild(Child c) {
        childList.add(c);
    }

    public void loadChildrenFromFirestore(LinearLayout childCont) {
        childList.clear();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchAdventurers(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Child c = ChildManager.parseChildData((HashMap<String, Object>) ds.getData());
                childList.add(c);

                ChildViewPreview cb = new ChildViewPreview(context ,c);
                childCont.addView(cb);
            }
        });
    }

    public static void injectToList(String id, List<Child> list, GenericCallback<?> callback) {
        FirestoreManager.fetchAdventurers(id, dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Child c = ChildManager.parseChildData((HashMap<String, Object>) ds.getData());
                list.add(c);
            }
            callback.onCallback(null);
        });
    }

    public static void grantReward (Child c) {

    }

}