package codeztalk.elbasha.delegate.activities.printer;

import static codeztalk.elbasha.delegate.helper.printerUtil.imageWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.paperWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerDensity;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerSpeed;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tscdll.TSCActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
import codeztalk.elbasha.delegate.helper.PreferenceHelper;
import codeztalk.elbasha.delegate.models.ClientInvoiceModel;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.models.InvoiceModel;


public class TaskPrintActivity extends BaseActivity {
    private static final String TAG = TaskPrintActivity.class.getSimpleName();

    private static final int REQUEST_ENABLE_BLUETOOTH = 7916;

    TSCActivity TscDll;
    Button scan, connect, task_button_print;
    BluetoothAdapter mBluetoothAdapter = null;
    public static boolean deviceConnected = false;
    public boolean connected = false;

    int quantityNo = 1;
    LinearLayout linearInvoice;
    //ConstraintLayout paper;
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
    FrameLayout mIcPrinterFrame;
    TextView mPrinterName;
    FrameLayout mIcCheckFrame;

    public PreferenceHelper mPreferenceHelper;
    public static ConnectedDevice mConnectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_print);

        Log.d(TAG, "TaskPrintActivity created");
        mIcPrinterFrame = findViewById(R.id.icPrinterFrame);
        mPrinterName = findViewById(R.id.printerName);
        mIcCheckFrame = findViewById(R.id.icCheckFrame);

        mPreferenceHelper = new PreferenceHelper(this);

        clientInvoiceModel = (ClientInvoiceModel) getIntent().getSerializableExtra("clientInvoiceModel");
        invoiceModel = (InvoiceModel) getIntent().getSerializableExtra("invoiceModel");
        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");
        if (getIntent().getStringExtra("printer_mac_address") != null) {
            mConnectedDevice = new ConnectedDevice(getIntent().getStringExtra("printer_name"),
                    getIntent().getStringExtra("printer_mac_address"));
        }
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
        //paper = findViewById(R.id.paper);
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
            if (TscDll.IsConnected) {
                TscDll.closeport();
            }
            startDiscovery();
        });
        task_button_print.setOnClickListener(v -> {
            Log.d(TAG, "print button clicked");
            shareImage(linearInvoice);
            //printQrCode();
        });
//        scan.setOnClickListener(v -> shareImage(linearInvoice));

        initializeInvoice();

//        String qrCodeText = "em";
//
//        MultiFormatWriter writer = new MultiFormatWriter();
//
//        try {
//            BitMatrix matrix = writer.encode(qrCodeText, BarcodeFormat.QR_CODE,
//                    350, 350);
//
//            BarcodeEncoder encoder = new BarcodeEncoder();
//
//            Bitmap bitmap = encoder.createBitmap(matrix);
//
//            mIcQrCode.setImageBitmap(bitmap);
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
    }

//    private void printQrCode(){
//        TSCActivity tscActivity = new TSCActivity();
//        tscActivity.openport(mConnectedDevice.getMacAddress());
//        tscActivity.qrcode(5, 5, "H", "2 x 2", "alphanumeric",
//                "0", "Model 1", "0", "EM");
//        tscActivity.printlabel(1, 1);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "TaskPrintActivity onResume");

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
            return;
        }

        if (mConnectedDevice != null){
            Log.d(TAG, "View Model Printer : " + mConnectedDevice.getName());
            mPrinterName.setText(mConnectedDevice.getName());
            mIcPrinterFrame.setVisibility(View.VISIBLE);
            mPrinterName.setVisibility(View.VISIBLE);
            mIcCheckFrame.setVisibility(View.VISIBLE);
            scan.setText(getString(R.string.change_printer));
            task_button_print.setVisibility(View.VISIBLE);
            TscDll.openport(mConnectedDevice.getMacAddress());
            connected = true;
        } else {
            scan.setText(getString(R.string.connect));
            task_button_print.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connected) {
                connected = false;
                TscDll.closeport();
                //TscDll = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (TscDll.IsConnected)
            TscDll.closeport();
        if (mBluetoothAdapter.isEnabled()) {
            try {
                if (connected) {
                    connected = false;
                    TscDll.closeport();
                    //TscDll = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //mBluetoothAdapter.disable();
        }

        if (invoiceModel != null) {
            removeCashedData();

        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode != RESULT_OK) {
                onBackPressed();
            }
        }
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


    void shareImage(View view) {
        view.setDrawingCacheEnabled(true);

        Log.e("getWidth", "" + view.getWidth());
        Log.e("getHeight", "" + view.getHeight());

//        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);
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
                if (!connected) {
                    Log.d(TAG, "Before print: " + mPreferenceHelper.getPrinter().getName());
                    TscDll.openport(mPreferenceHelper.getPrinter().getMacAddress());
                }
                TscDll.downloadpcx("UL.PCX");
                try {

//                        TscDll.setup(paperWidth, 200, printerSpeed, printerDensity, 0, 0, 0);
                    TscDll.setup(paperWidth, Math.round(getPaperHeight()),
                            printerSpeed, printerDensity, 0, 0, 0);
                    TscDll.clearbuffer();
                    TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");


                    TscDll.sendbitmap_resize(0, 0, bitmap,
                           imageWidth, Math.round(getImageHeight()));
                    TscDll.printlabel(1, quantityNo);
                    TscDll.clearbuffer();
                    TscDll.setup(paperWidth, 40,
                            printerSpeed, printerDensity, 0, 0, 0);
                    TscDll.qrcode(300,
                            0, "H", "5", "A","0","M2",
                            "S7", "مؤسسـة حسـين حـــامد الحـربى للحـلويـات");
                    TscDll.printlabel(1, quantityNo);
                    //TscDll.sendcommand("TEXT 10,10,\"1\",0,12,12,\"EM\"\r\n");
//                    TscDll.printlabel(1, quantityNo);
//                    TscDll.sendcommand("CLS");
//                    TscDll.sendcommand("TEXT 10,10,\"1\",0,12,12,\"EM\"\r\n");
//                    TscDll.sendbitmap_resize(0, 0, bitmap,
//                           imageWidth, Math.round(getImageHeight()));


//                        TscDll.sendbitmap(0, 0, bitmap);


//                        TscDll.closeport();

                } catch (Exception e) {
                    runOnUiThread(new Runnable(){
                        public void run() {
                            Toast.makeText(getBaseContext(), R.string.printer_not_connected,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    e.printStackTrace();
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

    void startDiscovery() {
        // Launch the DeviceListActivity to see devices and do scan
        Intent intent = new Intent(this, DeviceListActivity.class);
        startActivity(intent);
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






