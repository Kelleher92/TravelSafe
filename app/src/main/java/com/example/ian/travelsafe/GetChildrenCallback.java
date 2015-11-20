package com.example.ian.travelsafe;

import java.util.List;

/**
 * Created by ian on 13/11/2015.
 */
interface GetChildrenCallback {

    public abstract void done(List<ChildDetails> returnedChildren);

}
