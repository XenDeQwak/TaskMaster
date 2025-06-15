package com.taskmaster.appui.manager.entitymanager;


import android.content.Context;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ChildManager {

    private ArrayList<Child> childList;
    private Context context;

    private TemporaryConnectionManager tempConnectionManager;



    public ChildManager () {
        this.childList = new ArrayList<>();
    }

    public ChildManager (ArrayList<Child> childList) {
        this.childList = childList;
    }

    public ChildManager(Context context) {
        this.context = context;
    }

    public static Child parseChildData (Map<String, Object> cd) {
        return new Child(
                (String) cd.get("Email"),
                (String) cd.getOrDefault("Password", null),
                (String) cd.get("Username"),
                (String) cd.get("Firstname"),
                (String) cd.get("Lastname"),
                (String) cd.get("ParentUID"),
                (DocumentReference) cd.get("ParentRef")
        );
    }

    public static Map<String, Object> packChildData (Child c) {
        Map<String, Object> cd = new HashMap<>();
        cd.put("Email", c.getChildEmail());
        cd.put("Password", c.getChildPassword());
        cd.put("Username", c.getChildUsername());
        cd.put("Firstname", c.getChildFirstname());
        cd.put("Lastname", c.getChildLastname());
        cd.put("ParentUID", c.getParentUID());
        cd.put("ParentRef", c.getParentUID());

        return cd;
    }


    public void addChild(Child c, String cpass) {
        c.setChildPassword(cpass);

        childList.add(c);
        tempConnectionManager = new TemporaryConnectionManager();
        tempConnectionManager.start(context);
        tempConnectionManager.uploadChild(c);
        tempConnectionManager.end();
    }


    public void addChild() {
        addChild(createTestChild(), "testchild");
    }

    private Child createTestChild() {
        Random r = new Random();
        return new Child(
                "testchild" + r.nextInt(100) + "@email.com",
                "testchild",
                "testchild",
                "test",
                "child",
                "user",
                FirestoreManager.getFirestore().collection("Users").document("user")
        );
    }



}
