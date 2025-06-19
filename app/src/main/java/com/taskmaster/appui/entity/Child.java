package com.taskmaster.appui.entity;

import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

public class Child {

    String childEmail;
    String childPassword;
    String childUsername;
    String childFirstname;
    String childLastname;
    String parentUID;
    DocumentReference parentRef;


    public Child(String childEmail,
                 String childPassword,
                 String childUsername,
                 String childFirstname,
                 String childLastname,
                 String parentUID,
                 DocumentReference parentRef) {
        this.childEmail = childEmail;
        this.childPassword = childPassword;
        this.childUsername = childUsername;
        this.childFirstname = childFirstname;
        this.childLastname = childLastname;
        this.parentUID = parentUID;
        this.parentRef = parentRef;
    }


    public String getChildEmail() {
        return childEmail;
    }

    public void setChildEmail(String childEmail) {
        this.childEmail = childEmail;
    }

    public String getChildPassword() {
        return childPassword;
    }

    public void setChildPassword(String childPassword) {
        this.childPassword = childPassword;
    }

    public String getChildUsername() {
        return childUsername;
    }

    public void setChildUsername(String childUsername) {
        this.childUsername = childUsername;
    }

    public String getChildFirstname() {
        return childFirstname;
    }

    public void setChildFirstname(String childFirstname) {
        this.childFirstname = childFirstname;
    }

    public String getChildLastname() {
        return childLastname;
    }

    public void setChildLastname(String childLastname) {
        this.childLastname = childLastname;
    }

    public String getParentUID() {
        return parentUID;
    }

    public void setParentUID(String parentUID) {
        this.parentUID = parentUID;
    }

    public DocumentReference getParentRef() {
        return parentRef;
    }

    public void setParentRef(DocumentReference parentRef) {
        this.parentRef = parentRef;
    }
}
