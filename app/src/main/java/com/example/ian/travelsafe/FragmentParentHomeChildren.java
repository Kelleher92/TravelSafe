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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class FragmentParentHomeChildren extends Fragment {

    List<ChildDetails> children = new ArrayList<>();
    private TextView mText;
    private TextView mRoute;
    private ImageView mProfileImage;
    public List<ChildDetails> childList = new ArrayList<>();
    static ListView childrenListView;
    private View view;

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

        UserLocalStore userLocalStore;
        userLocalStore = new UserLocalStore(this.getContext());
        final Users returnedUser = userLocalStore.getLoggedInUser();

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

        // Setup refresh listener which triggers new data loading
//        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_parent_children);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                getChildren(returnedUser);
////                swipeContainer.setRefreshing(false);
//            }
//        });

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
}