package com.taskmaster.appui.data;

import com.google.firebase.firestore.DocumentSnapshot;
import com.taskmaster.appui.util.GenericCallback;

public class AuthUserData {

    String uid, email, username, role;
    DocumentSnapshot userSnapshot;

    public AuthUserData (){}

    public AuthUserData(String uid, String email, String username, String role, DocumentSnapshot userSnapshot) {
        this.uid = uid;
        this.email = email;
        this.username = username;
        this.role = role;
        this.userSnapshot = userSnapshot;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public DocumentSnapshot getUserSnapshot() {
        return userSnapshot;
    }

    public void setUserSnapshot(DocumentSnapshot userSnapshot, GenericCallback<?> callback) {
        this.userSnapshot = userSnapshot;
        if (userSnapshot == null) return;
        userSnapshot.getReference().get().addOnCompleteListener(task -> {
            DocumentSnapshot ds = task.getResult();
            this.uid = ds.getString("id");
            this.email = ds.getString("email");
            this.username = ds.getString("username");
            this.role = ds.getString("role");
            callback.onCallback(null);
        });
    }
}
