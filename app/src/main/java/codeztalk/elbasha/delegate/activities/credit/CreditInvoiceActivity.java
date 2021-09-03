package codeztalk.elbasha.delegate.activities.credit;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tscdll.TSCActivity;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.InvoiceModel;

import static codeztalk.elbasha.delegate.helper.printerUtil.imageWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.paperWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerDensity;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerSpeed;


public class CreditInvoiceActivity extends BaseActivity {


    TSCActivity TscDll;
    Button scan, connectButton, task_button_print;

    BluetoothAdapter mBluetoothAdapter = null;

    public static String address;
    public static boolean deviceConnected = false;
    public boolean connected = false;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    String quantityno = "1";
    LinearLayout linearInvoice;
    private Thread mPrintThread;


    TextView textDate;
    TextView textInvoiceNumber;
    TextView textClientName;
    TextView textTotal;
    TextView textDelegateName;
    TextView textPaid;
    TextView textUnPaid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText(getString(R.string.invoiceDetail2));
        imageBack.setOnClickListener(v -> onBackPressed());


        //button
        scan = findViewById(R.id.search);
        task_button_print = findViewById(R.id.task_button_print);
        connectButton = findViewById(R.id.connect_button);

        textDate = findViewById(R.id.textDate);
        linearInvoice = findViewById(R.id.linearInvoice);
        textInvoiceNumber = findViewById(R.id.textInvoiceNumber);
        textClientName = findViewById(R.id.textClientName);
        textTotal = findViewById(R.id.textTotal);

        textDelegateName = findViewById(R.id.textDelegateName);
        textUnPaid = findViewById(R.id.textUnPaid);
        textPaid = findViewById(R.id.textPaid);

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
                Toast.makeText(CreditInvoiceActivity.this, "Already Connected!", Toast.LENGTH_LONG).show();
            } else {
                startDiscovery();
            }
        });
        task_button_print.setOnClickListener(v -> shareImage(linearInvoice));

        initializeInvoice();


    }


    InvoiceModel invoiceModel;

    void initializeInvoice() {

        invoiceModel = (InvoiceModel) getIntent().getSerializableExtra("invoiceModel");


        if (invoiceModel != null) {
            textDate.setText(MyHelpers.getCurrentDate());
            textInvoiceNumber.setText(invoiceModel.getInvoiceNumber());
            textClientName.setText(invoiceModel.getClientName());
//            textTotal.setText(String.valueOf(invoiceModel.getTotalAfter()));
            textTotal.setText(String.valueOf(invoiceModel.getPaid()+invoiceModel.getUnPaid()));
            textDelegateName.setText(invoiceModel.getDelgateMan());
            textPaid.setText(String.valueOf(invoiceModel.getPaid()));
            textUnPaid.setText(String.valueOf(invoiceModel.getUnPaid()));

        }


    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            if(connected)
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
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);


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
//                    TscDll.downloadttf("ARIAL.TTF");
                    try {

//                        TscDll.setup(95, 120, 4, 7, 0, 0, 0);
                        TscDll.setup(paperWidth, 100, printerSpeed, printerDensity, 0, 0, 0);

                        TscDll.clearbuffer();
                        TscDll.sendcommand("SET TEAR ON\n");
                         TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");


                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth, 950);

                        TscDll.printlabel(1, Integer.parseInt(quantityno));
//                        TscDll.closeport();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(CreditInvoiceActivity.this, "Please connect to printer first.", Toast.LENGTH_LONG).show();
                }


            });
            mPrintThread.start();


        }, 500);


    }


    @Override
    public void onBackPressed()
    {
        if (mBluetoothAdapter.isEnabled()) {
            try {
                if(connected)
                {
                    connected = false;
                    TscDll.closeport();
                    TscDll = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            mBluetoothAdapter.disable();
        }


        finish();
    }

    boolean bDiscoveryStarted = false;

    void startDiscovery() {
        if (bDiscoveryStarted)
            return;
        bDiscoveryStarted = true;
        // Launch the DeviceCreditActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceCreditActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CONNECT_DEVICE) {

            //addLog("onActivityResult: requestCode==REQUEST_CONNECT_DEVICE");
            // When DeviceCreditActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                //addLog("resultCode==OK");

                // Get the device MAC address
                String addres = data.getExtras().getString(DeviceCreditActivity.EXTRA_DEVICE_ADDRESS);
                //addLog("onActivityResult: got device=" + address);


                if (addres != null)
                {
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

}

