package ca.bcit.votecanada;


public class Party {
    private String partyName;
    private String partyLeader;
    private int imageResourceID;


    public static final Party[] parties = {
            new Party("Liberal Party", "Justin Trudeau", R.drawable.liberal),
            new Party("Conservative Party", "Andrew Scheer", R.drawable.conservative),
            new Party("Bloc Québécois", "Yves-François Blanchet", R.drawable.bloc),
            new Party("New Democratic Party", "Jagmeet Singh", R.drawable.ndp),
            new Party("Green Party", "Elizabeth May", R.drawable.green),
            new Party("People's Party", "Maxime Bernier", R.drawable.people)
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


    public Party(String name, String leader, int imgId) {
        this.partyName = name;
        this.partyLeader = leader;
        this.imageResourceID = imgId;
    }

//    public static Party[] getPartyByPartyLeader(String leader) {
//        ArrayList<Party> partyList = new ArrayList<Party>();
//        for (int i = 0; i < parties.length; i++) {
//            Party p = parties[i];
//            if (leader.toLowerCase().trim().equals(p.getPartyLeader().toLowerCase())) {
//                partyList.add(p);
//            }
//        }
//        Party[] array = partyList.toArray(new Party[partyList.size()]);
//        return array;
//    }

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
