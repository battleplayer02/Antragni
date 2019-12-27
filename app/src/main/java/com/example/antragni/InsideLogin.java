package com.example.antragni;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class InsideLogin extends AppCompatActivity {

    AlertDialog dialog;
    ProgressDialog progressDialog;
    Button login;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inside_login);

        login = (Button)findViewById(R.id.next);
        email = (EditText)findViewById(R.id.email);

        progressDialog = new ProgressDialog(InsideLogin.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                getMail(email.getText().toString());
            }
        });

        dialog = new AlertDialog.Builder(this)
                .setMessage("Error")
                .setCancelable(false)
                .create();
    }

    private boolean getMail(final String email) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://www.himanshushekhar.ml/antragini/login_email.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonObject = new JSONObject(response);
                            // Process your json here as required
                            if (jsonObject.getString("success").equals("1"))
                            {
                                Intent intnt = new Intent(InsideLogin.this,LoginActivity.class);
                                intnt.putExtra("email",email);
                                progressDialog.dismiss();
                                startActivity(intnt);
                            }
                            else {
                                Intent intnt = new Intent(InsideLogin.this,NewAccount.class);
                                intnt.putExtra("email",email);
                                progressDialog.dismiss();
                                startActivity(intnt);
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
                        if(response != null && response.data != null){
                            switch(response.statusCode) {
                                default:
                                    String value = null;
                                    try {
                                        // It is important to put UTF-8 to receive proper data else you will get byte[] parsing error.
                                        value = new String(response.data, "UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                        progressDialog.dismiss();
                                        dialog.show();
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
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(InsideLogin.this);
        requestQueue.add(postRequest);
        return false;
    }
}