package ca.bcit.votecanada;

import com.google.android.gms.maps.model.LatLng;

/**
 *  Voting Office class to hold Voting Office informations from Json
 *
 *  * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class VotingOffice {
    // office name
    private String officeName;
    // office address
    private String address;
    // office distance to the current location
    private double distance;

    private LatLng xyCoord;

    /**
     * Constructor for voting office
     * @param fn
     * @param address
     * @param dis
     * @param xy
     */
    VotingOffice(String fn, String address, double dis, LatLng xy) {
        this.officeName = fn;
        this.distance = dis;
        this.address = address;
        this.xyCoord = xy;
    }
    // get latlong
    public LatLng getXyCoord() {
        return xyCoord;
    }
    // set latlong
    public void setXyCoord(LatLng xyCoord) {
        this.xyCoord = xyCoord;
    }

    // get office name
    public String getOfficeName() {
        return officeName;
    }
    // set office name
    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }
    // get office distance
    public double getDistance() {
        return distance;
    }
    // set office name
    public void setDistance(double distance) {
        this.distance = distance;
    }
    // get office address
    public String getAddress() {
        return address;
    }
    // set office address
    public void setAddress(String address) {
        this.address = address;
    }
}
