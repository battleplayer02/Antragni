package com.example.antragni.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.antragni.InsideEvent;
import com.example.antragni.InsideLogin;
import com.example.antragni.R;

import java.util.ArrayList;

public class HomeAdatper extends RecyclerView.Adapter<HomeAdatper.HomeViewHolder> {

    Context ctx;
    ArrayList<HomePojo> homePojoArrayList;

    public HomeAdatper(FragmentActivity ctx, ArrayList<HomePojo> homePojoArrayList) {
        this.ctx = ctx;
        this.homePojoArrayList = homePojoArrayList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.events_card,null);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeViewHolder holder, int position) {
        final HomePojo homePojo = homePojoArrayList.get(position);

        String pro_img=homePojo.getEventpic();
//        byte[] byteArray= Base64.decode(pro_img,Base64.DEFAULT);
//        Bitmap bmp= BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
//        holder.imgcard.setImageBitmap(bmp);
        System.out.println(homePojo.getDatecard()+"123"+homePojo.getPlacecard()+"123 "+homePojo.getTypecard());
        holder.datecard.setText(homePojo.getDatecard());
        holder.placecard.setText(homePojo.getPlacecard());
        holder.typecard.setText(homePojo.getTypecard());
        holder.eventname.setText(homePojo.getEventname());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctx.startActivity(new Intent(ctx, InsideEvent.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return homePojoArrayList.size();    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgcard;
        CardView cardView;
        TextView datecard,placecard,typecard,eventname;
        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
//            imgcard=itemView.findViewById(R.id.eventpic);
            cardView = itemView.findViewById(R.id.allEventsCard);
            datecard=itemView.findViewById(R.id.datecard);
            placecard=itemView.findViewById(R.id.placecard);
            typecard=itemView.findViewById(R.id.typecard);
            eventname=itemView.findViewById(R.id.eventname);
        }
    }
}
