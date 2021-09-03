package codeztalk.elbasha.delegate.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;


import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.db.ForsahDB;


public class SplashActivity extends BaseActivity {

    ForsahDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        db = new ForsahDB(this);


        preferenceHelper.setTotalPrice("0");
        db.deleteProductTable();
        db.deleteCategoryTable();
        db.deleteClientTable();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Log.e("getUserLocation : ", ">> " + preferenceHelper.getUserLocation());



        new Handler().postDelayed(() -> {

            if (!preferenceHelper.isUserLogged()) {
                launchLogin();
            } else
                launchHome();
        }, 2000);


    }






    @Override
    protected void onStart() {
        super.onStart();
    }

    private void launchLogin() {
        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    private void launchHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }




}
