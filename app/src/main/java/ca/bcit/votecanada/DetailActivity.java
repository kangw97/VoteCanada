package ca.bcit.votecanada;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        displayPartyDetails();
    }

    private void displayPartyDetails() {
        String partyName = (String) getIntent().getExtras().get("partyName");

        Party party = Party.getPartyByName(partyName);

        if (party != null) {
            TextView party_text = findViewById(R.id.party_text);
            party_text.setText(party.getPartyName());

            TextView party_leader = findViewById(R.id.party_leader);
            party_leader.setText(party.getPartyLeader());

            ImageView party_image = findViewById(R.id.party_image);
            party_image.setImageDrawable(ContextCompat.getDrawable(this, party.getImageResourceID()));
            party_image.setContentDescription(party.getPartyName());
        }
    }
}