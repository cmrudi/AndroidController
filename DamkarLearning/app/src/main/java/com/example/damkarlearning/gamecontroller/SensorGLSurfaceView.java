package com.example.damkarlearning.gamecontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.damkarlearning.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ASUS on 21/02/2017.
 */
public class SensorGLSurfaceView extends GLSurfaceView implements SensorEventListener {
    private final SensorGLRenderer mRenderer;

    private SensorManager sensorManager;
    private Sensor gravity_meter;
    private String damkarOrientation = new String();

    private float deltaX = 0;
    private float deltaY = 0;

    private Context newCtx;



    public SensorGLSurfaceView(Context context){

        super(context);

        newCtx = context;

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);


        mRenderer = new SensorGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY) != null) {
            // success! we have an gravity_meter

            gravity_meter = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            sensorManager.registerListener(this, gravity_meter, SensorManager.SENSOR_DELAY_NORMAL);

        } else {
            // fail! we dont have an gravity_meter!
        }

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }


    public String getDamkarOrientation() {
        return damkarOrientation;
    }
        //onResume() register the gravity_meter for listening the events
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravity_meter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //onPause() unregister the gravity_meter for stop listening the events
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferences sp = getContext().getSharedPreferences("SensorInfo", MODE_PRIVATE);
        Boolean isActive = sp.getBoolean("active",false);

        if (isActive) {
            final double alpha = 0.8;

            float[] gravity = new float[3];
            gravity[0] = (float) (alpha * gravity[0] + (1.0 - alpha) * event.values[0]);
            gravity[1] = (float) (alpha * gravity[1] + (1.0 - alpha) * event.values[1]);
            gravity[2] = (float) (alpha * gravity[2] + (1.0 - alpha) * event.values[2]);

            // get the change of the x,y,z values of the gravity_meter

            deltaX = event.values[0] - gravity[0];
            deltaY = event.values[1] - gravity[1];
            //deltaZ = event.values[2] - gravity[2];
            mRenderer.setOrientation(deltaX, deltaY);
            requestRender();
            damkarOrientation = mRenderer.getOrientation();

            try {
                // Get a RequestQueue
                RequestQueue queue = MySingleton.getInstance(newCtx).
                        getRequestQueue();

                String url = "https://damkar-learning.herokuapp.com/direction";
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("direct", damkarOrientation);
                final String mRequestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOG_VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG_VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {

                            responseString = String.valueOf(response.statusCode);

                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                queue.add(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.print("ASSDEKDEUBDBEYHFBEUJ");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onStop() {

    }

}
