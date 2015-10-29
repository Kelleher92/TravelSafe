package com.example.ian.travelsafe;

import android.media.Image;

public class ChildDetails {

    String mName;
    Image mProfileImage;
    String mCurrentRoute;

    public void ChildDetails(String name, Image profileImage, String currentRoute) {
        mName = name;
        mProfileImage = profileImage;
        mCurrentRoute = currentRoute;
    }

}
