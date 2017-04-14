package com.example.damkarlearning.tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.damkarlearning.GlobalVariableSingleton;
import com.example.damkarlearning.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Tab3Profile extends Fragment {

    String id;
    String name;
    String email;
    String usernameStr;
    String password;
    String city;

    TextView fullName;
    TextView username;
    TextView useremail;
    TextView userLevel;
    TextView userExp;
    TextView userCity;
    TextView textLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab3profile, container, false);
        fullName = (TextView) view.findViewById(R.id.fullName);
        username = (TextView) view.findViewById(R.id.username);
        useremail = (TextView) view.findViewById(R.id.useremail);
        userLevel = (TextView) view.findViewById(R.id.userLevel);
        userExp = (TextView) view.findViewById(R.id.userExp);
        userCity = (TextView) view.findViewById(R.id.userCity);
        textLocation = (TextView) view.findViewById(R.id.locationText);


        try {
            final GlobalVariableSingleton globalVar = GlobalVariableSingleton.getInstance();
            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String url = "https://damkar-learning.herokuapp.com/user/";
            url = url.concat(globalVar.userId);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    System.out.println(response);
                    //response2 = response;

                    try {

                        Log.d("Test", response);
                        JSONObject c = new JSONObject(response);
                        id = c.getString("_id");
                        name = c.getString("name");
                        email = c.getString("email");
                        usernameStr = c.getString("username");
                        password = c.getString("password");
                        city = c.getString("city");


                        fullName.setText(name);
                        username.setText(usernameStr);
                        useremail.setText(email);
                        userCity.setText(city);
                        userLevel.setText("Level ".concat("1"));
                        userExp.setText("Experience ".concat("0"));
                        textLocation.setText(globalVar.locationX.concat(",").concat(globalVar.locationY));

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


        return view;
    }
}
