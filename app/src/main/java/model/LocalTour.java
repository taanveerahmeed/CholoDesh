package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocalTour {

    @SerializedName("destination_nearby_place_id")
    @Expose
    private Integer destinationNearbyPlaceId;
    @SerializedName("destination_nearby_place_name")
    @Expose
    private String destinationNearbyPlaceName;
    @SerializedName("reservation_type_name")
    @Expose
    private String reservationTypeName;
    @SerializedName("reservation_type_id")
    @Expose
    private Integer reservationTypeId;
    @SerializedName("local_tour_id")
    @Expose
    private Integer localTourId;
    @SerializedName("local_tour_destination_id")
    @Expose
    private String localTourDestinationId;
    @SerializedName("local_tour_near_by_place_id")
    @Expose
    private String localTourNearByPlaceId;
    @SerializedName("local_tour_duration")
    @Expose
    private String localTourDuration;
    @SerializedName("local_tour_time")
    @Expose
    private String localTourTime;
    @SerializedName("local_tour_transport_type")
    @Expose
    private String localTourTransportType;
    @SerializedName("local_tour_reservation_type")
    @Expose
    private String localTourReservationType;
    @SerializedName("local_tour_max_size")
    @Expose
    private String localTourMaxSize;
    @SerializedName("local_tour_per_person_cost")
    @Expose
    private String localTourPerPersonCost;
    @SerializedName("local_tour_group_1_cost")
    @Expose
    private String localTourGroup1Cost;
    @SerializedName("local_tour_group_2_cost")
    @Expose
    private String localTourGroup2Cost;
    @SerializedName("local_tour_group_3_cost")
    @Expose
    private String localTourGroup3Cost;
    @SerializedName("local_tour_group_4_cost")
    @Expose
    private String localTourGroup4Cost;
    @SerializedName("local_tour_group_5_cost")
    @Expose
    private String localTourGroup5Cost;
    @SerializedName("local_tour_group_6_cost")
    @Expose
    private String localTourGroup6Cost;
    @SerializedName("local_tour_group_7_cost")
    @Expose
    private String localTourGroup7Cost;
    @SerializedName("local_tour_group_8_cost")
    @Expose
    private String localTourGroup8Cost;
    @SerializedName("local_tour_group_9_cost")
    @Expose
    private String localTourGroup9Cost;
    @SerializedName("local_tour_group_10_cost")
    @Expose
    private String localTourGroup10Cost;
    @SerializedName("local_tour_details")
    @Expose
    private String localTourDetails;
    @SerializedName("local_tour_status")
    @Expose
    private String localTourStatus;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getDestinationNearbyPlaceId() {
        return destinationNearbyPlaceId;
    }

    public void setDestinationNearbyPlaceId(Integer destinationNearbyPlaceId) {
        this.destinationNearbyPlaceId = destinationNearbyPlaceId;
    }

    public String getDestinationNearbyPlaceName() {
        return destinationNearbyPlaceName;
    }

    public void setDestinationNearbyPlaceName(String destinationNearbyPlaceName) {
        this.destinationNearbyPlaceName = destinationNearbyPlaceName;
    }

    public String getReservationTypeName() {
        return reservationTypeName;
    }

    public void setReservationTypeName(String reservationTypeName) {
        this.reservationTypeName = reservationTypeName;
    }

    public Integer getReservationTypeId() {
        return reservationTypeId;
    }

    public void setReservationTypeId(Integer reservationTypeId) {
        this.reservationTypeId = reservationTypeId;
    }

    public Integer getLocalTourId() {
        return localTourId;
    }

    public void setLocalTourId(Integer localTourId) {
        this.localTourId = localTourId;
    }

    public String getLocalTourDestinationId() {
        return localTourDestinationId;
    }

    public void setLocalTourDestinationId(String localTourDestinationId) {
        this.localTourDestinationId = localTourDestinationId;
    }

    public String getLocalTourNearByPlaceId() {
        return localTourNearByPlaceId;
    }

    public void setLocalTourNearByPlaceId(String localTourNearByPlaceId) {
        this.localTourNearByPlaceId = localTourNearByPlaceId;
    }

    public String getLocalTourDuration() {
        return localTourDuration;
    }

    public void setLocalTourDuration(String localTourDuration) {
        this.localTourDuration = localTourDuration;
    }

    public String getLocalTourTime() {
        return localTourTime;
    }

    public void setLocalTourTime(String localTourTime) {
        this.localTourTime = localTourTime;
    }

    public String getLocalTourTransportType() {
        return localTourTransportType;
    }

    public void setLocalTourTransportType(String localTourTransportType) {
        this.localTourTransportType = localTourTransportType;
    }

    public String getLocalTourReservationType() {
        return localTourReservationType;
    }

    public void setLocalTourReservationType(String localTourReservationType) {
        this.localTourReservationType = localTourReservationType;
    }

    public String getLocalTourMaxSize() {
        return localTourMaxSize;
    }

    public void setLocalTourMaxSize(String localTourMaxSize) {
        this.localTourMaxSize = localTourMaxSize;
    }

    public String getLocalTourPerPersonCost() {
        return localTourPerPersonCost;
    }

    public void setLocalTourPerPersonCost(String localTourPerPersonCost) {
        this.localTourPerPersonCost = localTourPerPersonCost;
    }

    public String getLocalTourGroup1Cost() {
        return localTourGroup1Cost;
    }

    public void setLocalTourGroup1Cost(String localTourGroup1Cost) {
        this.localTourGroup1Cost = localTourGroup1Cost;
    }

    public String getLocalTourGroup2Cost() {
        return localTourGroup2Cost;
    }

    public void setLocalTourGroup2Cost(String localTourGroup2Cost) {
        this.localTourGroup2Cost = localTourGroup2Cost;
    }

    public String getLocalTourGroup3Cost() {
        return localTourGroup3Cost;
    }

    public void setLocalTourGroup3Cost(String localTourGroup3Cost) {
        this.localTourGroup3Cost = localTourGroup3Cost;
    }

    public String getLocalTourGroup4Cost() {
        return localTourGroup4Cost;
    }

    public void setLocalTourGroup4Cost(String localTourGroup4Cost) {
        this.localTourGroup4Cost = localTourGroup4Cost;
    }

    public String getLocalTourGroup5Cost() {
        return localTourGroup5Cost;
    }

    public void setLocalTourGroup5Cost(String localTourGroup5Cost) {
        this.localTourGroup5Cost = localTourGroup5Cost;
    }

    public String getLocalTourGroup6Cost() {
        return localTourGroup6Cost;
    }

    public void setLocalTourGroup6Cost(String localTourGroup6Cost) {
        this.localTourGroup6Cost = localTourGroup6Cost;
    }

    public String getLocalTourGroup7Cost() {
        return localTourGroup7Cost;
    }

    public void setLocalTourGroup7Cost(String localTourGroup7Cost) {
        this.localTourGroup7Cost = localTourGroup7Cost;
    }

    public String getLocalTourGroup8Cost() {
        return localTourGroup8Cost;
    }

    public void setLocalTourGroup8Cost(String localTourGroup8Cost) {
        this.localTourGroup8Cost = localTourGroup8Cost;
    }

    public String getLocalTourGroup9Cost() {
        return localTourGroup9Cost;
    }

    public void setLocalTourGroup9Cost(String localTourGroup9Cost) {
        this.localTourGroup9Cost = localTourGroup9Cost;
    }

    public String getLocalTourGroup10Cost() {
        return localTourGroup10Cost;
    }

    public void setLocalTourGroup10Cost(String localTourGroup10Cost) {
        this.localTourGroup10Cost = localTourGroup10Cost;
    }

    public String getLocalTourDetails() {
        return localTourDetails;
    }

    public void setLocalTourDetails(String localTourDetails) {
        this.localTourDetails = localTourDetails;
    }

    public String getLocalTourStatus() {
        return localTourStatus;
    }

    public void setLocalTourStatus(String localTourStatus) {
        this.localTourStatus = localTourStatus;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}