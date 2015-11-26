package com.example.ian.travelsafe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeChildren extends Fragment {

    List<ChildDetails> children = new ArrayList<>();
    public static List<ChildDetails> childList = new ArrayList<>();
    static ListView childrenListView;
    private View view;
    public static Context context;
    public static List<RouteDetails> routes = new ArrayList<>();
    public static List<RouteDetails> routeList = new ArrayList<>();
    public static ChildDetails childClicked = new ChildDetails(null);

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
        context = this.getContext();
        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(this.getContext());
        final Users returnedUser = userLocalStore.getLoggedInUser();

        Log.i("MyActivity", "returnedUser id is = " + returnedUser.get_id() + " email is " + returnedUser.get_emailAddress());

        getChildren(returnedUser);
        getCurrentRouteList();
        Log.i("MyActivity", "***** returned list is = " + ParentRouteList.getCurrentRouteList());

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_parent_home_children, container, false);
        final Context context = view.getContext();

        // Floating Action Button
        FloatingActionButton fabAddNewChild = (FloatingActionButton) view.findViewById(R.id.fabAddNewChild);
        fabAddNewChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childList.clear();
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

                        childClicked = cd;

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
        if (childList.isEmpty()) {
            view.findViewById(R.id.listOfChildren).setVisibility(View.GONE);
            view.findViewById(R.id.addChildMessage).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.listOfChildren).setVisibility(View.VISIBLE);
            view.findViewById(R.id.addChildMessage).setVisibility(View.GONE);
        }
    }

    private void getChildren(Users user) {
        childList.clear();
        ServerRequests serverRequests = new ServerRequests(this.getContext());
        Log.i("MyActivity", "user id is = " + user.get_id());
        serverRequests.fetchChildDataInBackground(user.get_id(), children, new GetChildrenCallback() {
            @Override
            public void done(List<ChildDetails> returnedChildren) {
                if (returnedChildren == null) {
                    showErrorMessage();
                    Log.i("MyActivity", "No child returned");
                } else {
                    children = returnedChildren;

                    for (int x = 0; x < returnedChildren.size(); x++) {
                        ParentChildList.addToChildList(returnedChildren.get(x));
                    }
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

    public static void deRegisterChild(ChildDetails child) {
        ServerRequests serverRequests = new ServerRequests(FragmentParentHomeChildren.childrenListView.getContext());
        serverRequests.removeChildInBackground(child);
    }

    public static void getCurrentRouteList() {
        routeList.clear();
        Log.i("MyActivity", "retrieving list");
        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(context);
        final Users returnedUser = userLocalStore.getLoggedInUser();
        ServerRequests serverRequests = new ServerRequests(context);
        Log.i("GetRoutes", "user id is = " + returnedUser.get_id());
        serverRequests.fetchRouteDataInBackground(returnedUser.get_id(), routes, new GetRoutesCallback() {
            @Override
            public void done(List<RouteDetails> returnedRoutes) {
                if (returnedRoutes == null) {
                    Log.i("GetRoutes", "No routes returned");
                    routes = null;
                } else {
                    routes = returnedRoutes;
                    ParentRouteList.clearRouteList();
                    for (int x = 0; x < returnedRoutes.size(); x++) {
//                        routeList.add(returnedRoutes.get(x));
                        ParentRouteList.addToRouteList(returnedRoutes.get(x));
                    }
                }
            }
        });
    }
}