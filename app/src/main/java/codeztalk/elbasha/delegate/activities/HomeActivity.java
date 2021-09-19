package codeztalk.elbasha.delegate.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.fragments.CreditInvoicesFragment;
import codeztalk.elbasha.delegate.fragments.HomeFragment;
import codeztalk.elbasha.delegate.fragments.ProfileFragment;
import codeztalk.elbasha.delegate.fragments.reports.DashBoardFragment;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.PreferenceHelper;
import codeztalk.elbasha.delegate.view_models.PrinterViewModel;

public class HomeActivity extends BaseActivity {

    private Boolean isHome = true;
    private TextView textAction;
    ConstraintLayout toolbar_base;
    LinearLayout linearAction;
    private BottomNavigationView bottomNavigationView;
    String fragmentName = "home";
    private static final int REQUEST_ADD_CLIENT = 1;

    private static final String TAG = HomeActivity.class.getSimpleName();

    private CreditInvoicesFragment existingFragment;

    private PrinterViewModel mPrinterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "Home Activity Created");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        preferenceHelper = new PreferenceHelper(this);
        mPrinterViewModel = ViewModelProviders.of(this).get(PrinterViewModel.class);
        mPrinterViewModel.loadPrinter(preferenceHelper);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                long millisHour = 600000;
                long currentTime = new Date().getTime();

                if (currentTime - preferenceHelper.getUserDate() > millisHour) {
                    preferenceHelper.setUserDate(currentTime);
                    onNewLocation(locationResult.getLastLocation());


                }

            }
        };

        createLocationRequest();
        getLastLocation();


        toolbar_base = findViewById(R.id.toolbar_base);
        toolbar_base.setVisibility(View.GONE);
        textAction = findViewById(R.id.textAction);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        navigateToFragment(new HomeFragment(), true, getString(R.string.addClient), "home");

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateToFragment(new HomeFragment(),
                            true, getString(R.string.addClient), "home");
                    break;
                case R.id.navigation_invoices:
                    navigateToFragment(new CreditInvoicesFragment(),
                            false, getString(R.string.addPocket), "all");
                    break;
                case R.id.navigation_credit:
                    navigateToFragment(new CreditInvoicesFragment(),
                            false, getString(R.string.addPocket), "credit");
                    break;
                case R.id.navigation_notification:
                    navigateToFragment(new DashBoardFragment(),
                            false, getString(R.string.report9), "dashboard");
                    break;
                case R.id.navigation_profile:
                    navigateToFragment(new ProfileFragment(),
                            false, getString(R.string.logout), "profile");
                    break;
            }

            return true;
        });


        textAction.setOnClickListener(v -> {
            switch (fragmentName) {
                case "home":
                    launchAddClient();
                    break;
                case "pocket":
                    launchAddPocket();
                    break;

                case "profile":
                    ShowLogOutDialog();
                    break;
            }
        });
    }

    void launchAddClient() {
        Intent serverIntent = new Intent(this, AddClientActivity.class);
        startActivityForResult(serverIntent, REQUEST_ADD_CLIENT);
    }

    void launchAddPocket() {
        Intent i = new Intent(HomeActivity.this, AddPocketActivity.class);
        startActivity(i);
    }

    private void ShowLogOutDialog() {
        final Dialog dialog = new Dialog(HomeActivity.this, R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(HomeActivity.this, dialog);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_delete);
        dialog.show();

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);
        TextView alert_message = dialog.findViewById(R.id.alert_message);

        alert_message.setText(getString(R.string.logoutAlert));

        yes.setOnClickListener(v -> {

            preferenceHelper.logOut();
            launchLogin();
            dialog.cancel();


        });

        no.setOnClickListener(v -> dialog.cancel());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_ADD_CLIENT) {
            if (resultCode == Activity.RESULT_OK) {
                navigateToFragment(new HomeFragment(), true, getString(R.string.addClient), "home");

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void navigateToFragment(Fragment fragment, Boolean inHome, String text, String fragment_name) {
        isHome = inHome;
        fragmentName = fragment_name;

        if (fragment_name.equalsIgnoreCase("all")
                ||fragment_name.equalsIgnoreCase("credit")
                ||fragment_name.equalsIgnoreCase("dashboard")
                ||fragment_name.equalsIgnoreCase("report10")) {
            toolbar_base.setVisibility(View.GONE);

        } else {
            toolbar_base.setVisibility(View.VISIBLE);

        }


        if (fragment_name.equalsIgnoreCase("credit"))
        {
            existingFragment = CreditInvoicesFragment.newInstance("credit");
            fragment = existingFragment;

        }
        else if (fragment_name.equalsIgnoreCase("all"))
        {
            existingFragment = CreditInvoicesFragment.newInstance("all");
            fragment = existingFragment;


        }


        try {

            getSupportFragmentManager().beginTransaction().replace(R.id.frame_product, fragment)
                    .commitAllowingStateLoss();

            textAction.setText(text);
        } catch (Exception e) {
            Log.e("ccc", "Error! Can't replace fragment.", e);
        }

    }

    private void launchLogin() {
        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {

        if (isHome)
            finish();
        else {
            navigateToFragment(new HomeFragment(), true, getString(R.string.home), "home");
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            new Handler().postDelayed(this::requestLocationUpdates, 1000);
        }

    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.container),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(HomeActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(HomeActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private LocationRequest mLocationRequest;
     private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;

    private LocationCallback mLocationCallback;

    private Location mLocation;



    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
//        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }


    private void onNewLocation(Location location) {
        Log.e(TAG, "New location: " + location);
        Log.e(TAG, "currentTime: " + new Date().getTime());

        preferenceHelper.setUserLocation(location.getLatitude() + "," + location.getLongitude());
        mLocation = location;


        // Update notification content if running as a foreground service.
//        if (serviceIsRunningInForeground(this)) {
//            mNotificationManager.notify(NOTIFICATION_ID, getNotification());
//        }
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();
                        } else {
                            Log.w(TAG, "Failed to get location.");
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

}
