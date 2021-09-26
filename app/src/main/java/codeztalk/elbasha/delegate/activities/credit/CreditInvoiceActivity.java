package codeztalk.elbasha.delegate.activities.credit;

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

import com.example.tscdll.TSCActivity;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.helper.PreferenceHelper;
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.models.InvoiceModel;


public class CreditInvoiceActivity extends BaseActivity {

    private static final String TAG = CreditInvoiceActivity.class.getSimpleName();

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

    InvoiceModel invoiceModel;

    FrameLayout mIcPrinterFrame;
    TextView mPrinterName;
    FrameLayout mIcCheckFrame;

    boolean bDiscoveryStarted = false;
    private ConnectedDevice mConnectedDevice;
    public static ConnectedDevice mSelectedDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Log.d(TAG, "CreditInvoiceActivity OnCreate");

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

        mIcPrinterFrame = findViewById(R.id.icPrinterFrame);
        mPrinterName = findViewById(R.id.printerName);
        mIcCheckFrame = findViewById(R.id.icCheckFrame);

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

        preferenceHelper = new PreferenceHelper(this);
        mConnectedDevice = preferenceHelper.getPrinter();

        scan.setOnClickListener(view ->
        {
            if (TscDll.IsConnected) {
                TscDll.closeport();
            }
            startDiscovery();
        });

        task_button_print.setOnClickListener(v -> shareImage(linearInvoice));

        initializeInvoice();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            return;
        }

        if (mSelectedDevice != null) {
            preferenceHelper.setPrinter(mSelectedDevice);
            mPrinterName.setText(mSelectedDevice.getName());
            mIcPrinterFrame.setVisibility(View.VISIBLE);
            mPrinterName.setVisibility(View.VISIBLE);
            mIcCheckFrame.setVisibility(View.VISIBLE);
            task_button_print.setVisibility(View.VISIBLE);
            scan.setText(getString(R.string.change_printer));
            TscDll.openport(mSelectedDevice.getMacAddress());
            connected = true;
            return;
        }

        if (mConnectedDevice != null) {
            mPrinterName.setText(mConnectedDevice.getName());
            mIcPrinterFrame.setVisibility(View.VISIBLE);
            mPrinterName.setVisibility(View.VISIBLE);
            mIcCheckFrame.setVisibility(View.VISIBLE);
            task_button_print.setVisibility(View.VISIBLE);
            scan.setText(getString(R.string.change_printer));
            TscDll.openport(mConnectedDevice.getMacAddress());
            connected = true;
        } else {
            task_button_print.setVisibility(View.INVISIBLE);
            scan.setText(getString(R.string.connect));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (connected) {
                connected = false;
                TscDll.closeport();
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CONNECT_DEVICE) {
//
//            //addLog("onActivityResult: requestCode==REQUEST_CONNECT_DEVICE");
//            // When DeviceCreditActivity returns with a device to connect
//            if (resultCode == Activity.RESULT_OK) {
//                //addLog("resultCode==OK");
//
//                // Get the device MAC address
//                String addres = data.getExtras().getString(DeviceCreditActivity.EXTRA_DEVICE_ADDRESS);
//                //addLog("onActivityResult: got device=" + address);
//
//
//                if (addres != null) {
//                    TscDll.openport(address);
//                    connected = true;
//                }
//
//                if (deviceConnected || addres != null) {
//                    scan.setText(getString(R.string.connected));
//                    task_button_print.setVisibility(View.VISIBLE);
//                } else {
//                    scan.setText(getString(R.string.connect));
//                    task_button_print.setVisibility(View.GONE);
//                }
//            }
//
//            bDiscoveryStarted = false;
//
//        }
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                onBackPressed();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void initializeInvoice() {
        invoiceModel = (InvoiceModel) getIntent().getSerializableExtra("invoiceModel");
        if (invoiceModel != null) {
            textDate.setText(MyHelpers.getCurrentDate());
            textInvoiceNumber.setText(invoiceModel.getInvoiceNumber());
            textClientName.setText(invoiceModel.getClientName());
            textTotal.setText(String.valueOf(invoiceModel.getPaid() + invoiceModel.getUnPaid()));
            textDelegateName.setText(invoiceModel.getDelgateMan());
            textPaid.setText(String.valueOf(invoiceModel.getPaid()));
            textUnPaid.setText(String.valueOf(invoiceModel.getUnPaid()));
        }
    }

    void shareImage(View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.setDrawingCacheEnabled(false);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        String printerMacAddress;

        if (mSelectedDevice != null){
            printerMacAddress = mSelectedDevice.getMacAddress();
        } else {
            printerMacAddress = mConnectedDevice.getMacAddress();
        }

        new Handler().postDelayed(() -> {

            if (mPrintThread != null)
                mPrintThread.interrupt();

            mPrintThread = new Thread(() -> {
                Looper.prepare();

                if (!connected) {
                    TscDll.openport(printerMacAddress);
                }

                TscDll.downloadpcx("UL.PCX");
                try {
                    boolean firstReceipt = true;
                    int copies = 2;

                    while (copies > 0){
                        // print copy

                        if (firstReceipt) {
                            TscDll.clearbuffer();
                            TscDll.sendcommand("TEXT 0,0,\"A.FNT\",0,0,0\r\n");
                            TscDll.printlabel(1, 1);
                            firstReceipt = false;
                        }

                        TscDll.clearbuffer();
                        TscDll.setup(paperWidth, 100, printerSpeed,
                                printerDensity, 0, 0, 0);
                        TscDll.sendcommand("SET TEAR ON\n");
                        TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");
                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth,
                                950);
                        TscDll.printlabel(1, 1);

                        // Qr Code
                        TscDll.clearbuffer();
                        TscDll.setup(paperWidth, 50,
                                printerSpeed, 4, 0, 0, 0);
                        String [] s1 = textInvoiceNumber.getText().toString().split("-");
                        String s2 = s1[1] + "-" + s1[0];
                        String content = "مؤسسـة حسـين حـــامد الحـربى للحـلويـات" + "\n" +
                                "فاتورة ضريبية رقم : " + textInvoiceNumber.getText().toString() + "\n" +
                                "اسم العميل : " + textClientName.getText().toString() + "\n" +
                                "الرقم الضريبي : " + "310213123900003" + "\n" +
                                "التاريخ : " + textDate.getText().toString() + "\n" +
                                "الاجمالي : " + textTotal.getText().toString() + "\n" +
                                "المبلغ المحصل : " + textPaid.getText().toString() + "\n" +
                                "المبلغ المتبقي : " + textUnPaid.getText().toString() + "\n" ;
                        TscDll.qrcode(280,
                                0, "L", "4", "A","0","M2",
                                "S7", content);
                        TscDll.printlabel(1, 1);

                        copies--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            mPrintThread.start();
        }, 500);
    }

    void startDiscovery() {
        if (bDiscoveryStarted)
            return;
        bDiscoveryStarted = true;
        // Launch the DeviceCreditActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceCreditActivity.class);
        //startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
        startActivity(serverIntent);
    }
}

