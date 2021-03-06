package com.example.damkarlearning.tabs;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.damkarlearning.R;
import com.example.damkarlearning.SensorControllerActivity;
import com.example.damkarlearning.TouchControllerActivity;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class Tab1Play extends Fragment implements View.OnClickListener {
    Button sButton;
    Button tButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int screenOrientation = getResources().getConfiguration().orientation;
        if (screenOrientation == ORIENTATION_LANDSCAPE) {
            view = inflater.inflate(R.layout.tab1play_landscape, container, false);
        }
        else {
            view = inflater.inflate(R.layout.tab1play, container, false);
        }
        sButton = (Button) view.findViewById(R.id.sensorButton);
        sButton.setOnClickListener(this);
        tButton = (Button) view.findViewById(R.id.touchButton);
        tButton.setOnClickListener(this);
        return view;
        //return inflater.inflate(R.layout.tab1play, container, false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.sensorButton:
            {
                Intent myIntent = new Intent(getActivity(), SensorControllerActivity.class );
                startActivity(myIntent);

                break;
            }

            case R.id.touchButton:
                Intent myIntent = new Intent(getActivity(), TouchControllerActivity.class );
                startActivity(myIntent);

                break;
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setContentView(R.layout.tab1play_landscape);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            getActivity().setContentView(R.layout.tab1play);
        }
    }
}
