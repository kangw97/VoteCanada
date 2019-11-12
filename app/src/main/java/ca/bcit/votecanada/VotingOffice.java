package ca.bcit.votecanada;

/**
 *
 */
public class VotingOffice {

    private String officeName;
    private String address;
    private double distance;

    VotingOffice(String fn, String address, double dis) {
        this.officeName = fn;
        this.distance = dis;
        this.address = address;
    }
    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
