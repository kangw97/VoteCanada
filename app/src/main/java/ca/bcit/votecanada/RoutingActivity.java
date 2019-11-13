package ca.bcit.votecanada;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.usb.UsbAccessory;
import android.location.Location;
import android.nfc.cardemulation.OffHostApduService;
import android.os.AsyncTask;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    // current location
    LatLng bcit = new LatLng(49.25, -123);
    // markers for selected office
    Marker markers[];
    // the route
    private Polyline currentPolyline;
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
        // list item listener after user choose a voting office
        lvOffice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                setMarks(bcit, officeList.get(position).getXyCoord(), officeList.get(position).getOfficeName());
            }
        });
    }
    // loading data into list view
    @Override
    protected void onStart() {
        super.onStart();
        for (int i = 0; i < addresses.size(); i++) {
            VotingOffice vo;
            // getting the logLat
            LatLng voLatlng = new LatLng(longLat[i][1], longLat[i][0]);
            // hardcoding distance for now
            if(i == 1) {
                vo = new VotingOffice(pollingName.get(i), "7550 RoseWood Street", CalculationByDistance(bcit, voLatlng), voLatlng);
            } else if(i == 5) {
                vo = new VotingOffice("Normana Rest Home", addresses.get(i), CalculationByDistance(bcit, voLatlng), voLatlng);
            } else if(i == 9) {
                vo = new VotingOffice("Parkcrest Elementary", addresses.get(i), CalculationByDistance(bcit, voLatlng), voLatlng);
            } else{
                vo = new VotingOffice(pollingName.get(i), addresses.get(i), CalculationByDistance(bcit, voLatlng), voLatlng);
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
        VotingOfficeAdapter adapter = new VotingOfficeAdapter(RoutingActivity.this, officeList);
        lvOffice.setAdapter(adapter);
    }

    // when map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // initiate map
        mMap = googleMap;
        // sizeable
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // marker for current location
        mMap.addMarker(new MarkerOptions().position(bcit).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(bcit,13));
        // markers for every voting office
        for(int i = 0; i < addresses.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(longLat[i][1], longLat[i][0])).title(pollingName.get(i)));
        }
    }
    // calculate the distance between each voitingoffice to the current location
    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        float results[] = new float[10];
        Location.distanceBetween(StartP.latitude, StartP.longitude, EndP.latitude, EndP.longitude, results);
        return Double.parseDouble(new DecimalFormat("0.#").format(results[0] / 1000));
    }
    // show routing after user selection
    private void showRouting(MarkerOptions start, MarkerOptions end) {
        String url = getUrl(start.getPosition(), end.getPosition(), "driving");
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
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

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            // Get the resonse result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }
    // show the current location and the selected office markers
    private void setMarks(LatLng start, LatLng end, String officeName) {
        markers = new Marker[2];
        markers[0]= mMap.addMarker(new MarkerOptions().position(start).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        markers[1] = mMap.addMarker(new MarkerOptions().position(end).title(officeName));
        markers[1].showInfoWindow();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
        int padding = 200; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
        showRouting(new MarkerOptions().position(start), new MarkerOptions().position(end));
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString ="";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }
    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> >{

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionParser directionParser = new DirectionParser();
                routes = directionParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            // Get list route and display it into the map
            ArrayList points;
            PolylineOptions polylineOptions = null;
            for(List<HashMap<String, String>> path: lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for(HashMap<String, String> point: path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));
                    points.add(new LatLng(lat, lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if(polylineOptions != null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
