package com.example.antragni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminInsideEvent extends AppCompatActivity {
    ArrayList<ShowMembers> blackboardArrayList = new ArrayList<>();
    JSONObject jsonObject;
    RecyclerView recyclerView,teams;
    ProgressDialog progressDialog;
    TextView ename, cname, cnumber, edate, eplace, ecategory, eamount, edesc;
    String eventid;
    Button button;
    private static final String URL_REGIST = "http://3.20.14.234/antragini/insideEvent.php";
    String cn;
    String amount;
    TextView nop, ammountcollected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inside_event);
        button = (Button) findViewById(R.id.ticket);

//        Toast.makeText(this, getIntent().getStringExtra("evid"), Toast.LENGTH_SHORT).show();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        ename = findViewById(R.id.eventName);
        cname = findViewById(R.id.eventOrganiserName);
        cnumber = findViewById(R.id.eventOrganiserNumber);
        eplace = findViewById(R.id.eventVenue);
        eamount = findViewById(R.id.eventAmount);
        edate = findViewById(R.id.eventDate);
        edesc = findViewById(R.id.eventDescription);
        ecategory = findViewById(R.id.eventCategory);
        nop = findViewById(R.id.nop);
        ammountcollected = findViewById(R.id.ammountcollected);

        recyclerView = findViewById(R.id.members);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        eventid = getIntent().getStringExtra("eventid");
        members();
    //creating adapter object and setting it to recyclerview
    MembersAdapter adapter = new MembersAdapter(AdminInsideEvent.this, blackboardArrayList);
                        recyclerView.setAdapter(adapter);

        Regist();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressDialog.create();
                    Intent intent = new Intent(AdminInsideEvent.this, PurchaseTicket.class);
                    intent.putExtra("iii",eventid);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void Regist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            System.out.println(response + "response");
                            String success = jsonObject.getString("success");


                            if (success.equals("1")) {
                                cn = jsonObject.getString("coordinator").toUpperCase();
                                ecategory.setText(jsonObject.getString("category"));
                                cname.setText("By: " + jsonObject.getString("coordinator").toUpperCase());
                                cnumber.setText("Contact Number: " + jsonObject.getString("coordinatorNumber"));
                                eamount.setText("Rs. " + jsonObject.getString("cost"));
                                amount = eamount.getText().toString();
                                edesc.setText(jsonObject.getString("description"));
                                edate.setText(jsonObject.getString("eventdate"));
                                ename.setText(jsonObject.getString("eventname").toUpperCase());
                                eplace.setText(jsonObject.getString("location"));

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminInsideEvent.this)
                                        .setMessage("No Event Found")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        });
                                builder.create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder = new AlertDialog.Builder(AdminInsideEvent.this)
                                    .setMessage("No Event Found")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                        }
                                    });
                            builder.create().show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("eventid", eventid);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void members()
    {    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://3.20.14.234/antragini/members.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //converting the string to json array object
                        System.out.println("response"+response);
                        JSONArray array=new JSONArray(response);
                        //traversing through all the object


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject word = array.getJSONObject(i);
                            ShowMembers showBlackboard=new ShowMembers();
                            showBlackboard.setEmail(word.optString("name"));
                            showBlackboard.setAttendence(word.optString("attendence"));
                            showBlackboard.setId(word.optString("id"));
                            blackboardArrayList.add(showBlackboard);
                        }
                        //creating adapter object and setting it to recyclerview
                        MembersAdapter adapter = new MembersAdapter(AdminInsideEvent.this, blackboardArrayList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
    {
        @Override
        protected Map<String,String> getParams() throws AuthFailureError
        {
            Map<String,String> params=new HashMap<>();
            params.put("id",eventid);
            return params;
        }
    };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void teams()
    {    StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://3.20.14.234/antragini/teams.php",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //converting the string to json array object
                        System.out.println("response"+response);
                        JSONArray array=new JSONArray(response);
                        //traversing through all the object


                        for (int i = 0; i < array.length(); i++) {
                            JSONObject word = array.getJSONObject(i);
                            ShowMembers showBlackboard=new ShowMembers();
                            showBlackboard.setEmail(word.optString("name"));
                            showBlackboard.setAttendence(word.optString("attendence"));
                            showBlackboard.setId(word.optString("id"));
                            blackboardArrayList.add(showBlackboard);
                        }
                        //creating adapter object and setting it to recyclerview
                        MembersAdapter adapter = new MembersAdapter(AdminInsideEvent.this, blackboardArrayList);
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            })
    {
        @Override
        protected Map<String,String> getParams() throws AuthFailureError
        {
            Map<String,String> params=new HashMap<>();
            params.put("id",eventid);
            return params;
        }
    };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}