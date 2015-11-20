package com.example.ian.travelsafe;

public class NotificationDetails {
    String childName = "";
    String  note = "";


    public NotificationDetails(String mChildName, String mText) {
        childName = mChildName;
        note = mText;
    }

    public String getChildName() {
        return childName;
    }

    public String getNote() {
        return note;
    }

    public void setChildName(String cn) {
        this.childName = cn;
    }

    public void setNote(String n) {
        this.note = n;
    }
}
