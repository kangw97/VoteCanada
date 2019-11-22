package ca.bcit.votecanada;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;

/**
 * Party Activity to display the party information
 * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class PartyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        RecyclerView partyRecycler = findViewById(R.id.party_recycler);

        Party[] parties = Party.getParties();

        final String[] partyNames = new String[parties.length];
        int[] partyImages = new int[parties.length];
        for (int i=0; i<partyNames.length; i++) {
            partyNames[i] = parties[i].getPartyName();
            partyImages[i] = parties[i].getImageResourceID();
        }

        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(partyNames, partyImages);
        partyRecycler.setAdapter(adapter);
        // gridLayout
        GridLayoutManager lm = new GridLayoutManager(PartyActivity.this, 1);
        partyRecycler.setLayoutManager(lm);
        // click listener
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            public void onClick(String partyName) {
                Intent i = new Intent(PartyActivity.this, DetailActivity.class);
                i.putExtra("partyName", partyName);
                startActivity(i);
            }
        });
    }
}
