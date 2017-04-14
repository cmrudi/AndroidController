package com.example.damkarlearning;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.example.damkarlearning.gamecontroller.SensorGLSurfaceView;
import com.example.damkarlearning.gamecontroller.TouchGLSurfaceView;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ASUS on 22/02/2017.
 */

public class TouchControllerActivity extends Activity {
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new TouchGLSurfaceView(this);
        setContentView(mGLView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TouchGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TouchGLSurfaceView.onPause();
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);

        // make adjustments for screen ratio
        float ratio = (float) width / height;
        gl.glMatrixMode(GL10.GL_PROJECTION);        // set matrix to projection mode
        gl.glLoadIdentity();                        // reset the matrix to its default state
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7);  // apply the projection matrix
    }

}

