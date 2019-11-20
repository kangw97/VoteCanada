package ca.bcit.votecanada;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Google maps activity
 * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    // tage for current activity
    private static final String TAG = "RouteActivity";
    // Manifest.permission.ACCESS_FINE_LOCATION
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    // COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    // LOCATION_PERMISSION_REQUEST_CODE = 1234
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    // if current location enabled
    private Boolean mLocationPermissionsGranted = false;
    //// the map
    private GoogleMap mMap;
    // current location api
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // current location
    private Location currentLocation;
    // current lat and lng
    private LatLng currentLatLng;
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
    // markers for selected office
    Marker markers[];
    // the route
    private Polyline currentPolyline;

    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.i("test", "ready");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            for(int i = 0; i < addresses.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(longLat[i][1], longLat[i][0])).title(pollingName.get(i)));
            }
        }

    }

    // when the activity is loaded
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routing);
        Log.i("test", "create");
        // initiating textview holders
        officeName = findViewById(R.id.textViewOfficeName);
        officeAddress = findViewById(R.id.textViewAddresses);
        officeDistance = findViewById(R.id.textViewDistance);
        // initiating list view
        lvOffice = findViewById(R.id.lvOffices);
        officeList = new ArrayList<>();
        // getting extras
        Intent intent = getIntent();
        // getting addresses
        addresses = intent.getParcelableArrayListExtra("addresses");
        // getting polling names
        pollingName = intent.getParcelableArrayListExtra("pollingname");
        // getting geo coordinates
        longLat = (double[][]) intent.getSerializableExtra("geo");
        getLocationPermission();
    }
    // set markers on map
    private void generateMarks() {
        // list item listener after user choose a voting office
        lvOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                setMarks(currentLatLng, officeList.get(position).getXyCoord(), officeList.get(position).getOfficeName());
            }
        });
        // set markers on map
        for (int i = 0; i < addresses.size(); i++) {
            VotingOffice vo;
            // getting the logLat
            LatLng voLatlng = new LatLng(longLat[i][1], longLat[i][0]);
            // hardcoding distance for now
            if(i == 1) {
                vo = new VotingOffice(pollingName.get(i), "7550 RoseWood Street", CalculationByDistance(currentLatLng, voLatlng), voLatlng);
            } else if(i == 5) {
                vo = new VotingOffice("Normana Rest Home", addresses.get(i), CalculationByDistance(currentLatLng, voLatlng), voLatlng);
            } else if(i == 9) {
                vo = new VotingOffice("Parkcrest Elementary", addresses.get(i), CalculationByDistance(currentLatLng, voLatlng), voLatlng);
            } else{
                vo = new VotingOffice(pollingName.get(i), addresses.get(i), CalculationByDistance(currentLatLng, voLatlng), voLatlng);
            }
            officeList.add(vo);
        }
        // sort the list by distance
        Collections.sort(officeList, new Comparator<VotingOffice>() {
            @Override
            public int compare(VotingOffice c1, VotingOffice c2) {
                return Double.compare(c1.getDistance(), c2.getDistance());
            }
        });
        // constructing VotingOffice adapter
        VotingOfficeAdapter adapter = new VotingOfficeAdapter(RouteActivity.this, officeList);
        lvOffice.setAdapter(adapter);
    }
    // calculate the distance between each voting office to the current location
    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        float results[] = new float[1000];
        Location.distanceBetween(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude, results);
        return Double.parseDouble(new DecimalFormat("0.#").format(results[0] / 1000));
    }
    // show the current location and the selected office markers
    private void setMarks(LatLng start, LatLng end, String officeName) {
        markers = new Marker[2];
        // call showRouting()
        showRouting(new MarkerOptions().position(start), new MarkerOptions().position(end));
        // add marker on map
        markers[0]= mMap.addMarker(new MarkerOptions().position(start).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        markers[1] = mMap.addMarker(new MarkerOptions().position(end).title(officeName));
        markers[1].showInfoWindow();
        // auto move the camera to cover two locations
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }
    // get the Device current location
    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            currentLocation = (Location) task.getResult();
                            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(currentLatLng, 13);
                            generateMarks();
                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RouteActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
        }
    }
    // move the map
    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
    // initiating the map
    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(RouteActivity.this);
    }
    // get the location permission request
    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    // request device current location access
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
    // formatting the url to call routing api
    private String getUrl(LatLng origin, LatLng dest, String travelMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + travelMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }
    // show routing after user selection
    private void showRouting(MarkerOptions start, MarkerOptions end) {
        new FetchURL(RouteActivity.this).execute(getUrl(start.getPosition(), end.getPosition(), "driving"), "driving");
    }
    // add routing on map
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }
}
