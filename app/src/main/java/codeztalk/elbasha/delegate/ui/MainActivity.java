package codeztalk.elbasha.delegate.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.activities.HomeActivity;
import codeztalk.elbasha.delegate.activities.LoginActivity;
import codeztalk.elbasha.delegate.activities.SplashActivity;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.PreferenceHelper;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ForsahDB db;
    public PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyContextWrapper.changeLanguage(this, "ar");
        MyContextWrapper.forceRTLIfSupported(this, true);
        preferenceHelper = new PreferenceHelper(this);
        MainActivity.this.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        db = new ForsahDB(this);

        preferenceHelper.setTotalPrice("0");
        db.deleteProductTable();
        db.deleteCategoryTable();
        db.deleteClientTable();

        if (!preferenceHelper.isUserLogged()) {
            launchLogin();
        } else{
            launchHome();
        }
    }

    private void launchLogin() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void launchHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
