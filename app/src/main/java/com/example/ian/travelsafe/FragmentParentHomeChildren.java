package com.example.ian.travelsafe;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.imageio.*;

import java.io.File;
import java.util.HashMap;


public class FragmentParentHomeChildren extends Fragment {


    public static FragmentParentHomeChildren newInstance() {
        return new FragmentParentHomeChildren();
    }

    public FragmentParentHomeChildren() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent_home_children, container, false);
    }


    public HashMap<Integer, ChildDetails> GetChildInfo() {

//        String selectedImagePath = "C:\Users\temp2015\AndroidStudioProjects\TravelSafe\app\src\main\res\drawable-mdpi\child_placeholder.png";
//        ImageView img;
//        img = (ImageView) findViewById(R.drawable.child_placeholder);
//        Bitmap yourSelectedImage = BitmapFactory.decodeFile(selectedImagePath);
//        img.setImageBitmap(yourSelectedImage);

        HashMap<Integer, ChildDetails> mapChildDetails = new HashMap();
        //mapChildDetails.put(1, new ChildDetails("Thomas Moran", new Image(), "School Route 1"));


        return mapChildDetails;
    }
}