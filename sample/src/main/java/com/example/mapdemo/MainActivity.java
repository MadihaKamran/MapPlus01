package com.example.mapdemo;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.CheckBox;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements RoutingListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        AdapterView.OnItemSelectedListener, OnMapReadyCallback,
                ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener {
    protected GoogleMap map;
    protected LatLng start;
    protected LatLng end;
    @InjectView(R.id.start)
    AutoCompleteTextView starting;
    @InjectView(R.id.destination)
    AutoCompleteTextView destination;
    @InjectView(R.id.send)
    ImageView send;
    private String LOG_TAG = "MyActivity";
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutoCompleteAdapter mAdapter;
    private ProgressDialog progressDialog;
    private ArrayList<Polyline> polylines;
    private int[] colors = new int[]{R.color.primary_dark,R.color.primary,R.color.primary_light,R.color.accent,R.color.primary_dark_material_light};
    private CheckBox mTrafficCheckbox;
    private CheckBox mMyLocationCheckbox;
    private LocationListener net_listener;
    private LocationListener gps_listener;
    private LocationManager locationManager;
    private String res;
    private StringTokenizer token;


    private static final LatLngBounds BOUNDS_JAMAICA= new LatLngBounds(new LatLng(-57.965341647205726, 144.9987719580531),
            new LatLng(72.77492067739843, -9.998857788741589));

    /**
     * This activity loads a map and then displays the route and pushpins on it.
     */
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.inject(this);
    mTrafficCheckbox = (CheckBox) findViewById(R.id.traffic);
    mMyLocationCheckbox = (CheckBox) findViewById(R.id.my_location);
    getSupportActionBar().setDisplayShowHomeEnabled(true);


    polylines = new ArrayList<>();
    // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.
    mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(AppIndex.API).build();
    MapsInitializer.initialize(this);
    mGoogleApiClient.connect();

    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);

    if (mapFragment == null) {
        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
    }
    map = mapFragment.getMap();

    mAdapter = new PlaceAutoCompleteAdapter(this, android.R.layout.simple_list_item_1,
            mGoogleApiClient, BOUNDS_JAMAICA, null);

    net_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

            map.moveCamera(center);
            map.animateCamera(zoom);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    gps_listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

            map.moveCamera(center);
            map.animateCamera(zoom);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    map.setMyLocationEnabled(false);
    map.getUiSettings().setMyLocationButtonEnabled(false);
        /*
        * Updates the bounds being used by the auto complete adapter based on the position of the
        * map.
        * */
    map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
        @Override
        public void onCameraChange(CameraPosition position) {
            LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
            mAdapter.setBounds(bounds);
        }
    });


    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(43.7, -79.40));
    CameraUpdate zoom = CameraUpdateFactory.zoomTo(2);

    map.moveCamera(center);
    map.animateCamera(zoom);





        /*
        * Adds auto complete adapter to both auto complete
        * text views.
        * */
    starting.setAdapter(mAdapter);
    destination.setAdapter(mAdapter);


        /*
        * Sets the start and destination points based on the values selected
        * from the autocomplete text views.
        * */

    starting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        // Request did not complete successfully
                        Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                        places.release();
                        return;
                    }
                    // Get the Place object from the buffer.
                    final Place place = places.get(0);

                    start = place.getLatLng();
                }
            });

        }
    });
    destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceAutoCompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        // Request did not complete successfully
                        Log.e(LOG_TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                        places.release();
                        return;
                    }
                    // Get the Place object from the buffer.
                    final Place place = places.get(0);
                    end = place.getLatLng();
                }
            });

        }
    });

        /*
        These text watchers set the start and end points to null because once there's
        * a change after a value has been selected from the dropdown
        * then the value has to reselected from dropdown to get
        * the correct location.
        * */
    starting.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int startNum, int before, int count) {
            if (start != null) {
                start = null;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

    destination.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            if (end != null) {
                end = null;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

}

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * Called when the Traffic checkbox is clicked.
     */
    public void onTrafficToggled(View view) {
        updateTraffic();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map.setOnMyLocationButtonClickListener(this);
        enableMyLocation();
        updateTraffic();
    }


    /* Enables the My Location layer if the fine location permission has been granted.
    */
    private void enableMyLocation() {
       if (map != null) {
            // Access to the location has been granted to the app.
            map.setMyLocationEnabled(true);
        }
    }

    /**
     * Called when the MyLocation checkbox is clicked.
     */
    public void onMyLocationToggled(View view) {
        updateMyLocation();
    }

    private void updateMyLocation() {
        if (mMyLocationCheckbox.isChecked()) {
            map.setMyLocationEnabled(true);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 5000, 0, net_listener);


            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    3000, 0,gps_listener);
        } else {
            // Uncheck the box until the layer has been enabled and request missing permission.
            mMyLocationCheckbox.setChecked(false);
            locationManager.removeUpdates(gps_listener);
            locationManager.removeUpdates(net_listener);
        }

    }



    private void updateTraffic() {

        map.setTrafficEnabled(mTrafficCheckbox.isChecked());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @OnClick(R.id.send)
    public void sendRequest()
    {
        if(Util.Operations.isOnline(this))
        {
            route();
        }
        else
        {
            Toast.makeText(this,"No internet connectivity",Toast.LENGTH_SHORT).show();
        }
    }

    public void route(){
        if(start==null || end==null)
        {
            if(start==null)
            {
                if(starting.getText().length()>0)
                {
                    starting.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a starting point.",Toast.LENGTH_SHORT).show();
                }
            }
            if(end==null)
            {
                if(destination.getText().length()>0)
                {
                    destination.setError("Choose location from dropdown.");
                }
                else
                {
                    Toast.makeText(this,"Please choose a destination.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            progressDialog = ProgressDialog.show(this, "Please wait.",
                    "Fetching route information.", true);
            /*
            * Here contacts the local server to get the point we need to pass through
            */

            final Context current = this;

            final RoutingListener routingListener = this;
            ArrayList<LatLng> list = new ArrayList<LatLng>();
                            list.add(start);
                            // Start marker
                            map.addMarker(new MarkerOptions()
                                    .position(start)
                                    .title("starting point"));


                            map.addMarker(new MarkerOptions()
                                    .position(end)
                                    .title("destination"));
                            list.add(end);

                            Routing routing = new Routing.Builder()
                                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                                    .withListener(routingListener)
                                    .alternativeRoutes(true)
                                    .waypoints(list)
                                    .build();
                            routing.execute();

        }
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        progressDialog.dismiss();
        if(e != null) {
            Toast.makeText(this, "Error routing: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex)
    {
        progressDialog.dismiss();
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);

        map.moveCamera(center);


        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
//        for (int i = 0; i <route.size(); i++) {
        for (int i = 0; i < 1; i++) {
            //only display one route
            //In case of more than 5 alternative routes
            int colorIndex = i % colors.length;
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(colors[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = map.addPolyline(polyOptions);
            polylines.add(polyline);



            // contact our own server to get better estimation
            final Context current = this;
            RequestQueue queue = Volley.newRequestQueue(current);
            String url = "http://52.45.41.223:8080/";
//
            //Request a string response from the provided URL.
            final RoutingListener routingListener = this;
            final Route currentRoute = route.get(i);




            StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    // Display the first 500 characters of the response string
                    res = response;
                    Toast.makeText(current, response ,Toast.LENGTH_SHORT).show();
                    // Start marker
                    map.addMarker(new MarkerOptions()
                            .position(start)
                            .title("starting point"));

                    map.addMarker(new MarkerOptions()
                            .position(end)
                            .title("destination"));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(current,error.toString(),Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String,String> getParams(){
                    Map<String, String> params = new HashMap<String, String>();
                    int i = 0;
                    for(LatLng l: currentRoute.getPoints()){

                        params.put("lat" + i, l.latitude + "");
                        params.put("lng" + i, l.longitude + "");
//                       System.out.println("lat" + i + ":" + l.latitude
//                                            + " lng" + i  + ":" + l.longitude);
                        i++;
                    }
                    int j = 0;
                    for(int sec : currentRoute.getDurations())
                    {
                        params.put("dur" + j, sec + "");
                        j++;
                    }

                    System.out.println("sssssss" + i + " locations and "
                                        + j + " durations");
                    return params;
                }

            };
            queue.add(stringRequest);

            Toast.makeText(getApplicationContext(),"Route "+": distance - "
                           + route.get(i).getDistanceValue()
                           +": duration - "+ route.get(i).getDurationValue()
                           ,Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public void onRoutingCancelled() {
        Log.i(LOG_TAG, "Routing was cancelled.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.v(LOG_TAG,connectionResult.toString());
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(mGoogleApiClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mGoogleApiClient, getIndexApiAction());
        mGoogleApiClient.disconnect();
    }
}
