package com.example.ian.travelsafe;

import java.util.List;

/**
 * Created by temp2015 on 20/11/2015.
 */
interface GetRoutesCallback {

        public abstract void done(List<RouteDetails> returnedRoutes);

}
