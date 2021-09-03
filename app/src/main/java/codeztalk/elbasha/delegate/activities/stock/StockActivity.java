package codeztalk.elbasha.delegate.activities.stock;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tscdll.TSCActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.activities.BaseActivity;
import codeztalk.elbasha.delegate.adapter.StockAdapter;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ProductModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.printerUtil.imageWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.paperWidth;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerDensity;
import static codeztalk.elbasha.delegate.helper.printerUtil.printerSpeed;

public class StockActivity extends BaseActivity {

    private RecyclerView recyclerProducts;


    StockAdapter hoursOfflineAdapter;
    ArrayList<ProductModel> hoursOfflineArrayList;
    ImageView imageFilter;
    EditText editSearch;
    CharSequence search = "";
    LinearLayout linearToolbar;


    TSCActivity TscDll;
    Button scan, connect, task_button_print;

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

    ImageView imageLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        recyclerProducts = findViewById(R.id.recyclerProducts);
        imageFilter = findViewById(R.id.imageFilter);
        editSearch = findViewById(R.id.editSearch);
        imageLogo = findViewById(R.id.imageLogo);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                hoursOfflineAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initializeHoursOffline();
        initializeViews();


        //button
        scan = findViewById(R.id.search);
        task_button_print = findViewById(R.id.task_button_print);
        connect = findViewById(R.id.connect);

        textDate = findViewById(R.id.textDate);
        textDate.setText(MyHelpers.getCurrentDate());
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
                Toast.makeText(StockActivity.this, "Already Connected!", Toast.LENGTH_LONG).show();
            } else {
                startDiscovery();
            }
        });
        task_button_print.setOnClickListener(v -> shareImage(linearInvoice));


    }

    void initializeViews() {
        linearToolbar = findViewById(R.id.linearToolbar);


        imageFilter.setVisibility(View.VISIBLE);
        imageFilter.setImageResource(R.drawable.ic_back);
        imageFilter.setPadding(5, 5, 5, 5);
        linearToolbar.setBackgroundColor(Color.parseColor("#00489A"));

        imageFilter.setOnClickListener(v -> onBackPressed());
//        imageFilter.setOnClickListener(v -> shareImage(linearInvoice));
    }

    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();

        recyclerProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StockActivity.this);
        recyclerProducts.setLayoutManager(mLayoutManager);
        hoursOfflineAdapter = new StockAdapter(hoursOfflineArrayList, this);

        recyclerProducts.setAdapter(hoursOfflineAdapter);

        getProductsOnline();


    }

    private void getProductsOnline() {
        Log.e("on", "on");

        showSimpleProgressDialog(this, false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<ProductModel>> call = apiInterface.getProducts(
                preferenceHelper.getUserId()
                , preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response.body());

                if (response.body() != null) {

//                    hoursOfflineArrayList.addAll(response.body());
//                     hoursOfflineAdapter.notifyDataSetChanged();

                    for (ProductModel productModel : response.body()) {
                        if (productModel.getUnitStockQty() > 0) {

                            hoursOfflineArrayList.add(productModel);
                            hoursOfflineAdapter.notifyDataSetChanged();
                        }


                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            if (connected) {
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

                        TscDll.setup(paperWidth, Math.round(getPaperHeight()), printerSpeed, printerDensity, 0, 0, 0);
//                        TscDll.setup(paperWidth, 70, printerSpeed, printerDensity, 0, 0, 0);

                        TscDll.clearbuffer();
                        TscDll.sendcommand("SET TEAR ON\n");
                        TscDll.sendcommand("PUTPCX 100,300,\"UL.PCX\"\n");


                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth, Math.round(getImageHeight()));
//                        TscDll.sendbitmap_resize(0, 0, bitmap, imageWidth, 500);


                        TscDll.printlabel(1, Integer.parseInt(quantityno));
//                        TscDll.closeport();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(StockActivity.this, "Please connect to printer first.", Toast.LENGTH_LONG).show();
                }


            });
            mPrintThread.start();


        }, 1000);


    }

    public int getPaperHeight()
    {
        return (hoursOfflineArrayList.size()*10)+70;


    }

    public int getImageHeight()
    {

        return (hoursOfflineArrayList.size()*80)+500;



    }

    @Override
    public void onBackPressed() {
        if (mBluetoothAdapter.isEnabled()) {
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


        finish();
    }

    boolean bDiscoveryStarted = false;

    void startDiscovery() {
        if (bDiscoveryStarted)
            return;
        bDiscoveryStarted = true;
        // Launch the DeviceStockActivity to see devices and do scan
        Intent serverIntent = new Intent(this, DeviceStockActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CONNECT_DEVICE) {

            //addLog("onActivityResult: requestCode==REQUEST_CONNECT_DEVICE");
            // When DeviceStockActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                //addLog("resultCode==OK");

                // Get the device MAC address
                String addres = Objects.requireNonNull(data.getExtras()).getString(DeviceStockActivity.EXTRA_DEVICE_ADDRESS);
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


    int getPaperWidth(int size) {
        if (size < 10)
            return 100;

        return 0;
    }


    public void sendbitmap_resize(int x_coordinates, int y_coordinates, Bitmap original_bitmap, int resize_width, int resize_height) {
        Bitmap gray_bitmap = null;
        Bitmap binary_bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPurgeable = true;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//        try {
//            BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
//        } catch (IllegalArgumentException var27) {
//            var27.printStackTrace();
//        } catch (SecurityException var28) {
//            var28.printStackTrace();
//        } catch (IllegalAccessException var29) {
//            var29.printStackTrace();
//        } catch (NoSuchFieldException var30) {
//            var30.printStackTrace();
//        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(original_bitmap, resize_width, resize_height, false);
        gray_bitmap = this.bitmap2Gray(resizedBitmap);
        binary_bitmap = this.gray2Binary(gray_bitmap);
        String x_axis = Integer.toString(x_coordinates);
        String y_axis = Integer.toString(y_coordinates);
        String picture_wdith = Integer.toString((binary_bitmap.getWidth() + 7) / 8);
        String picture_height = Integer.toString(binary_bitmap.getHeight());
        String mode = Integer.toString(0);
        String command = "BITMAP " + x_axis + "," + y_axis + "," + picture_wdith + "," + picture_height + "," + mode + ",";
        byte[] stream = new byte[(binary_bitmap.getWidth() + 7) / 8 * binary_bitmap.getHeight()];
        int Width_bytes = (binary_bitmap.getWidth() + 7) / 8;
        int Width = binary_bitmap.getWidth();
        int Height = binary_bitmap.getHeight();

        int y;
        for(y = 0; y < Height * Width_bytes; ++y) {
            stream[y] = -1;
        }

        for(y = 0; y < Height; ++y) {
            for(int x = 0; x < Width; ++x) {
                int pixelColor = binary_bitmap.getPixel(x, y);
                int colorR = Color.red(pixelColor);
                int colorG = Color.green(pixelColor);
                int colorB = Color.blue(pixelColor);
                int total = (colorR + colorG + colorB) / 3;
                if (total == 0) {
                    stream[y * ((Width + 7) / 8) + x / 8] ^= (byte)(128 >> x % 8);
                }
            }
        }

        TscDll.sendcommand(command);
        TscDll.sendcommand(stream);
        TscDll.sendcommand("\r\n");
    }

    public Bitmap bitmap2Gray(Bitmap bmSrc) {
        int width = bmSrc.getWidth();
        int height = bmSrc.getHeight();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0F);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0.0F, 0.0F, paint);
        return bmpGray;
    }

    public Bitmap gray2Binary(Bitmap graymap) {
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        Bitmap binarymap = null;
        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                int col = binarymap.getPixel(i, j);
                int alpha = col & -16777216;
                int red = (col & 16711680) >> 16;
                int green = (col & '\uff00') >> 8;
                int blue = col & 255;
                int gray = (int)((double)((float)red) * 0.3D + (double)((float)green) * 0.59D + (double)((float)blue) * 0.11D);
//                short gray;
                if (gray <= 127) {
                    gray = 0;
                } else {
                    gray = 255;
                }

                int newColor = alpha | gray << 16 | gray << 8 | gray;
                binarymap.setPixel(i, j, newColor);
            }
        }

        return binarymap;
    }

}
