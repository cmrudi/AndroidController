package com.example.damkarlearning.gamecontroller;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.example.damkarlearning.shapes.Circle;
import com.example.damkarlearning.shapes.Square;
import com.example.damkarlearning.shapes.Triangle;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;


/**
 * Created by ASUS on 21/02/2017.
 */
public class TouchGLRenderer implements GLSurfaceView.Renderer  {

    private static final String TAG = "TensorGLRenderer";

    private Triangle[] mTriangle = new Triangle[4];
    private Square mSquare;

    private boolean mBlending = false;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private float screenHeight;
    private float screenWidth;


    public float getScreenHeight() {
        return screenHeight;
    }

    public float getScreenWidth() {
        return screenWidth;
    }

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


        // DRAW OBJECTS

        mSquare.draw(mMVPMatrix);

        for (int i=0; i<4; i++) {
            mTriangle[i].draw(mMVPMatrix);
        }

    }

    @Override
    public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // UP
        float triangleCoords0[] = {   // in counterclockwise order:
            0.0f,  0.5f, 0.0f, // top
            -0.2f, 0.2f, 0.0f, // bottom left
            0.2f, 0.2f, 0.0f  // bottom right
        };
        mTriangle[0] = new Triangle(triangleCoords0);

        // RIGHT
        float triangleCoords1[] = {   // in counterclockwise order:
            0.2f,  0.2f, 0.0f, // top
            0.5f, 0.0f, 0.0f, // bottom left
            0.2f, -0.2f, 0.0f  // bottom right
        };
        mTriangle[1] = new Triangle(triangleCoords1);

        // DOWN
        float triangleCoords2[] = {   // in counterclockwise order:
            0.2f,  -0.2f, 0.0f, // top
            0.0f, -0.5f, 0.0f, // bottom left
            -0.2f, -0.2f, 0.0f  // bottom right
        };
        mTriangle[2] = new Triangle(triangleCoords2);

        // LEFT
        float triangleCoords3[] = {   // in counterclockwise order:
            -0.2f,  -0.2f, 0.0f, // top
            -0.5f, 0.0f, 0.0f, // bottom left
            -0.2f, 0.2f, 0.0f  // bottom right
        };
        mTriangle[3] = new Triangle(triangleCoords3);


        float squareCoords[] = {
                -0.2f,  0.2f, 0.0f,   // top left
                -0.2f, -0.2f, 0.0f,   // bottom left
                0.2f, -0.2f, 0.0f,   // bottom right
                0.2f,  0.2f, 0.0f }; // top right


        // initialize a square
        mSquare = new Square(squareCoords);


    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            screenHeight = (float) height;
            screenWidth = (float) width;
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



    public void switchMode()
    {
        mBlending = !mBlending;

        if (mBlending)
        {
            // No culling of back faces
            GLES20.glDisable(GLES20.GL_CULL_FACE);

            // No depth testing
            GLES20.glDisable(GLES20.GL_DEPTH_TEST);

            // Enable blending
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        }
        else
        {
            // Cull back faces
            GLES20.glEnable(GLES20.GL_CULL_FACE);

            // Enable depth testing
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);

            // Disable blending
            GLES20.glDisable(GLES20.GL_BLEND);
        }
    }

    // TODO hover
    public void setHover(float x,float y) {
        System.out.println(x+" "+y);
        if ((x>=(-0.2f)) &&(x<=0.2f) && (y>=0.2f) &&(y<=0.5f)) {
            myOrientation = "FORWARD";
            System.out.println(myOrientation);
            mTriangle[0].draw(mMVPMatrix);
            mTriangle[0].hover(mMVPMatrix);
        }
        else if ((x>=(-0.2f)) &&(x<=0.2f) && (y>=-0.5f) &&(y<=-0.2f)) {
            myOrientation = "BACKWARD";
            System.out.println(myOrientation);
            mTriangle[2].hover(mMVPMatrix);
        }
        else if ((x>=(0.4f)) &&(x<=0.9f) && (y>=-0.2f) &&(y<=0.2f)) {
            myOrientation = "RIGHT";
            System.out.println(myOrientation);
            mTriangle[1].hover(mMVPMatrix);
        }
        else if ((x>=(-0.9f)) &&(x<=-0.4f) && (y>=-0.2f) &&(y<=0.2f)) {
            myOrientation = "LEFT";
            System.out.println(myOrientation);
            mTriangle[3].hover(mMVPMatrix);
        }
    }
}
