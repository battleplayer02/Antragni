package com.example.antragni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.antragni.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String URL_PRODUCTS ="http://himanshushekhar.ml/antragini/eventselectquery.php" ;
    RecyclerView recyclerView;
    ArrayList<HomePojo> homePojoArrayList= new ArrayList<>();
    HomeAdatper homeAdatper;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeToRefresh);

        recyclerView = (RecyclerView)root.findViewById(R.id.eventsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shuffle();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        Regist();
        return root;
    }

    public void shuffle() {
        Regist();
    }



    public void Regist()
    {    StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PRODUCTS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        //converting the string to json array object
                        System.out.println(response+"response");
                        JSONArray array=new JSONArray(response);
                        //traversing through all the object
                        JSONObject w= array.getJSONObject(0);

                        if(!w.getString("success").equals("0"))
                        {

                            if(homePojoArrayList.size()>0)
                            {
                                homePojoArrayList.clear();
                            }
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject word = array.getJSONObject(i);
                                HomePojo showBlackboard=new HomePojo();
                                showBlackboard.setEventid(word.optString("eventid"));
                                showBlackboard.setEventname(word.optString("eventname"));
                                showBlackboard.setTypecard(word.optString("category"));
                                showBlackboard.setPlacecard(word.optString("location"));
                                showBlackboard.setDatecard(word.optString("eventdate"));
                                showBlackboard.setEventamount(word.optString("cost"));

                                homePojoArrayList.add(showBlackboard);
                            }
                            //creating adapter object and setting it to recyclerview
                            HomeAdatper adapter = new HomeAdatper(getActivity(), homePojoArrayList);
                            recyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
//    {
//        @Override
//        protected Map<String,String> getParams() throws AuthFailureError
//        {
//            Map<String,String> params=new HashMap<>();
//            params.put("email",sessionManager.getUserDetails().get("email"));
//            return params;
//        }
//    };


        Volley.newRequestQueue(getContext()).add(stringRequest);

    }
}