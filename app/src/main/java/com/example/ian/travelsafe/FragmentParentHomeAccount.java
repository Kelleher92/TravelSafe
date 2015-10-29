package com.example.ian.travelsafe;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeAccount extends Fragment {

    private RecyclerView recyclerView;
//    private VivsAdapter adapter;

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
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_parent_home_account, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.ParentAccountList);
        adapter = new VivsAdapter(getActivity(), getAccountListTypes());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public static List<ParentAccountListDetails> getAccountListTypes() {

        List<ParentAccountListDetails> accountListTypes = new ArrayList<>();
        int[] icons = {R.drawable.ic_pause_light, R.drawable.ic_pause_light, R.drawable.ic_pause_light};
        String[] titles = {"Email Address", "Password", "Log out"};

        for (int i = 0; i < titles.length && i < icons.length; i++) {
            ParentAccountListDetails listTypes = new ParentAccountListDetails();
            listTypes.iconId = icons[i];
            listTypes.title = titles[i];
            accountListTypes.add(listTypes);
        }

        return accountListTypes;
    }*/
}