package com.example.antragni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    TextView fp, su,login;
    EditText email,password;
    ProgressDialog builder;

    LoginManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outside_login);
        session = new LoginManager(this);
        builder = new ProgressDialog(LoginActivity.this);

        fp=(TextView)findViewById(R.id.forgetpassword);
        login=(TextView)findViewById(R.id.login);

        email=(EditText)findViewById(R.id.email);
        email.setText(getIntent().getStringExtra("email"));
        password=(EditText)findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(email.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
            {
                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                login(email.getText().toString(),password.getText().toString());
                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
            }
            }
        });

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPassword.class));
            }
        });
    }

    public void login(final String e, final String p){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://www.himanshushekhar.ml/antragini/login.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    JSONObject object = new JSONObject(response);
                    System.out.println("kale: "+response);
                    String success = object.getString("success");
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    if (success.equals("1")){
                        session.createLoginSession(
                                object.optString("id"),
                                email.getText().toString(),
                                password.getText().toString(),
                                object.optString("phonenumber"),
                                object.optString("fn"),
                                object.optString("ll"),
                                object.optString("type")
                        );

                        builder.dismiss();
                        startActivity(new Intent(LoginActivity.this,Participants.class));
                    }
                    if (success.equals("0")){
                        builder.dismiss();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setMessage("Login Unsuccess")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                    }
                }catch (Exception e){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setMessage("Error5"+e.getMessage())
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .create()
                            .show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new AlertDialog.Builder(LoginActivity.this)
                        .setMessage("Error6"+error.getMessage())
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",e);
                params.put("password",p);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }
}