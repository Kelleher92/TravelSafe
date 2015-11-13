package com.example.ian.travelsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temp2015 on 13/11/2015.
 */
public class ParentChildList extends ArrayList<ChildDetails> {

    public static List<ChildDetails> childList = new ArrayList<>();
    public void ParentChildList() {

    }

    public static List<ChildDetails> getCurrentChildList(){
        return childList;
    }

    public static void addToChildList(ChildDetails cd){

        if(cd == null) {
        }
        else {
            childList.add(cd);     // Add new Child to list
        }
    }

    public static void removeFromChildList(String childUserName) {

        // Remove child from database

        // Remove from local list.
        for(ChildDetails cd : childList){
            if(cd.mName == childUserName){
                childList.remove(cd);
            }
        }
        childList.remove(childUserName);
    }

}
