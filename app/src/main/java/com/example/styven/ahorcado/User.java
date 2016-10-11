package com.example.styven.ahorcado;

/**
 * Created by Pelusa on 10/10/2016.
 */
public class User {
    public String email;
    public String uid;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email) {
        this.email = email;
        this.uid = uid;
    }

}
