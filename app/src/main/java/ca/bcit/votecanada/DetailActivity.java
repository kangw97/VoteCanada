package ca.bcit.votecanada;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * Party Detail activity, onclick party
 * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        displayPartyDetails();
    }

    /**
     * display the chosen party info
     */
    private void displayPartyDetails() {
        String partyName = (String) getIntent().getExtras().get("partyName");

        Party party = Party.getPartyByName(partyName);

        if (party != null) {
            TextView party_text = findViewById(R.id.party_text);
            party_text.setText(party.getPartyName());

            TextView party_leader = findViewById(R.id.party_leader);
            party_leader.setText(party.getPartyLeader());

            TextView party_seats = findViewById(R.id.party_seats);
            party_seats.setText(party.getSeats());

            TextView party_percentage = findViewById(R.id.party_percentage);
            party_percentage.setText(party.getPercentage());

            TextView party_ideology = findViewById(R.id. party_ideology);
            for (int i = 0; i < party.getIdeology().length; i++)
            party_ideology.setText(party_ideology.getText() + party.getIdeology()[i] + "\n");

            ImageView party_image = findViewById(R.id.party_image);
            party_image.setImageDrawable(ContextCompat.getDrawable(this, party.getLeaderImgID()));
            party_image.setContentDescription(party.getPartyName());
        }
    }
}