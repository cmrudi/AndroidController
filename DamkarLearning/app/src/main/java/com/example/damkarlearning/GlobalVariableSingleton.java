package com.example.damkarlearning;

/**
 * Created by asus on 01/03/17.
 */

public class GlobalVariableSingleton {
    private static GlobalVariableSingleton mInstance= null;

    public String userId;
    public String userEmail;
    public String locationX;
    public String locationY;

    protected GlobalVariableSingleton(){}

    public static synchronized GlobalVariableSingleton getInstance(){
        if(null == mInstance){
            mInstance = new GlobalVariableSingleton();
        }
        return mInstance;
    }
}
