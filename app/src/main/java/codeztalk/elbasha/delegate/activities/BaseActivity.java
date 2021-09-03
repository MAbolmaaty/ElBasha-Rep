package codeztalk.elbasha.delegate.activities;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.PreferenceHelper;


public class BaseActivity extends AppCompatActivity
{
     public PreferenceHelper preferenceHelper;

    protected void onCreate(Bundle savedInstanceState) {
        MyContextWrapper.changeLanguage(this, "ar");
        MyContextWrapper.forceRTLIfSupported(this, true);
        preferenceHelper = new PreferenceHelper(this);
        BaseActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);


    }
}
