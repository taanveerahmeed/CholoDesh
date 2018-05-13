/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by rhythmshahriar on 7/18/17.
 */

public class TransportName {

    @SerializedName("transport_info_id")
    @Expose
    private Integer transportInfoId;

    @SerializedName("transport_info_route_id")
    @Expose
    private Integer routeId;

    @SerializedName("transport_info_transport_type_id")
    @Expose
    private Integer transportTypeId;

    @SerializedName("transport_info_operator_name")
    @Expose
    private String transportOperatorName;

    public Integer getTransportInfoId() {
        return transportInfoId;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public Integer getTransportTypeId() {
        return transportTypeId;
    }

    public String getTransportOperatorName() {
        return transportOperatorName;
    }

    @Override
    public String toString() {
        return transportOperatorName;

    }

    public TransportName ()
    {
        transportOperatorName = "N/A";
        transportTypeId = 0;
        transportInfoId = 0;

    }
}
