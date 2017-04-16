package com.example.damkarlearning.gamecontroller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

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
import com.example.damkarlearning.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by ASUS on 22/02/2017.
 */

public class TouchGLSurfaceView extends GLSurfaceView {
    private TouchGLRenderer mRenderer;
    private float previousX;
    private float previousY;
    private String damkarOrientation = new String();
    private String lastDamkarOrientation = new String();
    private Context newCtx;

    public TouchGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        newCtx = context;

        mRenderer = new TouchGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null)
        {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
            {
                if (mRenderer != null)
                {
                    // Ensure we call switchMode() on the OpenGL thread.
                    // queueEvent() is a method of GLSurfaceView that will do this for us.
                    queueEvent(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            mRenderer.switchMode();
                        }
                    });

                    return true;
                }
            }
        }

        return super.onTouchEvent(event);
    }
    */

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;



    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = e.getX();
                float y = e.getY();
                float screenWidth = mRenderer.getScreenWidth();
                float screenHeight = mRenderer.getScreenHeight();

                float sceneX = (x/screenWidth)*2.0f - 1.0f;
                float sceneY = (y/screenHeight)*-2.0f + 1.0f; //if bottom is at -1. Otherwise same as X


                mRenderer.setHover(sceneX,sceneY);
                damkarOrientation = mRenderer.getOrientation();
                System.out.print("DIRECTION : ");
                System.out.println(damkarOrientation);
                requestRender();

                try {
                    // Get a RequestQueue
                    RequestQueue queue = MySingleton.getInstance(newCtx).
                            getRequestQueue();
                    String url = "https://damkar-learning.herokuapp.com/direction/58b1ab105fbd3c000416ab4c";
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
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }



                return true;

            case MotionEvent.ACTION_UP:

                return true;
        }




        //previousX = x;
        //previousY = y;
        return true;
    }
    public void setRenderer(TouchGLRenderer renderer)

    {
        super.setRenderer(renderer);
    }

    public String getDamkarOrientation() {
        return damkarOrientation;
    }


}
