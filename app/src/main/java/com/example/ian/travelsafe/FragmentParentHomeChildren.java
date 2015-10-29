package com.example.ian.travelsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        View view =inflater.inflate(R.layout.fragment_parent_home_children, container, false);
        final Context context = view.getContext();
        FloatingActionButton fabAddNewChild = (FloatingActionButton) view.findViewById(R.id.fabAddNewChild);
        fabAddNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new user with this button", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(context, RegisterNewChild.class);
                startActivity(i);
            }
        });
        return view;
    }

/*
    public HashMap<Integer, ChildDetails> GetChildInfo() {

//        String selectedImagePath = "C:\Users\temp2015\AndroidStudioProjects\TravelSafe\app\src\main\res\drawable-mdpi\child_placeholder.png";
//        ImageView img;
//        img = (ImageView) findViewById(R.drawable.child_placeholder);
//        Bitmap yourSelectedImage = BitmapFactory.decodeFile(selectedImagePath);
//        img.setImageBitmap(yourSelectedImage);

        HashMap<Integer, ChildDetails> mapChildDetails = new HashMap();
        //mapChildDetails.put(1, new ChildDetails("Thomas Moran", new Image(), "School Route 1"));


        return mapChildDetails;
    }*/
}