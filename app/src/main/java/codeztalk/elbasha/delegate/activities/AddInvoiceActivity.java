package codeztalk.elbasha.delegate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.models.InvoiceModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;


public class AddInvoiceActivity extends BaseActivity {
    private static final String TAG = AddInvoiceActivity.class.getSimpleName();
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
    private ConnectedDevice mConnectedDevice;
//    private boolean validateInputs() {
//        if (isEmpty(editInvoiceNumber)) {
//            editInvoiceNumber.setError(AddInvoiceActivity.this.getString(R.string.empty_invoice_error));
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
        Log.d(TAG, "AddInvoiceActivity created");
        db = new ForsahDB(this);
        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");
        if (getIntent().getStringExtra("printer_mac_address") != null){
            mConnectedDevice = new ConnectedDevice(getIntent().getStringExtra("printer_name"),
                    getIntent().getStringExtra("printer_mac_address"));
        }

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

            editPaid.setText("0");
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
//        textSend.setOnClickListener(v -> launchPrint("25"));
//
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

        if (!textUnPaid.getText().toString().equalsIgnoreCase("")) {
            invoiceModel.setUnPaid(Double.parseDouble(textUnPaid.getText().toString()));

        } else {
            invoiceModel.setUnPaid(0);

        }

        if (!editDiscount.getText().toString().equalsIgnoreCase("")) {
            invoiceModel.setDiscount(Double.parseDouble(editDiscount.getText().toString()));

        } else {
            invoiceModel.setDiscount(0);

        }

//        invoiceModel.setUnPaid(0);


        invoiceModel.setTotalAfter(Double.parseDouble(textFinalTotal.getText().toString()));
        invoiceModel.setTotalAfterDiscount(Double.parseDouble(textTotalAfterDiscount.getText().toString()));
        invoiceModel.setTax(Double.parseDouble(textTax.getText().toString()));
        invoiceModel.setCash(isCash);
        invoiceModel.setInvoiceNumber(invoiceNumber);


        Intent i = new Intent(AddInvoiceActivity.this, TaskPrintActivity.class);
        i.putExtra("invoiceModel", invoiceModel);
        i.putExtra("clientModel", clientModel);
        if (mConnectedDevice != null){
            i.putExtra("printer_name", mConnectedDevice.getName());
            i.putExtra("printer_mac_address", mConnectedDevice.getMacAddress());
        }
        startActivity(i);
        finish();


    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddInvoiceActivity.this, CategoryProductsActivity.class);
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
                        && Double.parseDouble(charSequence.toString()) < finalPrice)
                {
                    calculatePrice(false);
                }
                else
                    {
                        Log.e("conan", "unPaidPrice =");
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
                        && Double.parseDouble(charSequence.toString()) < finalPrice) {

                    calculatePrice(isCash);

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
    public double unPaidPrice = 0;
//    public double paidPrice = 0;


    public void calculatePrice(boolean is_cash) {
        if (!editDiscount.getText().toString().equalsIgnoreCase("")) {
            discountValue = Double.parseDouble(editDiscount.getText().toString());

        }
        else
            {
                discountValue=0;
            }


        totalAfterDiscount = totalPrice - discountValue;
        textTotalAfterDiscount.setText(String.format("%s", totalAfterDiscount));
        textTax.setText(String.format("%s", (totalAfterDiscount * taxValue / 100)));

        finalPrice = (totalAfterDiscount) + (totalAfterDiscount * taxValue / 100);
        textFinalTotal.setText(String.format("%s", finalPrice));


        if (is_cash) {

//            paidPrice = finalPrice;

            editPaid.setEnabled(false);
            editPaid.setText(String.format("%s", finalPrice));

            Log.e("true", "unPaidPrice =" + unPaidPrice);

        } else {
            editPaid.setEnabled(true);




            Log.e("false", "unPaidPrice =" + unPaidPrice);



        }
        unPaidPrice = finalPrice - Double.parseDouble(editPaid.getText().toString());


        textUnPaid.setText(new DecimalFormat("##.##", new DecimalFormatSymbols(Locale.US)).
                format(unPaidPrice));

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
            addInvoiceRequest.setGPSLocation(preferenceHelper.getUserLocation());

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
            Log.e("getTaxValue", " >>===>> " + addInvoiceRequest.getTaxValue());
            Log.e("getTotalAfterDiscount", " >>===>> " + addInvoiceRequest.getTotalAfterDiscount());
            Log.e("getTotalValue", " >>===>> " + addInvoiceRequest.getTotalValue());

            showSimpleProgressDialog(this, false);

            ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

            Call<AddInvoiceResponse> call =
                    apiInterface.addNewInvoice(preferenceHelper.getUserToken(), addInvoiceRequest);


            call.enqueue(new Callback<AddInvoiceResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddInvoiceResponse> call,
                                       @NonNull Response<AddInvoiceResponse> response) {
                    removeSimpleProgressDialog();

                    addInvoiceResponse = response.body();

                    Log.e("conan : ", "  >>==> " + new Gson().toJson(addInvoiceResponse));
                    Log.e("addInvoiceResponse : ", "  >>==> " + response.message());

                    if (addInvoiceResponse != null) {
                        launchPrint(addInvoiceResponse.getInvoiceTaxNumber());

                    } else {
                        //TODO : Remove : launchPrint("231132");
                        launchPrint("231132");
                        Log.e("addInvoiceResponse : ", "  >>==> " + response.message());
                        Toast.makeText(AddInvoiceActivity.this,
                                "" + response.message(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddInvoiceActivity.this,
                                "من فضلك قم بتسجيل الخروج واللدخول مرة اخرى", Toast.LENGTH_SHORT).show();

//                            preferenceHelper.logOut();
//                            launchLogin();
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
            Toast.makeText(this, getString(R.string.empty_product_error), Toast.LENGTH_SHORT).show();
        }
    }

//    void login() {
//
//        List<Xxxx.InvoiceProduct> products = new ArrayList<>();
//
//        for (int i = 0; i < this.hoursOfflineArrayList.size(); i++) {
//
//            products.add(new Xxxx.InvoiceProduct(
//                    hoursOfflineArrayList.get(i).getId() + "",
//                    hoursOfflineArrayList.get(i).getBoxPrice() + "",
//                    hoursOfflineArrayList.get(i).isBox() + "",
//                    hoursOfflineArrayList.get(i).getProductAmount(),
//                    hoursOfflineArrayList.get(i).getTotalPrice() + "",
//                    hoursOfflineArrayList.get(i).getUnitPrice() + "",
//                    hoursOfflineArrayList.get(i).getOfficialUnitPrice() + ""));
//
//        }
//
//        if (products.size() > 0) {
//            Xxxx addInvoiceRequest = new Xxxx();
//
//            addInvoiceRequest.setIssuedBy(preferenceHelper.getUserId());
//            addInvoiceRequest.setClientId(clientModel.getId() + "");
////            addInvoiceRequest.setGPSLocation(preferenceHelper.getUserLocation());
//
//            addInvoiceRequest.setTotalValue(preferenceHelper.getTotalPrice());
//            addInvoiceRequest.setDiscount(String.valueOf(discountValue));
//            addInvoiceRequest.setTotalAfterDiscount(String.valueOf(totalAfterDiscount));
//            addInvoiceRequest.setTaxValue(String.valueOf(totalPrice * taxValue / 100));
//            addInvoiceRequest.setFinalValue(String.valueOf(finalPrice));
//            addInvoiceRequest.setPaidValue(editPaid.getText().toString());
////            addInvoiceRequest.setRemainValue("20");
//
//            addInvoiceRequest.setRemainValue(textUnPaid.getText().toString());
////            addInvoiceRequest.setInvoiceTaxNumber(editInvoiceNumber.getText().toString());
////            addInvoiceRequest.setInvoiceProducts(products);
//
//            if (editNotes.getText().toString().equalsIgnoreCase("")) {
//                addInvoiceRequest.setNotes("notes");
//
//            } else {
//                addInvoiceRequest.setNotes(editNotes.getText().toString());
//
//            }
//
//            Log.e("size", " >>===>> " + products.size());
//            Log.e("getClientId", " >>===>> " + addInvoiceRequest.getClientId());
//            Log.e("getDiscount", " >>===>> " + addInvoiceRequest.getDiscount());
//            Log.e("getFinalValue", " >>===>> " + addInvoiceRequest.getFinalValue());
////            Log.e("getGPSLocation", " >>===>> " + addInvoiceRequest.getGPSLocation());
//            Log.e("getInvoiceTaxNumber", " >>===>> " + addInvoiceRequest.getInvoiceTaxNumber());
//            Log.e("getIssuedBy", " >>===>> " + addInvoiceRequest.getIssuedBy());
//            Log.e("getNotes", " >>===>> " + addInvoiceRequest.getNotes());
//            Log.e("getPaidValue", " >>===>> " + addInvoiceRequest.getPaidValue());
//            Log.e("getRemainValue", " >>===>> " + addInvoiceRequest.getRemainValue());
//            Log.e("getClientId", " >>===>> " + addInvoiceRequest.getTaxValue());
//            Log.e("getTotalAfterDiscount", " >>===>> " + addInvoiceRequest.getTotalAfterDiscount());
//            Log.e("getTotalValue", " >>===>> " + addInvoiceRequest.getTotalValue());
//
//            showSimpleProgressDialog(this, false);
//
//
//            AndroidNetworking.post(baseURL + postInvoice)
//                    .addBodyParameter(addInvoiceRequest) // posting java object
//                    .addHeaders("Authorization", preferenceHelper.getUserToken())
//
//                    .setTag("test")
//                    .setPriority(Priority.MEDIUM)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            // do anything with response
//
//                            Log.e("response", " >>" + response);
//                            removeSimpleProgressDialog();
//
////                        try {
////                            String userToken=response.getString("access_token");
////                            preferenceHelper.setUserToken("Bearer "+userToken);
////                            getEmployeeProfile(editEmail.getText().toString(),"Bearer "+userToken);
////
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
//
//
//                        }
//
//                        @Override
//                        public void onError(ANError error) {
//                            // handle error
//
//                            Log.e("error", " >>" + error);
//
//                            removeSimpleProgressDialog();
//
//                            ProgressDialogHelper.removeSimpleProgressDialog();
//
//                            Log.e("getErrorDetail is ", " : " + error.getErrorDetail());
//                            Log.e("getResponse is ", " : " + error.getResponse());
//                            Log.e("getErrorBody is ", " : " + error.getErrorBody());
//                            Log.e("getMessage is ", " : " + error.getMessage());
//                            Log.e("getErrorCode is ", " : " + error.getErrorCode());
//                            Log.e("toString is ", " : " + error.toString());
//                            Log.e("getCause is ", " : " + error.getCause());
//                            Log.e("getLocalizedMessage is ", " : " + error.getLocalizedMessage());
//
//
//                            if (error.getErrorCode() == 0) {
//                                Toast.makeText(AddInvoiceActivity.this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
//
//                            } else {
//                                // handle error
//                                try {
//                                    JSONObject jsonObject = new JSONObject(error.getErrorBody());
//
//                                    Log.e("aa", "is : " + jsonObject.getString("message"));
//                                    MyHelpers.showNotification(AddInvoiceActivity.this, getString(R.string.app_name), jsonObject.getString("message"));
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//
//                        }
//                    });
//
//
//        } else {
//            Toast.makeText(this, getString(R.string.empty_product_error), Toast.LENGTH_SHORT).show();
//        }
//
//
//    }

}