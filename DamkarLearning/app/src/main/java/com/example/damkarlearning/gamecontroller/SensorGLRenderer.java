package com.example.damkarlearning.gamecontroller;

import android.graphics.drawable.GradientDrawable;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.damkarlearning.shapes.Circle;
import com.example.damkarlearning.shapes.Square;
import com.example.damkarlearning.shapes.Triangle;

import javax.microedition.khronos.opengles.GL10;

public class SensorGLRenderer implements GLSurfaceView.Renderer  {

    private static final String TAG = "SensorGLRenderer";

    private Triangle mTriangle;
    private Square mSquare;
    private Circle mCircle;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle;

    private String myOrientation = new String();

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);


        // Create a rotation transformation for the triangle
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        //Matrix.translateM(mMVPMatrix,0,1,1,0);
        Matrix.setIdentityM(mCircle.mModelMatrix, 0);
        switch (getOrientation()) {
            case "FORWARD": {
                Matrix.translateM(mCircle.mModelMatrix, 0, 0f, 0.3f, 0f);
                break;
            }
            case "BACKWARD": {
                Matrix.translateM(mCircle.mModelMatrix, 0, 0f, -0.3f, 0f);
                break;
            }
            case "RIGHT": {
                Matrix.translateM(mCircle.mModelMatrix, 0, -0.3f, 0f, 0f);
                break;
            }
            case "LEFT": {
                Matrix.translateM(mCircle.mModelMatrix, 0, 0.3f, 0f, 0f);
                break;
            }
            default: {
                Matrix.translateM(mCircle.mModelMatrix, 0, 0f, 0f, 0f);
                break;
            }
        }

        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mCircle.mModelMatrix, 0);

        //Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


        // Draw triangle
        //mTriangle.draw(mMVPMatrix);

        mSquare.draw(mMVPMatrix);

        mCircle.draw(scratch);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        // initialize a triangle

        float triangleCoords[] = {   // in counterclockwise order:
                0.0f,  0.622008459f, 0.0f, // top
                -0.5f, -0.311004243f, 0.0f, // bottom left
                0.5f, -0.311004243f, 0.0f  // bottom right
        };

        float squareCoords[] = {
                -0.3f,  0.3f, 0.0f,   // top left
                -0.3f, -0.3f, 0.0f,   // bottom left
                0.3f, -0.3f, 0.0f,   // bottom right
                0.3f,  0.3f, 0.0f }; // top right

        mTriangle = new Triangle(triangleCoords);
        // initialize a square
        mSquare = new Square(squareCoords);
        // initialize a circle
        mCircle = new Circle();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    public String getOrientation() {
        return myOrientation;
    }

    public void setOrientation(float dx, float dy) {
        myOrientation = classifyOrientation(dx,dy);
    }

    public String classifyOrientation(float dX, float dY) {
        String Ori = new String();
        if (Math.abs(dX) <= 1.0) {
            if (Math.abs(dY) <= 1.0) {
                Ori = "NETRAL";
            }
            else if (dY>1.0) {
                Ori = "BACKWARD";
            }
            else {
                Ori = "FORWARD";
            }

        }
        else if (dX<(-1.0)) {
            if (Math.abs(dX)>Math.abs(dY)) {
                Ori = "RIGHT";
            }
            else {
                if (dY>0.0) {
                    Ori = "BACKWARD";
                }
                else {
                    Ori = "FORWARD";
                }
            }
        }
        else if (dX>1.0) {
            if (Math.abs(dX)>Math.abs(dY)) {
                Ori = "LEFT";
            }
            else {
                if (dY>0.0) {
                    Ori = "BACKWARD";
                }
                else {
                    Ori = "FORWARD";
                }
            }
        }


        return Ori;
    }
}
