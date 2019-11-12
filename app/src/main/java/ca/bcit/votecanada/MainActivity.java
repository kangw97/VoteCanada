package ca.bcit.votecanada;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onRoutingClick(View V) {
        Intent intent = new Intent(this, RoutingActivity.class);
        startActivity(intent);
    }

    public void onPartyClick(View V) {
        Intent intent = new Intent(this, PartyActivity.class);
        startActivity(intent);
    }
}
