package com.example.ian.travelsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeChildren extends Fragment {

    ChildDetails child = new ChildDetails(null, null);
    private TextView mText;
    private TextView mRoute;
    private ImageView mProfileImage;
    public List<ChildDetails> childList = new ArrayList<>();
    static ListView childrenListView;
    private View view;

    public static String childClickedUserName = "";

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
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(this.getContext());
        Users returnedUser = userLocalStore.getLoggedInUser();

        Log.i("MyActivity", "returnedUser id is = " + returnedUser.get_id() + " email is " + returnedUser.get_emailAddress());

        getChildren(returnedUser);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_home_children, container, false);
        final Context context = view.getContext();

        // Floating Action Button
        FloatingActionButton fabAddNewChild = (FloatingActionButton) view.findViewById(R.id.fabAddNewChild);
        fabAddNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RegisterNewChild.class);
                startActivity(i);
            }
        });

        childrenListView = (ListView) view.findViewById(R.id.listOfChildren);
        childList = ParentChildList.getCurrentChildList();
        IsChildListEmpty();
        ListAdapter childrenAdapter = new ChildListAdapter(this.getContext(), childList);

        childrenListView.setAdapter(childrenAdapter);

        childrenListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ChildDetails cd = (ChildDetails) parent.getItemAtPosition(position);

                        childClickedUserName = cd._name;

                        if (view.findViewById(R.id.space).getVisibility() == View.GONE) {
                            view.findViewById(R.id.space).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.deleteChild).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.changeRoute).setVisibility(View.VISIBLE);
                        } else {
                            view.findViewById(R.id.space).setVisibility(View.GONE);
                            view.findViewById(R.id.deleteChild).setVisibility(View.GONE);
                            view.findViewById(R.id.changeRoute).setVisibility(View.GONE);
                        }
                    }
                }

        );

        TextView tv = (TextView) view.findViewById(R.id.changeRoute);

        return view;
    }

    private void IsChildListEmpty() {
        if(childList.isEmpty()) {
            view.findViewById(R.id.listOfChildren).setVisibility(View.GONE);
            view.findViewById(R.id.addChildMessage).setVisibility(View.VISIBLE);
        }
        else{
            view.findViewById(R.id.listOfChildren).setVisibility(View.VISIBLE);
            view.findViewById(R.id.addChildMessage).setVisibility(View.GONE);
        }

    }


    private void getChildren(Users user) {
        ServerRequests serverRequests = new ServerRequests(this.getContext());
        Log.i("MyActivity", "user id is = " + user.get_id());
        serverRequests.fetchChildDataInBackground(user.get_id(), child, new GetChildCallback() {
            @Override
            public void done(ChildDetails returnedChild) {
                if (returnedChild == null) {
                    showErrorMessage();
                    Log.i("MyActivity", "No child returned");
                } else {
                    child = returnedChild;
                    Log.i("MyActivity", "child returned = " + child.get_id() + child.get_name() + child.get_username());

                    Log.i("MyActivity", "Returned child is NOT null");
                    /////////////////////////////////////////////////
                    //perform action here to create table of linked children
                    ParentChildList.addToChildList(child);
                    FragmentParentHomeChildren.childrenListView.invalidateViews();
                    IsChildListEmpty();

                }
            }
        });
    }

    private void showErrorMessage() {
        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this.getContext());
        dialogBuilder.setMessage("No children found");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }
}