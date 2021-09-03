package codeztalk.elbasha.delegate.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.BuildConfig;
import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.AddInvoiceRequest;
import codeztalk.elbasha.delegate.aPIS.responses.AddInvoiceResponse;
import codeztalk.elbasha.delegate.activities.categoryProduct.CategoryProductsActivity;
import codeztalk.elbasha.delegate.activities.categoryProduct.Product;
import codeztalk.elbasha.delegate.activities.printer.TaskPrintActivity;
import codeztalk.elbasha.delegate.adapter.ProductSelectedAdapter;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.helper.BottomNavigationBehavior;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.InvoiceModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class AddInvoiceActivity2 extends BaseActivity {

    private static final String TAG = AddInvoiceActivity2.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private FusedLocationProviderClient mFusedLocationClient;
    protected Location mLastLocation;

    String userLocation;


    private RecyclerView recyclerProducts;

    ProductSelectedAdapter hoursOfflineAdapter;
    ArrayList<Product> hoursOfflineArrayList;


    TextView textClientName;
    public TextView textTotal;
    TextView textTax;
    TextView textTotalAfterDiscount;
    TextView textFinalTotal;
    TextView textUnPaid;
    TextView textUpdate;
    TextView textSend;

    EditText editPaid;
    EditText editDiscount;
    //    EditText editInvoiceNumber;
    EditText editNotes;


    ForsahDB db;

    ClientModel clientModel;
    InvoiceModel invoiceModel;


    boolean isCash = true;


//    private boolean validateInputs() {
//        if (isEmpty(editInvoiceNumber)) {
//            editInvoiceNumber.setError(AddInvoiceActivity2.this.getString(R.string.empty_invoice_error));
//            return false;
//        }
//
//
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);

        db = new ForsahDB(this);
        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText(getString(R.string.addInvoice));

        imageBack.setOnClickListener(v -> onBackPressed());

        RadioButton radioCash = findViewById(R.id.radioCash);
        RadioButton radioNoCash = findViewById(R.id.radioNoCash);


        radioCash.setOnClickListener(v -> {

            isCash = true;
            calculatePrice(true);

        });

        radioNoCash.setOnClickListener(v -> {

            isCash = false;
            calculatePrice(false);

        });


        recyclerProducts = findViewById(R.id.recyclerProducts);

        textClientName = findViewById(R.id.textClientName);
        textTotal = findViewById(R.id.textTotal);
        textTax = findViewById(R.id.textTax);
        textTotalAfterDiscount = findViewById(R.id.textTotalAfterDiscount);
        textFinalTotal = findViewById(R.id.textFinalTotal);
        textUnPaid = findViewById(R.id.textUnPaid);
        textUpdate = findViewById(R.id.textUpdate);
        textSend = findViewById(R.id.textSend);

        editPaid = findViewById(R.id.editPaid);
        editDiscount = findViewById(R.id.editDiscount);
//        editInvoiceNumber = findViewById(R.id.editInvoiceNumber);
        editNotes = findViewById(R.id.editNotes);


        textUpdate.setOnClickListener(v -> onBackPressed());


        textSend.setOnClickListener(v -> addNewInvoice());

        initializeHoursOffline();
        initializeViews();
        calculatePrice(true);
    }


    void launchPrint(String invoiceNumber) {
        invoiceModel = new InvoiceModel();

        invoiceModel.setClientName(clientModel.getClientName());
        invoiceModel.setDelgateMan(preferenceHelper.getUserName());
        invoiceModel.setTotal(Double.parseDouble(textTotal.getText().toString()));
        invoiceModel.setPaid(Double.parseDouble(editPaid.getText().toString()));

        if(!textUnPaid.getText().toString().equalsIgnoreCase(""))
        {
            invoiceModel.setUnPaid(Double.parseDouble(textUnPaid.getText().toString()));

        }
        else
        {
            invoiceModel.setUnPaid(0);

        }

        invoiceModel.setTotalAfter(Double.parseDouble(textFinalTotal.getText().toString()));
        invoiceModel.setTax(Double.parseDouble(textTax.getText().toString()));
        invoiceModel.setCash(isCash);
        invoiceModel.setInvoiceNumber(invoiceNumber);


        Intent i = new Intent(AddInvoiceActivity2.this, TaskPrintActivity.class);
        i.putExtra("invoiceModel", invoiceModel);
        i.putExtra("clientModel", clientModel);
        startActivity(i);
        finish();


    }

    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(AddInvoiceActivity2.this, CategoryProductsActivity.class);
        i.putExtra("clientModel", clientModel);
        startActivity(i);
        finish();
    }


    void initializeViews() {
        LinearLayout containerNav = findViewById(R.id.containerNav);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        totalPrice = Double.parseDouble(preferenceHelper.getTotalPrice());
        textClientName.setText(clientModel.getClientName());
        textTotal.setText(String.format("%s", preferenceHelper.getTotalPrice()));

        editPaid.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {


            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equalsIgnoreCase("")
                        && Double.parseDouble(charSequence.toString()) < finalPrice) {
                    calculatePrice(false);

                } else {
//                        calculatePrice(true);

                    Log.e(">>>>>>", "eeee");
                }


            }
        });

        editDiscount.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {


            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equalsIgnoreCase("")
                        && Double.parseDouble(charSequence.toString()) < totalPrice) {

                    calculatePrice(true);

                } else {
//                        calculatePrice(true);

                    Log.e(">>>>>>", "eeee");
                }


            }
        });

    }


    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();
        hoursOfflineArrayList.clear();

        hoursOfflineArrayList = db.getSelectedCategoryProducts();

        recyclerProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducts.setLayoutManager(mLayoutManager);
        hoursOfflineAdapter = new ProductSelectedAdapter(hoursOfflineArrayList, this);
        recyclerProducts.setAdapter(hoursOfflineAdapter);
        hoursOfflineAdapter.notifyDataSetChanged();


    }


    public double totalPrice = 0;
    double taxValue = 15;
    double totalAfterDiscount = 0;

    double discountValue = 0;
    public double finalPrice = 0;


    public void calculatePrice(boolean is_cash) {
        if (!editDiscount.getText().toString().equalsIgnoreCase("")) {
            discountValue = Double.parseDouble(editDiscount.getText().toString());

        }


        totalAfterDiscount = totalPrice - discountValue;
        textTotalAfterDiscount.setText(String.format("%s", totalAfterDiscount));
        textTax.setText(String.format("%s", (totalAfterDiscount * taxValue / 100)));
        finalPrice = (totalAfterDiscount) + (totalAfterDiscount * taxValue / 100);
        textFinalTotal.setText(String.format("%s", finalPrice));


        if (is_cash) {

            editPaid.setText(String.format("%s", finalPrice));
            editPaid.setEnabled(false);

        } else {
            editPaid.setEnabled(true);

        }
//        textUnPaid.setText(String.format("%s", finalPrice - Double.parseDouble(editPaid.getText().toString())));
        textUnPaid.setText(new DecimalFormat("##.##").format(finalPrice - Double.parseDouble(editPaid.getText().toString())));

        //tv.setText(new DecimalFormat("##.##").format(i2));
    }


    AddInvoiceResponse addInvoiceResponse;

    void addNewInvoice() {
        List<AddInvoiceRequest.InvoiceProduct> products = new ArrayList<>();

        for (int i = 0; i < this.hoursOfflineArrayList.size(); i++) {

            products.add(new AddInvoiceRequest.InvoiceProduct(
                    hoursOfflineArrayList.get(i).getId() + "",
                    hoursOfflineArrayList.get(i).getBoxPrice() + "",
                    hoursOfflineArrayList.get(i).isBox() + "",
                    hoursOfflineArrayList.get(i).getProductAmount(),
                    hoursOfflineArrayList.get(i).getTotalPrice() + "",
                    hoursOfflineArrayList.get(i).getUnitPrice() + "",
                    hoursOfflineArrayList.get(i).getOfficialUnitPrice() + ""));

        }

        if (products.size() > 0) {
            AddInvoiceRequest addInvoiceRequest = new AddInvoiceRequest();

            addInvoiceRequest.setIssuedBy(preferenceHelper.getUserId());
            addInvoiceRequest.setClientId(clientModel.getId() + "");
            addInvoiceRequest.setGPSLocation(userLocation);

            addInvoiceRequest.setTotalValue(preferenceHelper.getTotalPrice());
            addInvoiceRequest.setDiscount(String.valueOf(discountValue));
            addInvoiceRequest.setTotalAfterDiscount(String.valueOf(totalAfterDiscount));
            addInvoiceRequest.setTaxValue(String.valueOf(totalPrice * taxValue / 100));
            addInvoiceRequest.setFinalValue(String.valueOf(finalPrice));
            addInvoiceRequest.setPaidValue(editPaid.getText().toString());
            addInvoiceRequest.setRemainValue(textUnPaid.getText().toString());
//            addInvoiceRequest.setInvoiceTaxNumber(editInvoiceNumber.getText().toString());
            addInvoiceRequest.setInvoiceProducts(products);

            if (editNotes.getText().toString().equalsIgnoreCase("")) {
                addInvoiceRequest.setNotes("notes");

            } else {
                addInvoiceRequest.setNotes(editNotes.getText().toString());

            }

            Log.e("size", " >>===>> " + products.size());
            Log.e("getClientId", " >>===>> " + addInvoiceRequest.getClientId());
            Log.e("getDiscount", " >>===>> " + addInvoiceRequest.getDiscount());
            Log.e("getFinalValue", " >>===>> " + addInvoiceRequest.getFinalValue());
            Log.e("getGPSLocation", " >>===>> " + addInvoiceRequest.getGPSLocation());
            Log.e("getInvoiceTaxNumber", " >>===>> " + addInvoiceRequest.getInvoiceTaxNumber());
            Log.e("getIssuedBy", " >>===>> " + addInvoiceRequest.getIssuedBy());
            Log.e("getNotes", " >>===>> " + addInvoiceRequest.getNotes());
            Log.e("getPaidValue", " >>===>> " + addInvoiceRequest.getPaidValue());
            Log.e("getRemainValue", " >>===>> " + addInvoiceRequest.getRemainValue());
            Log.e("getClientId", " >>===>> " + addInvoiceRequest.getTaxValue());
            Log.e("getTotalAfterDiscount", " >>===>> " + addInvoiceRequest.getTotalAfterDiscount());
            Log.e("getTotalValue", " >>===>> " + addInvoiceRequest.getTotalValue());

            showSimpleProgressDialog(this, false);

            ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

            Call<AddInvoiceResponse> call = apiInterface.addNewInvoice(preferenceHelper.getUserToken(), addInvoiceRequest);


            call.enqueue(new Callback<AddInvoiceResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddInvoiceResponse> call, @NonNull Response<AddInvoiceResponse> response) {
                    removeSimpleProgressDialog();

                    addInvoiceResponse = response.body();

                    Log.e("addInvoiceResponse : ", "  >>==> " + new Gson().toJson(addInvoiceResponse));
                    Log.e("addInvoiceResponse : ", "  >>==> " + response);

                    if (addInvoiceResponse != null) {
                        launchPrint(addInvoiceResponse.getInvoiceTaxNumber());

//                        Log.e(TAG, "" + noResponse.getClientName());

//                        launchHome();
                    }


                }

                @Override
                public void onFailure(@NonNull Call<AddInvoiceResponse> call, @NonNull Throwable t) {
                    removeSimpleProgressDialog();
                    Log.e("fail", "is : " + call.toString());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.empty_days_error), Toast.LENGTH_SHORT).show();
        }


    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        mLastLocation = task.getResult();


                        userLocation = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();
                        Log.e(TAG, "userLocation: " + userLocation);


                    } else {
                        Log.w(TAG, "getLastLocation:exception", task.getException());
                        showSnackbar(getString(R.string.no_location_detected));
                    }
                });
    }

    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(AddInvoiceActivity2.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    view -> startLocationPermissionRequest());

        } else {
            Log.i(TAG, "Requesting permission");

            startLocationPermissionRequest();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {

                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {

                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        view -> {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        });
            }
        }
    }


}