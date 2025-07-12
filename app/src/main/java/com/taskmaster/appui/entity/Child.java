package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;
import com.taskmaster.appui.data.ChildData;
import com.taskmaster.appui.view.uimodule.ChildBoxPreview;

import java.util.List;

public class Child {

    private ChildData childData;
    private ChildBoxPreview childBoxPreview;

    public Child(){}

    public Child (ChildData childData) {
        this.childData = new ChildData(childData);
    }

    public ChildData getChildData() {
        return childData;
    }

    public void setChildData(ChildData childData) {
        this.childData = childData;
    }
}
