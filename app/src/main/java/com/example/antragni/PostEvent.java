package com.example.antragni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostEvent extends AppCompatActivity {

    EditText ename,edate,eplace,edesc,cname,cnumber,eamount,etype,enop;
    Button createEvent,eventImage;
    final Calendar myCalendar = Calendar.getInstance();
    private static final String URL_REGIST ="http://3.20.14.234/antragini/insertevent.php" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_event);

        ename=(EditText)findViewById(R.id.eventName);
        eplace=(EditText)findViewById(R.id.eventPlace);
        edesc=(EditText)findViewById(R.id.eventDescription);
        cname=(EditText)findViewById(R.id.coordinatorName);
        cnumber=(EditText)findViewById(R.id.coordinatorNumber);
        eamount=(EditText)findViewById(R.id.eventAmount);
        edate=(EditText)findViewById(R.id.eventDate);
        etype=(EditText)findViewById(R.id.eventType);
        enop=(EditText)findViewById(R.id.eventnoofparticipant) ;
        createEvent=(Button)findViewById(R.id.createEvent);
        eventImage=(Button)findViewById(R.id.eventImage);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(PostEvent.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Regist();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String str = sdf1.format(new Date());
        edate.setText(sdf.format(myCalendar.getTime())+" "+str);
    }

    public void Regist()
    {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            Toast.makeText(getApplicationContext(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                            String success=jsonObject.getString("success");

                            if (success.equals("1"))
                            {
                                AlertDialog.Builder builder=new AlertDialog.Builder(PostEvent.this)
                                        .setMessage("Event Created Successfully")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                startActivity(new Intent(PostEvent.this,Participants.class));
                                            }
                                        });
                                builder.create().show();
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            AlertDialog.Builder builder=new AlertDialog.Builder(PostEvent.this)
                                    .setMessage("Class Created Unsuccessfully")
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
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                })
        {
            @Override
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("cname",cname.getText().toString());
                params.put("cnumber",cnumber.getText().toString());
                params.put("ename",ename.getText().toString());
                params.put("edate",edate.getText().toString());
                params.put("ecategory",etype.getText().toString());
                params.put("edesc",edesc.getText().toString());
                params.put("eplace",eplace.getText().toString());
                params.put("eamount",eamount.getText().toString());
                params.put("enop",enop.getText().toString());
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
