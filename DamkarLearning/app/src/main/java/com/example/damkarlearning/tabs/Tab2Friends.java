package com.example.damkarlearning.tabs;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.damkarlearning.MenuActivity;
import com.example.damkarlearning.R;
import com.example.damkarlearning.SensorControllerActivity;
import com.example.damkarlearning.UsersLocationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Tab2Friends extends Fragment implements View.OnClickListener {
    private static final String TAG = "Tab2Friend";

    View view;
    private TextView msection_label;

    String response2;

    String id;
    String name;
    String email;
    String username;
    String password;
    String city;

    Button lButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tab2friends, container, false);

        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String url = "https://damkar-learning.herokuapp.com/user";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    System.out.println(response);
                    //response2 = response;
                    try {
                        LinearLayout layout = (LinearLayout) getActivity().findViewById(R.id.asd);

                        Log.d("Test", response);
                        JSONArray obj = new JSONArray(response);

                        for (int i = 0; i < obj.length(); i++) {
                            JSONObject c = obj.getJSONObject(i);
                            id = c.getString("_id");
                            name = c.getString("name");
                            email = c.getString("email");
                            username = c.getString("username");
                            password = c.getString("password");
                            city = c.getString("city");

                            String x = "";
                            x += name + "\n";
                            x += username + "\n";
                            x += email + "\n\n";
                            Button btnTag = new Button(getActivity());
                            layout.addView(btnTag);
                            btnTag.setText(x);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEY", error.toString());
                }
            });
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lButton = (Button) view.findViewById(R.id.locButton);
        lButton.setOnClickListener(this);

        return view;
    }
    // TODO ada yg gaberes
    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(v.getContext(), UsersLocationActivity.class);
        switch (v.getId()) {
            case R.id.locButton: {
                startActivity(myIntent);
                break;
            }
        }
    }
}
