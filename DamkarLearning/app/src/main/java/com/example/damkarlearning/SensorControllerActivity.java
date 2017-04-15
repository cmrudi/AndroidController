package com.example.damkarlearning;

import android.app.Activity;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.damkarlearning.gamecontroller.SensorGLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ASUS on 21/02/2017.
 */

public class SensorControllerActivity extends Activity {

    private GLSurfaceView mGLView;
    private String arduinoDirection = new String();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new SensorGLSurfaceView(this);
        setContentView(mGLView);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*
        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(SensorControllerActivity.this);
            String url = "https://damkar-learning.herokuapp.com/user";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEY", response);
                    System.out.println(response);
                    //response isinya JSON dalam bentuk string dari data friends
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
        */
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("SensorInfo", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", true);
        ed.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Store our shared preference
        SharedPreferences sp = getSharedPreferences("SensorInfo", MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("active", false);
        ed.commit();

    }

}