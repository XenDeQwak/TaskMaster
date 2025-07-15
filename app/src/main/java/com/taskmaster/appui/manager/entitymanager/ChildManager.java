package com.taskmaster.appui.manager.entitymanager;


import android.content.Context;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.entity.Child;
import com.taskmaster.appui.manager.firebasemanager.AuthManager;
import com.taskmaster.appui.manager.firebasemanager.FirestoreManager;
import com.taskmaster.appui.manager.firebasemanager.TemporaryConnectionManager;
import com.taskmaster.appui.util.GenericCallback;
import com.taskmaster.appui.view.uimodule.ChildBoxPreview;

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


    public void addChild(Child c) {
        childList.add(c);
    }

    public void loadChildrenFromFirestore(LinearLayout childCont) {
        childList.clear();
        FirebaseUser parent = AuthManager.getAuth().getCurrentUser();
        FirestoreManager.fetchAdventurers(parent.getUid(), dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Child c = new Child(ds.toObject(ChildData.class));
                childList.add(c);

                ChildBoxPreview cb = new ChildBoxPreview(context ,c);
                childCont.addView(cb);
            }
        });
    }

    public static void injectToList(String id, List<Child> list, GenericCallback<?> callback) {
        FirestoreManager.fetchAdventurers(id, dsl -> {
            for (DocumentSnapshot ds : dsl) {
                Child c = new Child(ds.toObject(ChildData.class));
                list.add(c);
            }
            callback.onCallback(null);
        });
    }

}