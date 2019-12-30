package com.example.antragni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class NewAccount extends AppCompatActivity {
    LoginManager session;
    ProgressDialog builder;

    EditText email,password,firstname,lastname,phonenumber;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        session = new LoginManager(this);
        builder = new ProgressDialog(NewAccount.this);
        builder.setMessage("Please Wait...");

        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        firstname = (EditText)findViewById(R.id.firstName);
        lastname = (EditText)findViewById(R.id.lastName);
        phonenumber = (EditText)findViewById(R.id.contactno);

        final String cp = ((EditText)findViewById(R.id.cnfpassword)).getText().toString();
        button =(Button)findViewById(R.id.btsignup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cp.equals(password.getText().toString())){
                    Toast.makeText(NewAccount.this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(email.getText().toString(),
                            password.getText().toString(),
                            firstname.getText().toString(),
                            lastname.getText().toString(),
                            phonenumber.getText().toString(),
                            "participant");
                }
            }
        });

        email.setText(getIntent().getStringExtra("email"));

    }

    /* *
     * $email=$_POST['emailh'];
     * $password=$_POST['passwordh'];
     * $firstname=$_POST['firstnameh'];
     * $lastname=$_POST['lastnameh'];
     * $phonenumber=$_POST['phonenumberh'];
     * $usertype=$_POST['usertypeh'];
     * */

    public void register(final String email, final String password, final String firstname, final String lastname,
                         final String phonenumber, final String usertype)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://3.20.14.234/antragini/signup.php"
                ,new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                try {
                    Log.e("ss", "onResponse: " + response);
                    JSONObject object = new JSONObject(response);
                    String success = object.getString("success");
                    if (success.equals("1")){
//                        public void createLoginSession(String id, String email,String password
//                        ,String contactnumber,String firstnem,String lastname){
                        session.createLoginSession(object.optString("id"),
                                email,
                                password,
                                phonenumber,
                                firstname,
                                lastname,
                                object.optString("type"));
                        builder.dismiss();
                        startActivity(new Intent(NewAccount.this,Participants.class));
                        finish();
                    }
                    if (success.equals("0")){
                        builder.dismiss();
                        new AlertDialog.Builder(NewAccount.this)
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
                    new AlertDialog.Builder(NewAccount.this)
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
                new AlertDialog.Builder(NewAccount.this)
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
                /*
                 *
                 * $email=$_POST['emailh'];
                 * $password=$_POST['passwordh'];
                 * $firstname=$_POST['firstnameh'];
                 * $lastname=$_POST['lastnameh'];
                 * $phonenumber=$_POST['phonenumberh'];
                 * $usertype=$_POST['usertypeh'];
                 * */

                params.put("emailh",email);
                params.put("passwordh",password);
                params.put("firstnameh",firstname);
                params.put("lastnameh",lastname);
                params.put("phonenumberh",phonenumber);
                params.put("usertypeh",usertype);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewAccount.this);
        requestQueue.add(stringRequest);
    }



}
