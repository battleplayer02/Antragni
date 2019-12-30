package com.example.antragni;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PurchaseTicket extends AppCompatActivity {
    ProgressDialog progressDialog;

    EditText firstname, lastname, email, contactnumber, section,
            alternatecontactnumber, semester, year, branch, college;
    Button add;
    String eventid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_ticket);
        firstname = (EditText) findViewById(R.id.firstName);
        lastname = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.email);
        contactnumber = (EditText) findViewById(R.id.contactno);
        alternatecontactnumber = (EditText) findViewById(R.id.alternatecontactnumber);
        semester = (EditText) findViewById(R.id.semester);
        year = (EditText) findViewById(R.id.year);
        branch = (EditText) findViewById(R.id.branch);
        college = (EditText) findViewById(R.id.college);
        section = (EditText) findViewById(R.id.section);

        progressDialog = new ProgressDialog(PurchaseTicket.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        add = (Button) findViewById(R.id.btsignup);

        eventid = getIntent().getStringExtra("iii");

        Toast.makeText(this, eventid, Toast.LENGTH_SHORT).show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                participate();
            }
        });
    }

    public void participate() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://3.20.14.234/antragini/participate.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            final JSONObject jsonObject = new JSONObject(response);
                            // Process your json here as required
                            if (jsonObject.getString("success").equals("1")) {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(PurchaseTicket.this)
                                        .setMessage("Registered Sucessfully")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create().show();
                            } else {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(PurchaseTicket.this)
                                        .setMessage("Error")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .create().show();
                            }
                        } catch (JSONException e) {
                            // Handle json exception as needed
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String json = null;
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                default:
                                    String value = null;
                                    try {
                                        // It is important to put UTF-8 to receive proper data else you will get byte[] parsing error.
                                        value = new String(response.data, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                    break;
                            }
                        }
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("firstname", firstname.getText().toString());
                params.put("lastname", lastname.getText().toString());
                params.put("email", email.getText().toString());
                params.put("contactnumber", contactnumber.getText().toString());
                params.put("alternatecontactnumber", email.getText().toString());
                params.put("sem", semester.getText().toString());
                params.put("year", year.getText().toString());
                params.put("section", section.getText().toString());
                params.put("branch", branch.getText().toString());
                params.put("college", college.getText().toString());
                params.put("eventid", eventid);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(PurchaseTicket.this);
        requestQueue.add(postRequest);
    }
}