package com.example.alexandra.expendablechecklist;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Route {
    @SerializedName("legs")
    @Expose
    private List<Leg> legs = new ArrayList<Leg>();
    @SerializedName("overview_polyline")
    @Expose
    private OverviewPolyline overviewPolyline;

    public List<Integer> getWayponts_order() {
        return wayponts_order;
    }

    public void setWayponts_order(List<Integer> wayponts_order) {
        this.wayponts_order = wayponts_order;
    }

    @SerializedName("waypoints_order")
    @Expose
    private List<Integer> wayponts_order;

    /**
     *
     * @return
     * The legs
     */
    public List<Leg> getLegs() {
        return legs;
    }

    /**
     *
     * @param legs
     * The legs
     */
    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    /**
     *
     * @return
     * The overviewPolyline
     */
    public OverviewPolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    /**
     *
     * @param overviewPolyline
     * The overview_polyline
     */
    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }
}

