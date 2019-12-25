package com.example.antragni.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.antragni.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

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

        HomePojo homePojo=new HomePojo();
        homePojo.setDatecard("22th DEC");
        homePojo.setPlacecard("GHRCE");
        homePojo.setTypecard("Technical");
        homePojo.setEventname("Code Stamble");

        System.out.println(homePojo.getDatecard()+"123"+homePojo.getPlacecard()+"123 "+homePojo.getTypecard()+"123 "+homePojo.getEventname());

        homePojoArrayList.add(homePojo);
        homeAdatper=new HomeAdatper(getActivity(),homePojoArrayList);
        recyclerView.setAdapter(homeAdatper);

        return root;
    }
    public void shuffle() {
        HomePojo homePojo=new HomePojo();
        homePojo.setDatecard("23rd DEC");
        homePojo.setPlacecard("GHRCE");
        homePojo.setTypecard("Technical");
        homePojo.setEventname("Code Stamble");
        homePojoArrayList.add(homePojo);
        homeAdatper=new HomeAdatper(getActivity(),homePojoArrayList);
        recyclerView.setAdapter(homeAdatper);
    }

}