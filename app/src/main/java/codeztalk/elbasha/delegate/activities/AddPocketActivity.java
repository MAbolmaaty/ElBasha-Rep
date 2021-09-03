package codeztalk.elbasha.delegate.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.adapter.FilePhotoAdapter;
import codeztalk.elbasha.delegate.helper.ConnectivityReceiver;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.helper.ProgressDialogHelper;
import codeztalk.elbasha.delegate.models.fileModel;

import static codeztalk.elbasha.delegate.helper.MyHelpers.getCurrentDateEN;
import static codeztalk.elbasha.delegate.helper.MyHelpers.isEmpty;
import static codeztalk.elbasha.delegate.helper.constants.baseURL;
import static codeztalk.elbasha.delegate.helper.constants.postData;

public class AddPocketActivity extends BaseActivity {


    private EditText editMoney;
    private EditText editNotes;
    private TextView textDate;
    private TextView textAdd;


    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener onMYDateSetListener;


    //select image pathes
    String patha;
    private static final int PERMISSION_REQUEST_CODE = 1;

    public FilePhotoAdapter filePhotoAdapter;
    public ArrayList<fileModel> filesArrayList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerPhotos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pocket);

        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText(getString(R.string.addPocket));
        imageBack.setOnClickListener(v -> onBackPressed());

        textDate = findViewById(R.id.textDate);
        editMoney = findViewById(R.id.editMoney);
        editNotes = findViewById(R.id.editNotes);
        textAdd = findViewById(R.id.textAdd);

        textDate.setText(getCurrentDateEN());

        myCalendar = Calendar.getInstance();
        onMYDateSetListener = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);


             String myFormat = "yyyy-dd-MM"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            textDate.setText(sdf.format(myCalendar.getTime()));


        };


//        textDate.setOnClickListener(v -> new DatePickerDialog(this,
//                onMYDateSetListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
//                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        textAdd.setOnClickListener(v -> {
            if (validateInputs())
            {
//                if (filesArrayList.size() > 0)
//                {
//                    uploadPocket();
//
//                } else {
//                    Toast.makeText(this, getString(R.string.empty_files_error), Toast.LENGTH_SHORT).show();
//
//                }

                uploadPocket();

            }
        });


        initializeRecycler();


    }

    void initializeRecycler() {
        recyclerPhotos = findViewById(R.id.recycler_photos);


        linearLayoutManager = new LinearLayoutManager(AddPocketActivity.this);
        recyclerPhotos.setLayoutManager(new LinearLayoutManager(AddPocketActivity.this));
         recyclerPhotos.setHasFixedSize(true);
        filesArrayList = new ArrayList<>();
        filePhotoAdapter = new FilePhotoAdapter(AddPocketActivity.this, filesArrayList);

        recyclerPhotos.setAdapter(filePhotoAdapter);
    }


    public void addImage() {
        if (checkPermission()) {

            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(AddPocketActivity.this);
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(AddPocketActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddPocketActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(AddPocketActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(AddPocketActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileModel photoItem = new fileModel();


                try {
                    InputStream imageStream = AddPocketActivity.this.getContentResolver().openInputStream(result.getUri());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);


                    Log.e(" getByteCount ", "" + selectedImage.getByteCount());
                    Log.e(" getWidth ", "" + selectedImage.getWidth());

                    selectedImage = scaleBitmap(selectedImage, 500, 500);// 400 is for example, replace with desired size


                    Uri newURI = getImageUri(AddPocketActivity.this, selectedImage);

                    Log.e(" getByteCount 22", "" + selectedImage.getByteCount());
                    Log.e(" getWidth 22", "" + selectedImage.getWidth());
                    Log.e(" patha", "" + patha);

                    File target = new File(patha + "/gawal.JPEG");

                    Log.e("target is ", "" + target.getAbsolutePath());
                    Log.e("isFile   ", "" + target.isFile());

                    if (target.exists()) {
//                    target.delete();
                        Log.e("delete", "sss" + target.getName());
//
                    }

                    photoItem.setImageUri(newURI);
                    photoItem.setFileType("image");

                    filesArrayList.add(photoItem);
                    filePhotoAdapter.notifyDataSetChanged();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();


            }
        }


    }

    private Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v("Pictures", "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyymmddhhmmss", Locale.ENGLISH);


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "forsah_" + SDF.format(new Date()) + ".jpg", null);
        patha = path;

        Log.e("path is ", " " + path);
        Log.e("patha is ", " " + patha);
        Log.e("Uri is ", " " + Uri.parse(path));

        return Uri.parse(path);

    }


    private void uploadPocket() {

        if (ConnectivityReceiver.isConnected()) {


            MyHelpers.showNotification(this, getString(R.string.app_name), getString(R.string.uploaded));
            ProgressDialogHelper.showSimpleProgressDialog(this, false);

            List<File> files = new ArrayList<>();

            for (int i = 0; i < filesArrayList.size(); i++) {

                try {
                    files.add(new File(Objects.requireNonNull(MyHelpers.getPath(AddPocketActivity.this,
                            filesArrayList.get(i).getImageUri()))));
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }


            }

            JSONObject jsonObject = new JSONObject();

            try {

                //{"EmpId":"2","TransactionAmount":"120.5","TransactionDate" : "10-March-2020","Notes":"دفع بنزين عربيه"}
                jsonObject.put("EmpId", preferenceHelper.getUserId() + "");
                jsonObject.put("TransactionAmount", editMoney.getText().toString());
                jsonObject.put("Notes", editNotes.getText().toString());
                jsonObject.put("TransactionDate", textDate.getText().toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }
             AndroidNetworking.upload(baseURL+postData)
                    .addMultipartFileList("FileName", files)
                    .addMultipartParameter("Json", jsonObject.toString())
                    .addHeaders("Authorization", preferenceHelper.getUserToken())

                    .setPriority(Priority.HIGH)
                    .build()
                    .setUploadProgressListener((bytesUploaded, totalBytes) -> {
                                // do anything with progress

//                                Log.e("conan : ", "progress => " + bytesUploaded);
//                                Log.e("conan : ", "totalBytes => " + totalBytes);

                            }
                    ).getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {

                    ProgressDialogHelper.removeSimpleProgressDialog();
                    Toast.makeText(AddPocketActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                    Log.e("response", "onResponse: " + response);

                    MyHelpers.showNotification(AddPocketActivity.this, getString(R.string.app_name), getString(R.string.upload_success));

                    filesArrayList.clear();
                    filePhotoAdapter.notifyDataSetChanged();

                    onBackPressed();
                }

                @Override
                public void onError(ANError error) {

                    ProgressDialogHelper.removeSimpleProgressDialog();

                    Log.e("getErrorDetail is ", " : " + error.getErrorDetail());
                    Log.e("getResponse is ", " : " + error.getResponse());
                    Log.e("getErrorBody is ", " : " + error.getErrorBody());
                    Log.e("getMessage is ", " : " + error.getMessage());
                    Log.e("getErrorCode is ", " : " + error.getErrorCode());
                    Log.e("toString is ", " : " + error.toString());
                    Log.e("getCause is ", " : " + error.getCause());
                    Log.e("getLocalizedMessage is ", " : " + error.getLocalizedMessage());


                    if (error.getErrorCode() == 0) {
                        Toast.makeText(AddPocketActivity.this, R.string.connection_failed, Toast.LENGTH_SHORT).show();

                    } else {
                        // handle error
                        try {
                            JSONObject jsonObject = new JSONObject(error.getErrorBody());

                            Log.e("aa", "is : " + jsonObject.getString("message"));
                            MyHelpers.showNotification(AddPocketActivity.this, getString(R.string.app_name), jsonObject.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }
            });

        } else {
            Toast.makeText(this, R.string.connection_failed, Toast.LENGTH_SHORT).show();
        }


    }


    private boolean validateInputs() {
        if (isEmpty(editMoney)) {
            editMoney.setError(AddPocketActivity.this.getString(R.string.empty_money_error));
            return false;
        }

        if (isEmpty(editNotes)) {
            editNotes.setError(AddPocketActivity.this.getString(R.string.empty_notes_error));

            return false;
        }
        if (isEmpty(textDate)) {
            textDate.setError(AddPocketActivity.this.getString(R.string.empty_date_error));

            return false;
        }

        return true;
    }



}
