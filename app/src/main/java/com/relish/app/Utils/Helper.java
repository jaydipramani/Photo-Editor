package com.relish.app.Utils;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Helper {
    private static final FirebaseDatabase database;
    public static final DatabaseReference generalRef;
    private static Helper helper;
    public static final DatabaseReference mainRef;
    public static String typeAds = "Admob";

    private Context context;

    static {
        FirebaseDatabase instance = FirebaseDatabase.getInstance();
        database = instance;
        DatabaseReference reference = instance.getReference();
        mainRef = reference;
        generalRef = reference.child("general");
    }

    public static void initInstance(Context context2) {
        if (helper == null) {
            helper = new Helper(context2);
        }
    }

    public static void clean() {
        helper = null;
    }

    private Helper(Context context2) {
        this.context = context2;
    }


}
