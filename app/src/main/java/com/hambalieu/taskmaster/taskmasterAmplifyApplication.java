package com.hambalieu.taskmaster;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class taskmasterAmplifyApplication extends Application
{
    public static final String TAG = "taskmasterapplication";
    @Override
    public void onCreate(){
       super.onCreate();
        try
        {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
        } catch (AmplifyException ae)
        {
            Log.e(TAG, "Error initializing Amplify: " + ae.getMessage(), ae);
        }

    }

}
