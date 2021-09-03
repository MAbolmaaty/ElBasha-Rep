package codeztalk.elbasha.delegate;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.multidex.MultiDex;



 public class BashaApp extends Application {

    private static BashaApp instance;


    public static final String TAG = BashaApp.class.getSimpleName();



    public static synchronized BashaApp getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Something went horribly wrong!!, no application attached!");
        }
        return instance;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        Log.e("corah", "corah");

    }


    @Override
    public void onCreate() {
        super.onCreate();

         instance = this;

    }


 }
