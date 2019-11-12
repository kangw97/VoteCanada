package ca.bcit.votecanada;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class RoutingActivity extends FragmentActivity implements OnMapReadyCallback {
    // the map
    private GoogleMap mMap;
    // default zoom level
    // ArrayList to store address
    ArrayList<? extends String> addresses;
    // ArrayList to store polling names
    ArrayList<? extends String> pollingName;
    // 2D ArrayList to store longitude and latitude
    double[][] longLat;
    // officename text
    private TextView officeName;
    // officeaddress text
    private TextView officeAddress;
    // officedistance text
    private TextView officeDistance;
    // listview of offices
    ListView lvOffice;
    // list of voting offices
    List<VotingOffice> officeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(RoutingActivity.this);

        // initiating textview holders
        officeName = findViewById(R.id.textViewOfficeName);
        officeAddress = findViewById(R.id.textViewAddresses);
        officeDistance = findViewById(R.id.textViewDistance);
        // initiating list view
        lvOffice = findViewById(R.id.lvOffices);
        officeList = new ArrayList<VotingOffice>();
        // getting extras
        Intent intent = getIntent();
        // getting addresses
        addresses = intent.getParcelableArrayListExtra("addresses");
        // getting polling names
        pollingName = intent.getParcelableArrayListExtra("pollingname");
        // getting geo coordinates
        longLat = (double[][]) intent.getSerializableExtra("geo");
    }
    // loading data into list view
    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < addresses.size(); i++) {
            VotingOffice vo;
            // hardcoding distance for now
            if(i == 1) {
                vo = new VotingOffice(pollingName.get(i), "7550 RoseWood Street", 10.3);
            } else if(i == 5) {
                vo = new VotingOffice("Normana Rest Home", addresses.get(i), 10.3);
            } else if(i == 9) {
                vo = new VotingOffice("Parkcrest Elementary", addresses.get(i), 10.3);
            } else{
                vo = new VotingOffice(pollingName.get(i), addresses.get(i), 10.3);
            }
            officeList.add(vo);
        }
        VotingOfficeAdapter adapter = new VotingOfficeAdapter(RoutingActivity.this, officeList);
        lvOffice.setAdapter(adapter);
        Log.i("test", Integer.toString(officeList.size()));
    }

    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // initiate map
        mMap = googleMap;
        // sizeable
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // current location
        LatLng bcit = new LatLng(49.25, -123);
        // marker for current location
        mMap.addMarker(new MarkerOptions().position(bcit).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bcit,13));
        // markers for every voting office
        for(int i = 0; i < addresses.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(longLat[i][1], longLat[i][0])).title(pollingName.get(i)));
        }
    }
}
