/**
 * Created by temp2015 on 27/10/2015.
 */

package com.example.ian.travelsafe;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.*;

public class FragmentParentHomeChildren extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FragmentParentHomeChildren newInstance(int sectionNumber) {
        FragmentParentHomeChildren fragment = new FragmentParentHomeChildren();
        //Bundle args = new Bundle();
        //args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        //fragment.setArguments(args);
        return fragment;
    }

    public FragmentParentHomeChildren() {  //constructor method of our class.
    }
    Button ClickMe;
    TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_parent_home_children, container, false);
        ClickMe = (Button) rootView.findViewById(R.id.buttonChildTab);
        tv = (TextView) rootView.findViewById(R.id.TabChildren);

        ClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv.getText().toString().contains("Hello")){
                    tv.setText("Hi");
                }else tv.setText("Hello");
            }
        });
        return rootView;
    }  // This is the end of our MyFragments Class
}