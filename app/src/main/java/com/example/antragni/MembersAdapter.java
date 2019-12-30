package com.example.antragni;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MembersViewHolder> {
    private Context mCtx;
    ArrayList<ShowMembers> blackboardList;

    public MembersAdapter(FragmentActivity activity, ArrayList<ShowMembers> blackboardArrayList) {
        this.mCtx = activity;
        this.blackboardList = blackboardArrayList;
    }

    @NonNull
    @Override
    public MembersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.members, null);
        return new MembersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MembersViewHolder holder, int position) {
        final ShowMembers blackboard = blackboardList.get(position);
        holder.member.setText(blackboard.getEmail());
        holder.attendence.setText(blackboard.getAttendence());
        holder.attendence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "click hua " + blackboard.getId(), Toast.LENGTH_SHORT).show();
                if (holder.attendence.getText().toString().equals("Present")) {
                    new Thread() {
                        @Override
                        public void run() {
                            StringRequest postRequest = new StringRequest(Request.Method.POST, "http://3.20.14.234/antragini/setattenendence.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                System.out.println("attendencd: " + response);

                                                final JSONObject jsonObject = new JSONObject(response);
                                                // Process your json here as required
                                                if (jsonObject.getString("success").equals("1")) {

                                                    Toast.makeText(mCtx, blackboard.getEmail() + " is " + "Absent", Toast.LENGTH_SHORT).show();
                                                    holder.attendence.setText("Absent");
                                                } else {
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
                                    params.put("id", blackboard.getId());
                                    params.put("attendence", "Absent");
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                            requestQueue.add(postRequest);
                        }
                    }.start();

                } else if (holder.attendence.getText().toString().equals("Absent")) {
                    new Thread() {
                        @Override
                        public void run() {
                            StringRequest postRequest = new StringRequest(Request.Method.POST, "http://3.20.14.234/antragini/setattenendence.php",
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                final JSONObject jsonObject = new JSONObject(response);
                                                // Process your json here as required
                                                System.out.println("attendencd: " + response);
                                                if (jsonObject.getString("success").equals("1")) {
                                                    Toast.makeText(mCtx, blackboard.getEmail() + " is " + "Present", Toast.LENGTH_SHORT).show();
                                                    holder.attendence.setText("Present");
                                                } else {
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
                                    params.put("id", blackboard.getId());
                                    params.put("attendence", "Present");
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                            requestQueue.add(postRequest);
                        }
                    }.start();
                    holder.attendence.setText("Present");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blackboardList.size();
    }

    class MembersViewHolder extends RecyclerView.ViewHolder {

        public Button attendence;
        public TextView member;

        public MembersViewHolder(@NonNull View itemView) {
            super(itemView);
            member = itemView.findViewById(R.id.textMember);
            attendence = itemView.findViewById(R.id.attendence);
        }
    }
}