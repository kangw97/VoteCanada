package ca.bcit.votecanada;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * MainActivity that reads JSON objs from Burnaby City API and send all data to Routing Activity
 * * @author Jovan Sekhon, Kang Wang, Lawrence Zheng, 2019-11-20
 */
public class MainActivity extends AppCompatActivity {

    // URL to get contacts JSON
    private static String SERVICE_URL = "https://gis.burnaby.ca/arcgis/rest/services/OpenData/OpenData1/MapServer/4/query?where=1%3D1&outFields=*&outSR=4326&f=json";
    // loading dialog
    private ProgressDialog pDialog;
    // Log Tag
    private String TAG = MainActivity.class.getSimpleName();
    // ArrayList to store address
    ArrayList<String> addresses;
    // ArrayList to store polling name
    ArrayList<String> pollName;
    // 2D ArrayList to store longitude and latitude
    double[][] longLat;

    /**
     * on create method, initiating all variables
      */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // call super class
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initialize address arraylist
        addresses = new ArrayList<String>();
        // initialize pollingname arraylist
        pollName = new ArrayList<String>();
        // reading json objects
        new GetContacts().execute();

    }
    // starting routing activity after click VIEW MAP button
    public void onRoutingClick(View V) {
        // intent for RoutingActivity
        Intent intentRouting = new Intent(this, RouteActivity.class);
        // passing address to google map activity
        intentRouting.putExtra("addresses", addresses);
        // passing polling name to google map activity
        intentRouting.putExtra("pollingname", pollName);
        // passing geo coordinates to google map activity
        intentRouting.putExtra("geo", longLat);
        startActivity(intentRouting);
    }
    // starting party activity after click PARTIES button
    public void onPartyClick(View V) {
        Intent intent = new Intent(this, PartyActivity.class);
        startActivity(intent);
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        // setting up dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        // read from json url and store addresses and names
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = null;

            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(SERVICE_URL);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    // read the json url
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray features = jsonObj.getJSONArray("features");
                    // initialize 2d longlat array
                    longLat = new double[features.length()][2];
                    // looping through All Contacts
                    for (int i = 0; i < features.length(); i++) {
                        // feature object
                        JSONObject feature = features.getJSONObject(i);
                        // attribute object
                        JSONObject attribute = feature.getJSONObject("attributes");
                        // getting address
                        String address = attribute.getString("ADDRESS");
                        // getting polling name
                        String name = attribute.getString("POLLING_DIV_NAME");
                        // Geo coordinates x y
                        JSONObject geo = feature.getJSONObject("geometry");
                        // get x coord
                        Double x = geo.getDouble("x");
                        // get y coord
                        Double y = geo.getDouble("y");
                        // store address into addresses
                        addresses.add(address);
                        // store polling nameinto addresses
                        pollName.add(name);
                        // store into coordinates
                        longLat[i] = new double[]{x, y};
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }
        // after execute
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}
