package com.example.antragni.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.antragni.LoginManager;
import com.example.antragni.PostEvent;
import com.example.antragni.R;

public class NotificationsFragment extends Fragment {

    TextView logout;
    TextView addevent,name,type,email;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        addevent=root.findViewById(R.id.addEvents);
        logout = root.findViewById(R.id.logout);
        name = root.findViewById(R.id.name);
        type = root.findViewById(R.id.type);
        email = root.findViewById(R.id.email);

        name.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_FIRSTNAME)
                + " " + new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_LASTNAME));
        email.setText(new LoginManager(getContext()).getUserDetails().get(LoginManager.KEY_EMAIL));
        type.setText(LoginManager.KEY_USERTYPE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                new LoginManager(root.getContext()).logoutUser();
            }
        });

        addevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(root.getContext(), PostEvent.class));
            }
        });



        return root;
    }
}