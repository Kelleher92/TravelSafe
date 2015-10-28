package com.example.ian.travelsafe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class FragmentParentHomeAccount extends Fragment {

    private RecyclerView recyclerView;

    public static FragmentParentHomeAccount newInstance() {
        return new FragmentParentHomeAccount();
    }

    public FragmentParentHomeAccount() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_parent_home_account, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.ParentAccountDrawerList);
        return layout;
    }
}