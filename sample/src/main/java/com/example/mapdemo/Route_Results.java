package com.example.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import
        android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.card.action.WelcomeButtonAction;
import com.dexafree.materialList.view.MaterialListView;
import com.directions.route.Route;
import com.directions.route.RoutingListener;
import com.directions.route.Segment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.RequestCreator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mapdemo.R.id.map;

public class Route_Results extends AppCompatActivity {

    private String secondsToString(double pTime) {
        final int hour =(int) pTime/3600;
        final int min = (int)(pTime%3600)/60;
        String strMin = (min > 1) ? new String(min + " mins") : new String(min + " min");
        String strHour;
        if(hour == 0)
        {
            strHour = "";
        }
        else
        {
            strHour = (hour > 1) ? new String(hour + " hours ") : new String(hour + " hour ");
        }

        return String.format("%s%s",strMin,strHour);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route__results);

        Bundle b = this.getIntent().getExtras();

        if (b != null)
        {
            final List<Route> route= ( List<Route> )b.getSerializable("routes_object");
            final MaterialListView mListView = (MaterialListView) findViewById(R.id.routes_result_material);
            final Context current = this;
            String route_name = route.get(0).getName();
            final String route_start = route_name.substring(0,route_name.indexOf("Canada"));
            String dest = route_name.substring(route_name.indexOf(" to ") + 4);
            final String route_end = dest.substring(0, dest.indexOf("Canada"));

            Card card = new Card.Builder(this)
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_welcome_card_layout)
                    .setTitle("Recommended routes")
                    .setTitleColor(Color.WHITE)
                    .setDescription(route_start + "\nTo\n"+route_end)
                    .setDescriptionColor(Color.WHITE)
                    .setSubtitle(route.size() + " possible routes")
                    .setSubtitleColor(Color.WHITE)
                    .setBackgroundColor(Color.BLUE)
                    .endConfig()
                    .build();

            mListView.getAdapter().addAtStart(card);

            for (int i = 0; i < route.size(); i++) {
                final int index =i + 1 ;
                final String distance = route.get(i).getDistanceText();
                final String duration = route.get(i).getDurationText();
                final String title = "Data from Google distance: " + distance + " duration: " + duration;
                // contact our own server to get better estimation

                RequestQueue queue = Volley.newRequestQueue(current);
                String url = "http://52.45.41.223:8080/";

                //Request a string response from the provided URL.
                final Route currentRoute = route.get(i);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        // Display the first 500 characters of the response string

                        double sec = 0;
                        JSONObject  json = null;
                        JSONArray duration = null;
                        JSONArray incidents = null;
                        try
                        {
                            json = new JSONObject(response);
                            duration =  json.getJSONArray("durations");
                            incidents =  json.getJSONArray("incidents");
                            for(int i=0; i < duration.length(); i++){
                                JSONObject tmp = duration.getJSONObject(i);
                                if(tmp.has("weightedAvg"))
                                {
                                    sec = tmp.getDouble("weightedAvg");
                                }
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        final String incidents_pass = (incidents==null)?null:incidents.toString();
                        System.out.println("response: " + response);
                        List<Segment> segs = route.get(index-1).getSegments();
                        Card updated =
                                new Card.Builder(current)
                                        .withProvider(new CardProvider())
                                        .setLayout(R.layout.material_basic_buttons_card)
                                        .setTitle("Route  Recommend")
                                        .setDescription(segs.get(0).getInstruction()
                                                        + "\n ... \n"
                                                        + segs.get(segs.size()-1).getInstruction()
                                                        + "\n duration: " + secondsToString(sec) + "\n")
                                        .addAction(R.id.left_text_button, new TextViewAction(current)
                                                .setText("Go")
                                                .setTextResourceColor(R.color.black_button)
                                                .setListener(new OnActionClickListener() {
                                                    @Override
                                                    public void onActionClicked(View view, Card card) {

                                                        Intent intent = new Intent(current,MainActivity.class);
                                                        Bundle b = new Bundle();
                                                        b.putParcelable("route_selected", currentRoute);
                                                        b.putString("incidents",incidents_pass);
                                                        intent.putExtras(b);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                        startActivity(intent);

                                                    }
                                                }))
                                        .endConfig()
                                        .build();
                        mListView.getAdapter().add(updated);
                        //Toast.makeText(current, response ,Toast.LENGTH_LONG).show();

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
                        i++;
                    }
                    int j = 0;
                    for(int sec : currentRoute.getDurations())
                    {
                        params.put("dur" + j, sec + "");
                        j++;
                    }

                    return params;
                }

            };
            queue.add(stringRequest);
        }

        }
        else
        {
            System.out.println("sssss bundle is null");

        }

    }
}
