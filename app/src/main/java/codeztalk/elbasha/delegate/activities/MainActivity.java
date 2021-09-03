//package codeztalk.elbasha.delegate.activities;
//
//import android.Manifest;
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.IBinder;
//import android.provider.Settings;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AlertDialog;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.snackbar.Snackbar;
//
//import codeztalk.elbasha.delegate.BuildConfig;
//import codeztalk.elbasha.delegate.R;
//import codeztalk.elbasha.delegate.fragments.CreditInvoicesFragment;
//import codeztalk.elbasha.delegate.fragments.HomeFragment;
//import codeztalk.elbasha.delegate.fragments.PocketFragment;
//import codeztalk.elbasha.delegate.fragments.ProfileFragment;
//import codeztalk.elbasha.delegate.helper.MyContextWrapper;
//import codeztalk.elbasha.delegate.myLocation.LocationUpdatesService;
//
//
//public class MainActivity extends BaseActivity {
//
//
//    private Boolean isHome = true;
//    private TextView textAction;
//    LinearLayout toolbar_base;
//    private BottomNavigationView bottomNavigationView;
//    String fragmentName = "home";
//    private static final int REQUEST_ADD_CLIENT = 1;
//
//
//    //Location
//    private static final String TAG = MainActivity.class.getSimpleName();
//
//    // Used in checking for runtime permissions.
//    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
//
//    // The BroadcastReceiver used to listen from broadcasts from the service.
//    private MyReceiver myReceiver;
//
//    // A reference to the service used to get location updates.
//    private LocationUpdatesService mService = null;
//
//    // Tracks the bound state of the service.
//    private boolean mBound = false;
//
//
//    // Monitors the state of the connection to the service.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mBound = false;
//        }
//    };
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        myReceiver = new MyReceiver();
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Check that the user hasn't revoked permissions by going to Settings.
//
//        checkLocation();
//
//
//        toolbar_base = findViewById(R.id.toolbar_base);
//        textAction = findViewById(R.id.textAction);
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//
//        navigateToFragment(new HomeFragment(), true, getString(R.string.addClient), "home");
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//
//                    navigateToFragment(new HomeFragment(), true, getString(R.string.addClient), "home");
//                    break;
//
//                case R.id.navigation_pocket:
//                    navigateToFragment(new PocketFragment(), false, getString(R.string.addPocket), "pocket");
//                    break;
//
//                case R.id.navigation_credit:
//                    navigateToFragment(new CreditInvoicesFragment(), false, getString(R.string.addPocket), "credit");
//                    break;
//
//                case R.id.navigation_profile:
//                    navigateToFragment(new ProfileFragment(), false, getString(R.string.logout), "profile");
//
//                    break;
//
//
//            }
//
//            return true;
//        });
//
//
//        textAction.setOnClickListener(v -> {
//
//            switch (fragmentName) {
//
//                case "home":
//                    launchAddClient();
//                    break;
//                case "pocket":
//                    launchAddPocket();
//                    break;
//
//                case "profile":
//                    ShowLogOutDialog();
//                    break;
//
//            }
//
//
//        });
//    }
//
//    void launchAddClient() {
//
//        Intent serverIntent = new Intent(this, AddClientActivity.class);
//        startActivityForResult(serverIntent, REQUEST_ADD_CLIENT);
//
//    }
//
//    void launchAddPocket() {
//        Intent i = new Intent(MainActivity.this, AddPocketActivity.class);
//        startActivity(i);
//    }
//
//
//    private void ShowLogOutDialog() {
//        final Dialog dialog = new Dialog(MainActivity.this, R.style.MyDialog);
//        MyContextWrapper.forceRTLIfSupported(MainActivity.this, dialog);
//
//        dialog.setCancelable(true);
//        dialog.setContentView(R.layout.alert_delete);
//        dialog.show();
//
//        TextView yes = dialog.findViewById(R.id.yes);
//        TextView no = dialog.findViewById(R.id.no);
//        TextView alert_message = dialog.findViewById(R.id.alert_message);
//
//        alert_message.setText(getString(R.string.logoutAlert));
//
//        yes.setOnClickListener(v -> {
//
//            preferenceHelper.logOut();
//            launchLogin();
//            dialog.cancel();
//
//
//        });
//
//        no.setOnClickListener(v -> dialog.cancel());
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == REQUEST_ADD_CLIENT) {
//            if (resultCode == Activity.RESULT_OK) {
//                navigateToFragment(new HomeFragment(), true, getString(R.string.addClient), "home");
//
//            }
//
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public void navigateToFragment(Fragment fragment, Boolean inHome, String text, String fragment_name) {
//
//        isHome = inHome;
//        fragmentName = fragment_name;
//
//        if (fragment_name.equalsIgnoreCase("credit")) {
//            toolbar_base.setVisibility(View.GONE);
//
//        } else {
//            toolbar_base.setVisibility(View.VISIBLE);
//
//        }
//
//        try {
//
//            getSupportFragmentManager().beginTransaction().replace(R.id.frame_product, fragment).commitAllowingStateLoss();
//
//            textAction.setText(text);
//        } catch (Exception e) {
//            Log.e("ccc", "Error! Can't replace fragment.", e);
//        }
//
//    }
//
//
//    private void launchLogin() {
//
//        Intent i = new Intent(MainActivity.this, LoginActivity.class);
//        startActivity(i);
//        finish();
//
//    }
//
//    @Override
//    public void onBackPressed() {
//
//        if (isHome)
//            finish();
//        else {
//            navigateToFragment(new HomeFragment(), true, getString(R.string.home), "home");
//            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
//        }
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        // Bind to the service. If the service is in foreground mode, this signals to the service
//        // that since this activity is in the foreground, the service can exit foreground mode.
//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
//
//        if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//
//            new Handler().postDelayed(() -> mService.requestLocationUpdates(), 1000);
//
//
//        }
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
//                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
//    }
//
//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
//        super.onPause();
//    }
//
//    @Override
//    protected void onStop() {
//        if (mBound) {
//            // Unbind from the service. This signals to the service that this activity is no longer
//            // in the foreground, and the service can respond by promoting itself to a foreground
//            // service.
//            unbindService(mServiceConnection);
//            mBound = false;
//        }
////        PreferenceManager.getDefaultSharedPreferences(this)
////                .unregisterOnSharedPreferenceChangeListener(this);
//        super.onStop();
//    }
//
//    /**
//     * Returns the current state of the permissions needed.
//     */
//    private boolean checkPermissions() {
//        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//    }
//
//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//            Snackbar.make(
//                    findViewById(R.id.container),
//                    R.string.permission_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_PERMISSIONS_REQUEST_CODE);
//                        }
//                    })
//                    .show();
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//    /**
//     * Callback received when a permissions request has been completed.
//     */
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        Log.i(TAG, "onRequestPermissionResult");
//        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
//            if (grantResults.length <= 0) {
//                // If user interaction was interrupted, the permission request is cancelled and you
//                // receive empty arrays.
//                Log.i(TAG, "User interaction was cancelled.");
//            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission was granted.
//                mService.requestLocationUpdates();
//            } else {
//                // Permission denied.
//                Snackbar.make(
//                        findViewById(R.id.container),
//                        R.string.permission_denied_explanation,
//                        Snackbar.LENGTH_INDEFINITE)
//                        .setAction(R.string.settings, view -> {
//                            // Build intent that displays the App settings screen.
//                            Intent intent = new Intent();
//                            intent.setAction(
//                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package",
//                                    BuildConfig.APPLICATION_ID, null);
//                            intent.setData(uri);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        })
//                        .show();
//            }
//        }
//    }
//
//    /**
//     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
//     */
//    private static class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
//            if (location != null) {
//
//                Log.e("location",""+location.getLatitude() + "," + location.getLongitude());
//
//
//            }
//        }
//    }
//
//
//    void checkLocation() {
//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        try {
//            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }
//
//        try {
//            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//        }
//
//        if (!gps_enabled && !network_enabled) {
//            // notify user
//            new AlertDialog.Builder(this)
//                    .setMessage(R.string.permission_rationale)
//                    .setPositiveButton(R.string.settings, (paramDialogInterface, paramInt) -> startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
//                    .setNegativeButton(R.string.cancel, null)
//                    .show();
//        } else {
//            if (!checkPermissions()) {
//                requestPermissions();
//            }
//
//        }
//
//
//    }
//
//
//}
