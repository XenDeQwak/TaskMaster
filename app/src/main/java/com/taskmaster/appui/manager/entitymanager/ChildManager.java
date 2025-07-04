package com.taskmaster.appui.manager.entitymanager;


import android.content.Context;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.view.uimodule.ViewChild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Number stre = (Number) cd.get("Strength") ;
        Number inte = (Number) cd.get("Intelligence");
        Number avat = (Number) cd.get("Avatar");
        Number btime = (Number) cd.get("BossTimer");
        Number floor = (Number) cd.get("Floor");
        Number gold = (Number) cd.get("Gold");

        if (btime instanceof Long) {
            btime = ((Long) btime).doubleValue();
        } else if (btime instanceof Double) {
            btime = (Double) btime;
        } else {
            btime = null;
        }
        List<String> OwnedItems = (List<String>) cd.get("OwnedItems");

        return new Child(
                (String) cd.get("Email"),
                (String) cd.getOrDefault("Password", null),
                (String) cd.get("Username"),
                (String) cd.get("Firstname"),
                (String) cd.get("Lastname"),
                (String) cd.get("ParentUID"),
                (DocumentReference) cd.get("ParentRef"),
                stre.intValue(),
                inte.intValue(),
                avat.intValue(),
                btime.longValue(),
                (Boolean) cd.get("BossAlive"),
                floor.intValue(),
                gold.intValue(),
                OwnedItems
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
        cd.put("OwnedItems", c.getOwnedItems());
        return cd;
    }

//    public static Child createTestChild() {
//        Random r = new Random();
//        return new Child(
//                "testchild" + r.nextInt(100) + "@email.com",
//                "testchild",
//                "testchild",
//                "test",
//                "child",
//                "user",
//                FirestoreManager.getFirestore().collection("Users").document("user"),
//                0,
//                0,
//                0,
//                0L,
//                true,
//                1,
//                0,
//                OwnedItems);
//    }

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

                ViewChild cb = new ViewChild(context ,c);
                childCont.addView(cb);
            }
        });
    }

    public static void injectToList(List<Child> list, GenericCallback<?> callback) {
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchAdventurers(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Child c = ChildManager.parseChildData((HashMap<String, Object>) ds.getData());
                list.add(c);
            }
            callback.onCallback(null);
        });
    }

}