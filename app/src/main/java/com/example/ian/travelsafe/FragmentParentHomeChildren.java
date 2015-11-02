package com.example.ian.travelsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeChildren extends Fragment {


    private TextView mText;
    private TextView mRoute;
    private ImageView mProfileImage;
    private List<ChildDetails> mData = new ArrayList<>();

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

        // Floating Action Button
        FloatingActionButton fabAddNewChild = (FloatingActionButton) view.findViewById(R.id.fabAddNewChild);
        fabAddNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add new user with this button", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent i = new Intent(context, RegisterNewChild.class);
                startActivity(i);
            }
        });

        //List of Children
        final List<ChildDetails> childList = getChildDetails();
        ListAdapter childrenAdapter = new ChildListAdapter(this.getContext(), childList);
        ListView childrenListView = (ListView) view.findViewById(R.id.listOfChildren);
        childrenListView.setAdapter(childrenAdapter);
//
        childrenListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChildDetails cd = (ChildDetails) parent.getItemAtPosition(position);
                        String childName = cd.mName;
                        String childRoute = cd.mCurrentRoute;

                        Snackbar.make(view, "Do something with "+childName + ": "+childRoute, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
        );


        return view;

    }

    private List<ChildDetails> getChildDetails() {

        List<ChildDetails> list= new ArrayList<>();
        list.add(new ChildDetails("Tom", R.drawable.child_placeholder, "School route 1"));
        list.add(new ChildDetails("Ian", R.drawable.child_placeholder, "School route 1"));
        list.add(new ChildDetails("Conor", R.drawable.child_placeholder, "School route 1"));
        list.add(new ChildDetails("Jamie", R.drawable.child_placeholder, "School route 1"));
        list.add(new ChildDetails("Brian", R.drawable.child_placeholder, "Grans House"));

        return list;
    }
}