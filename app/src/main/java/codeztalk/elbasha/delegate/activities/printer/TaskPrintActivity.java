package codeztalk.elbasha.delegate.activities.printer;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tscdll.TSCActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.activities.categoryProduct.Product;
import codeztalk.elbasha.delegate.adapter.Product2SelectedAdapter;
import codeztalk.elbasha.delegate.adapter.ProductSelectedAdapter;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ClientInvoiceModel;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.InvoiceModel;

import static codeztalk.elbasha.delegate.helper.printerUtil.imageWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.paperWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerDensity;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerSpeed;


public class TaskPrintActivity extends BaseActivity {

    TSCActivity TscDll;
    Button scan, connect, task_button_print;

    BluetoothAdapter mBluetoothAdapter = null;

    public static String address;
    public static boolean deviceConnected = false;
    public boolean connected = false;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    int quantityNo = 1;


    LinearLayout linearInvoice;


    ProductSelectedAdapter hoursOfflineAdapter;
    ArrayList<Product> hoursOfflineArrayList;

    Product2SelectedAdapter product2SelectedAdapter;
    ClientInvoiceModel clientInvoiceModel;

    ForsahDB db;
    InvoiceModel invoiceModel;
    ClientModel clientModel;
    RecyclerView recyclerProducts;

    private Thread mPrintThread;


    TextView textDate;
    TextView textTime;
    TextView textInvoiceNumber;
    TextView textClientName;
    TextView textTotal;
    TextView textTax;
    TextView textTotalAfter;
    TextView textTotalAfterDiscount;
    TextView textDiscount;

    TextView textType;
    TextView textDelegateName;
    TextView textPaid;
    TextView textUnPaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_print);


        clientInvoiceModel = (ClientInvoiceModel) getIntent().getSerializableExtra("clientInvoiceModel");
        invoiceModel = (InvoiceModel) getIntent().getSerializableExtra("invoiceModel");
        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");

        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText(getString(R.string.invoiceDetail));
        imageBack.setOnClickListener(v -> onBackPressed());


        textDate = findViewById(R.id.textDate);
        textTime = findViewById(R.id.textTime);
        textInvoiceNumber = findViewById(R.id.textInvoiceNumber);
        textClientName = findViewById(R.id.textClientName);
        textTotal = findViewById(R.id.textTotal);
        textTax = findViewById(R.id.textTax);
        textTotalAfterDiscount = findViewById(R.id.textTotalAfterDiscount);
        textDiscount = findViewById(R.id.textDiscount);
        textTotalAfter = findViewById(R.id.textTotalAfter);
        textType = findViewById(R.id.textType);
        textDelegateName = findViewById(R.id.textDelegateName);
        textUnPaid = findViewById(R.id.textUnPaid);
        textPaid = findViewById(R.id.textPaid);
        recyclerProducts = findViewById(R.id.recyclerProducts);


        //button
        scan = findViewById(R.id.search);
        task_button_print = findViewById(R.id.task_button_print);
        connect = findViewById(R.id.connect);

        linearInvoice = findViewById(R.id.linearInvoice);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //tsc printer
        TscDll = new TSCActivity();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        //scan button
        scan.setOnClickListener(view ->
        {
            if (connected) {
                Toast.makeText(TaskPrintActivity.this, "Already Connected!", Toast.LENGTH_LONG).show();
            } else {
                startDiscovery();
            }
        });
        task_button_print.setOnClickListener(v -> shareImage(linearInvoice));
//        scan.setOnClickListener(v -> shareImage(linearInvoice));

        initializeInvoice();


    }


    void initializeInvoice() {

        if (invoiceModel != null) {
            textDate.setText(getCurrentDate());
            textTime.setText(getCurrentTime());
            textInvoiceNumber.setText(invoiceModel.getInvoiceNumber());

            textClientName.setText(invoiceModel.getClientName());
            textTotal.setText(String.valueOf(invoiceModel.getTotal()));
            textTotalAfterDiscount.setText(String.valueOf(invoiceModel.getTotalAfterDiscount()));
            textDiscount.setText(String.valueOf(invoiceModel.getDiscount()));

            textTax.setText(String.valueOf(invoiceModel.getTax()));
            textTotalAfter.setText(String.valueOf(invoiceModel.getTotalAfter()));
            textType.setText(getInvoiceType(invoiceModel.isCash()));
            textDelegateName.setText(invoiceModel.getDelgateMan());
            textPaid.setText(String.valueOf(invoiceModel.getPaid()));
            textUnPaid.setText(String.valueOf(invoiceModel.getUnPaid()));


            quantityNo = getCopyNo(invoiceModel.isCash());

            initializeHoursOffline();

            Log.e("initializeHoursOffline", ">>>>>");

        } else if (clientInvoiceModel != null) {


            String actionDate = MyHelpers.formatDateFromOneToAnother(clientInvoiceModel.getActionDate(),
                    "yyyy-MM-dd'T'hh:mm:ss", "dd-MMM-yyyy");

            String actionTime = MyHelpers.formatDateFromOneToAnother(clientInvoiceModel.getActionDate(),
                    "yyyy-MM-dd'T'hh:mm:ss", "hh:mm aa");

            textDate.setText(actionDate);
            textTime.setText(actionTime);
            textInvoiceNumber.setText(clientInvoiceModel.getInvoiceTaxNumber());

            textClientName.setText(clientInvoiceModel.getClientName());
            textTotal.setText(String.valueOf(clientInvoiceModel.getTotalValue()));
            textTotalAfterDiscount.setText(String.valueOf(clientInvoiceModel.getTotalAfterDiscount()));
            textDiscount.setText(String.valueOf(clientInvoiceModel.getDiscount()));

            textTax.setText(String.valueOf(clientInvoiceModel.getTaxValue()));
            textTotalAfter.setText(String.valueOf(clientInvoiceModel.getTotalAfterDiscount()));
            textType.setText(getInvoiceType(!clientInvoiceModel.getIsCredit()));
            textDelegateName.setText(preferenceHelper.getEmpName());
            textPaid.setText(String.valueOf(clientInvoiceModel.getPaidValue()));
            textUnPaid.setText(String.valueOf(clientInvoiceModel.getRemainValue()));


            quantityNo = getCopyNo(!clientInvoiceModel.getIsCredit());

            initializeProductsOnline();
            Log.e("lizeProductsOnline", ">>>>>");


        }


    }


    public void initializeProductsOnline() {
        //initialize views


        recyclerProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducts.setLayoutManager(mLayoutManager);
        product2SelectedAdapter = new Product2SelectedAdapter(clientInvoiceModel.getmProducts(), this);
        recyclerProducts.setAdapter(product2SelectedAdapter);
        product2SelectedAdapter.notifyDataSetChanged();


    }

    public void initializeHoursOffline() {
        //initialize views
        db = new ForsahDB(this);

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


    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            if (connected)
            {
                connected = false;
                TscDll.closeport();
                TscDll = null;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void shareImage(View view) {
        view.setDrawingCacheEnabled(true);

        Log.e("getWidth", "" + view.getWidth());
        Log.e("getHeight", "" + view.getHeight());

//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);


        Log.e("getImageHeight", " px >> " + Math.round(getImageHeight()));
        Log.e("getPaperHeight", " mm >> " + Math.round(getPaperHeight()));


        new Handler().postDelayed(() -> {


            if (mPrintThread != null)
                mPrintThread.interrupt();

            mPrintThread = new Thread(() -> {
                Looper.prepare();


                if (address != null && !address.equals("")) {
                    if (!connected) {

                        TscDll.openport(address);
                    }
                    TscDll.downloadpcx("UL.PCX");
                    try {

//                        TscDll.setup(paperWidth, 200, printerSpeed, printerDensity, 0, 0, 0);
                        TscDll.setup(paperWidth, Math.round(getPaperHeight()), printerSpeed, printerDensity, 0, 0, 0);
                        TscDll.clearbuffer();
                        TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");


//                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth, 1500);
                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth, Math.round(getImageHeight()));
//                        TscDll.sendbitmap(0, 0, bitmap);

                        TscDll.printlabel(1, quantityNo);
//                        TscDll.closeport();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(TaskPrintActivity.this, "Please connect to printer first.", Toast.LENGTH_LONG).show();
                }


            });
            mPrintThread.start();


        }, 1000);


    }


    public int getPaperHeight() {
        if (invoiceModel != null) {
            return (hoursOfflineArrayList.size() * 10) + 100;

        } else if (clientInvoiceModel != null) {
            return (clientInvoiceModel.getmProducts().size() * 10) + 100;

        } else return 100;

    }

    public int getImageHeight() {

        if (invoiceModel != null) {
            return (hoursOfflineArrayList.size() * 80) + 750;


        } else if (clientInvoiceModel != null) {
            return (clientInvoiceModel.getmProducts().size() * 80) + 750;


        } else return 750;


    }

    @Override
    public void onBackPressed() {
        if (mBluetoothAdapter.isEnabled())
        {
            try {
                if (connected) {
                    connected = false;
                    TscDll.closeport();
                    TscDll = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            mBluetoothAdapter.disable();
        }

        if (invoiceModel != null) {
            removeCashedData();

        }
        finish();
    }

    boolean bDiscoveryStarted = false;

    void startDiscovery() {
        if (bDiscoveryStarted)
            return;
        bDiscoveryStarted = true;
        // Launch the DeviceListActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CONNECT_DEVICE) {

            //addLog("onActivityResult: requestCode==REQUEST_CONNECT_DEVICE");
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                //addLog("resultCode==OK");

                // Get the device MAC address
                String addres = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                //addLog("onActivityResult: got device=" + address);


                if (addres != null) {
                    TscDll.openport(address);
                    connected = true;
                }

                if (deviceConnected || addres != null) {
                    scan.setText(getString(R.string.connected));
                    task_button_print.setVisibility(View.VISIBLE);
                } else {
                    scan.setText(getString(R.string.connect));
                    task_button_print.setVisibility(View.GONE);
                }
            }

            bDiscoveryStarted = false;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        if (mBluetoothAdapter != null) {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                // Otherwise, setup the comm session
            }
        }
        super.onStart();
    }


    String getInvoiceType(boolean isCash) {
        if (isCash)
            return getString(R.string.payCash);

        else
            return getString(R.string.pay2);
    }

    int getCopyNo(boolean isCash) {
        if (isCash)
            return 1;

        else
            return 2;
    }

    public String getCurrentDate() {
        Locale locale = new Locale("ar");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", locale);
        return sdf.format(date);
    }

    public String getCurrentTime() {

        Locale locale = new Locale("ar");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", locale);
        return sdf.format(date);
    }

    void removeCashedData() {
        db.deleteCategoryTable();
        db.deleteProductTable();
        preferenceHelper.setTotalPrice("0");
    }
}






