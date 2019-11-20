package ca.bcit.votecanada;



public class Party {
    private String partyName;
    private String partyLeader;
    private String[] ideology;
    private String seats;
    private String percentage;
    private int imageResourceID;



    private int leaderImgID;

    static String[] libIdeo = {"Liberalism", "Social liberalism"};

    static String[] conservIdeo = {"Canadian conservatism",
            "Economic liberalism",
            "Fiscal conservatism",
            "Canadian federalism"};
    static String[] blocIdeo = {"Quebec nationalism",
            "Quebec sovereigntism",
            "Left-wing nationalism",
            "Regionalism",
            "Republicanism",
            "Environmentalism",
            "Social democracy"};
    static String[] ndpIdeo = {"Social democracy",
            "Democratic socialism"};
    static String[] peopleIdeo = {"Canadian conservatism",
            "Canadian populism",
            "Classical liberalism",
            "Libertarianism",
            "Right-wing populism"};
    static String[] greenIdeo = {"Green politics"};



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
                    "9.5",
                    R.drawable.bloc,
                    R.drawable.yfb),
            new Party(
                    "New Democratic Party",
                    "Jagmeet Singh",
                    ndpIdeo,
                    "24",
                    "7.1",
                    R.drawable.ndp,
                    R.drawable.js),
            new Party(
                    "Green Party",
                    "Elizabeth May",
                    peopleIdeo,
                    "3",
                    "0.8",
                    R.drawable.green,
                    R.drawable.emg),
            new Party(
                    "People's Party",
                    "Maxime Bernier",
                    greenIdeo,
                    "1",
                    "0.3",
                    R.drawable.people,
                    R.drawable.mb)
    };




    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLeader() {
        return partyLeader;
    }

    public void setPartyLeader(String partyLeader) {
        this.partyLeader = partyLeader;
    }

    public int getImageResourceID() {
        return imageResourceID;
    }

    public void setImageResourceID(int imageResourceID) {
        this.imageResourceID = imageResourceID;
    }

    public static Party[] getParties() {
        return parties;
    }

    public String[] getIdeology() {
        return ideology;
    }

    public void setIdeology(String[] ideology) {
        this.ideology = ideology;
    }

    public int getLeaderImgID() {
        return leaderImgID;
    }

    public void setLeaderImgID(int leaderImgID) {
        this.leaderImgID = leaderImgID;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Party(String name, String leader, String[] ideology, String seats, String percentage, int imgId, int imgId2) {
        this.partyName = name;
        this.partyLeader = leader;
        this.ideology = ideology;
        this.seats = seats;
        this.percentage = percentage;
        this.imageResourceID = imgId;
        this.leaderImgID = imgId2;
    }


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
