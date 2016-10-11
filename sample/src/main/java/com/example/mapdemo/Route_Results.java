package com.example.mapdemo;

import android.content.Context;
import
        android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.Route;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mapdemo.R.id.map;

public class Route_Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__results);

        Bundle b = this.getIntent().getExtras();

        if (b != null)
        {
            List<Route> route= ( List<Route> )b.getSerializable("routes_object");
            System.out.println("ssssssize of route is " + route.size());

            for (int i = 0; i < route.size(); i++) {

            // contact our own server to get better estimation
            final Context current = this;
            RequestQueue queue = Volley.newRequestQueue(current);
            String url = "http://52.45.41.223:8080/";

            //Request a string response from the provided URL.
            final Route currentRoute = route.get(i);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    // Display the first 500 characters of the response string
                    Toast.makeText(current, response ,Toast.LENGTH_SHORT).show();

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
                        System.out.println("lat" + i + ":" + l.latitude +
                                            " lng" + i  + ":" + l.longitude);
                        i++;
                    }
                    int j = 0;
                    for(int sec : currentRoute.getDurations())
                    {
                        params.put("dur" + j, sec + "");
                        System.out.println("dur" + j + ":" + sec);
                        j++;
                    }


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
        else
        {
            System.out.println("sssss bundle is null");

        }

    }
}
