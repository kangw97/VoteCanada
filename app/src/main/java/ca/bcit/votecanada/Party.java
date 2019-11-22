package ca.bcit.votecanada;


/**
 * Party Class to hold the information of each pary
 * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class Party {
    // party name
    private String partyName;
    // party leader
    private String partyLeader;
    // party ideologies
    private String[] ideology;
    // party seats
    private String seats;
    // party chosen percentage
    private String percentage;
    // party logo
    private int imageResourceID;
    // party leader photo
    private int leaderImgID;
    // lib ideo
    static String[] libIdeo = {"Liberalism", "Social liberalism"};
    // cons ideo
    static String[] conservIdeo = {"Canadian conservatism",
            "Economic liberalism",
            "Fiscal conservatism",
            "Canadian federalism"};
    // block ideo
    static String[] blocIdeo = {"Quebec nationalism",
            "Quebec sovereigntism",
            "Left-wing nationalism",
            "Regionalism",
            "Republicanism",
            "Environmentalism",
            "Social democracy"};
    // ndp ideo
    static String[] ndpIdeo = {"Social democracy",
            "Democratic socialism"};
    // people ideo
    static String[] peopleIdeo = {"Canadian conservatism",
            "Canadian populism",
            "Classical liberalism",
            "Libertarianism",
            "Right-wing populism"};
    // green ideo
    static String[] greenIdeo = {"Green politics"};

    /**
     * party constructor
     * @param name
     * @param leader
     * @param ideology
     * @param seats
     * @param percentage
     * @param imgId
     * @param imgId2
     */
    public Party(String name, String leader, String[] ideology, String seats, String percentage, int imgId, int imgId2) {
        this.partyName = name;
        this.partyLeader = leader;
        this.ideology = ideology;
        this.seats = seats;
        this.percentage = percentage;
        this.imageResourceID = imgId;
        this.leaderImgID = imgId2;
    }

    // party info
    public static final Party[] parties = {
            new Party(
                    "Liberal Party",
                    "Justin Trudeau",
                    libIdeo,
                    "157",
                    "46.4%",
                    R.drawable.liberal,
                    R.drawable.jt),
            new Party(
                    "Conservative Party",
                    "Andrew Scheer",
                    conservIdeo,
                     "121",
                     "35.8%",
                     R.drawable.conservative,
                     R.drawable.as),
            new Party(
                    "Bloc Québécois",
                    "Yves-François Blanchet",
                    blocIdeo,
                    "32",
                    "9.5%",
                    R.drawable.bloc,
                    R.drawable.yfb),
            new Party(
                    "New Democratic Party",
                    "Jagmeet Singh",
                    ndpIdeo,
                    "24",
                    "7.1%",
                    R.drawable.ndp,
                    R.drawable.js),
            new Party(
                    "Green Party",
                    "Elizabeth May",
                    greenIdeo,
                    "3",
                    "0.8%",
                    R.drawable.green,
                    R.drawable.emg),
            new Party(
                    "People's Party",
                    "Maxime Bernier",
                    peopleIdeo,
                    "1",
                    "0.3%",
                    R.drawable.people,
                    R.drawable.mb)
    };

    /**
     * party name getter
     * @return party name
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * party name setter
     * @param partyName
     */
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * party leader name getter
     * @return party leader name
     */
    public String getPartyLeader() {
        return partyLeader;
    }

    /**
     * party leader name setter
     * @param partyLeader
     */
    public void setPartyLeader(String partyLeader) {
        this.partyLeader = partyLeader;
    }

    /**
     * party img getter
     * @return
     */
    public int getImageResourceID() {
        return imageResourceID;
    }

    /**
     * party img setter
     * @param imageResourceID
     */
    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    /**
     * getter for party info
     * @return
     */
    public static Party[] getParties() {
        return parties;
    }

    /**
     * getter for party ideo
     * @return
     */
    public String[] getIdeology() {
        return ideology;
    }

    /**
     * party ideo setter
     * @param ideology
     */
    public void setIdeology(String[] ideology) {
        this.ideology = ideology;
    }

    /**
     * party leader img getter
     * @return
     */
    public int getLeaderImgID() {
        return leaderImgID;
    }

    /**
     * party leader img setter
     * @param leaderImgID
     */
    public void setLeaderImgID(int leaderImgID) {
        this.leaderImgID = leaderImgID;
    }

    /**
     * seats getter
     * @return
     */
    public String getSeats() {
        return seats;
    }

    /**
     * seat setter
     * @param seats
     */
    public void setSeats(String seats) {
        this.seats = seats;
    }

    /**
     * percentage getter
     * @return
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * percentage setter
     * @param percentage
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * get party name bu chosen
     * @param name
     * @return
     */
    public static Party getPartyByName(String name) {

        Party p = null;
        for (int i = 0; i < parties.length; i++) {
            Party q = parties[i];
            if (name.toLowerCase().trim().equals(q.getPartyName().toLowerCase())) {
                p = q;
                break;
            }
        }
        return p;
    }
}
