package codeztalk.elbasha.delegate.activities;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.FileUtils;
import codeztalk.elbasha.delegate.aPIS.requests.AddCreditRequest;
import codeztalk.elbasha.delegate.aPIS.responses.AddCreditResponse;
import codeztalk.elbasha.delegate.aPIS.responses.NoResponse;
import codeztalk.elbasha.delegate.activities.credit.CreditInvoiceActivity;
import codeztalk.elbasha.delegate.adapter.InvoiceAdapter;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ClientInvoiceModel;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.InvoiceModel;
import codeztalk.elbasha.delegate.views.MyRecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class ClientProfileActivity extends BaseActivity {

    ImageView imageBack;
    TextView textEmployeeName;

    private static final String TAG = ClientProfileActivity.class.getSimpleName();

    ArrayList<ClientInvoiceModel> clientInvoiceArrayList;
    private MyRecyclerView recyclerAll;
    InvoiceAdapter hoursOfflineAdapter;

    ClientModel clientModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> onBackPressed());

        recyclerAll = findViewById(R.id.recyclerAll);
        recyclerAll.setEmptyView(findViewById(R.id.emptyView));
        recyclerAll.setEmptyImageView((findViewById(R.id.emptyImage)));

        textEmployeeName = findViewById(R.id.textEmployeeName);
        textEmployeeName.setText(String.format("%s\t:\t%s", getString(R.string.invoices), clientModel.getClientName()));


        initializeHoursOffline();

    }

    public void initializeHoursOffline() {
        //initialize views
        clientInvoiceArrayList = new ArrayList<>();
        clientInvoiceArrayList.clear();


        recyclerAll.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerAll.setLayoutManager(mLayoutManager);

        hoursOfflineAdapter = new InvoiceAdapter(clientInvoiceArrayList, this,"profile");
        recyclerAll.setAdapter(hoursOfflineAdapter);
        getClientInvoices();


    }


    private void getClientInvoices() {

        clientInvoiceArrayList.clear();
        showSimpleProgressDialog(this, false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<ClientInvoiceModel>> call = apiInterface.getClientInvoices(
                clientModel.getId() + "",
                preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<ClientInvoiceModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClientInvoiceModel>> call, @NonNull Response<List<ClientInvoiceModel>> response) {
                removeSimpleProgressDialog();

                Log.e("response : ", "  >> " + new Gson().toJson(response.body()));
                Log.e("clientModel.getId() : ", "  >> " +clientModel.getId());
                Log.e(" .getUserToken() : ", "  >> " + preferenceHelper.getUserToken());


                if (response.body() != null) {
                    clientInvoiceArrayList.addAll(response.body());
                    hoursOfflineAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClientInvoiceModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    AddCreditResponse addCreditResponse;
    double unPaid;
    InvoiceModel invoiceModel;

    private void addUnPaidInvoice(ClientInvoiceModel clientInvoiceModel, String paidValue, String notes) {

        unPaid=clientInvoiceModel.getRemainValue()-Double.parseDouble(paidValue);

        showSimpleProgressDialog(this, false);
        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        AddCreditRequest addCreditRequest = new AddCreditRequest();

        addCreditRequest.setClientId(clientModel.getId() + "");
        addCreditRequest.setInvoiceId(clientInvoiceModel.getId()+"");
        addCreditRequest.setIssuedBy(preferenceHelper.getUserId());
        addCreditRequest.setIssuedDate(MyHelpers.getCurrentDateEN());
        addCreditRequest.setNotes(notes);
        addCreditRequest.setPaidValue(paidValue);


        Call<AddCreditResponse> call = apiInterface.addUnPaidInvoice(
                preferenceHelper.getUserToken(),
                addCreditRequest);



        call.enqueue(new Callback<AddCreditResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddCreditResponse> call, @NonNull Response<AddCreditResponse> response) {
                removeSimpleProgressDialog();

                addCreditResponse = response.body();

                Log.e(TAG, "" + response);

                if (addCreditResponse != null) {

                    getClientInvoices();
                    Toast.makeText(ClientProfileActivity.this, getString(R.string.getCredit), Toast.LENGTH_SHORT).show();

                    invoiceModel = new InvoiceModel();

                    invoiceModel.setClientName(clientModel.getClientName());
                    invoiceModel.setDelgateMan(preferenceHelper.getUserName());
                    invoiceModel.setTotal(clientInvoiceModel.getTotalValue());
                    invoiceModel.setTotalAfter(clientInvoiceModel.getFinalValue());
                    invoiceModel.setPaid(Double.parseDouble(paidValue));
                    invoiceModel.setUnPaid(unPaid);

                    invoiceModel.setInvoiceNumber(clientInvoiceModel.getInvoiceTaxNumber());

                    Intent i = new Intent(ClientProfileActivity.this, CreditInvoiceActivity.class);
                     i.putExtra("invoiceModel", invoiceModel);
                     startActivity(i);


                }
            }

            @Override
            public void onFailure(@NonNull Call<AddCreditResponse> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    public void ShowLogOutDialog(ClientInvoiceModel clientInvoiceModel) {
        final Dialog dialog = new Dialog(ClientProfileActivity.this, R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(ClientProfileActivity.this, dialog);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_add_invoice);
        dialog.show();

        EditText editMoney = dialog.findViewById(R.id.editMoney);
        EditText editNotes = dialog.findViewById(R.id.editNotes);
        TextView textAdd = dialog.findViewById(R.id.textAdd);
        ImageView image_exit = dialog.findViewById(R.id.image_exit);

        TextView textInvoiceRemain = dialog.findViewById(R.id.textInvoiceRemain);
        textInvoiceRemain.setText(String.format("%s\t:\t%s", getString(R.string.unPaid), clientInvoiceModel.getRemainValue()));


        textAdd.setOnClickListener(v ->
        {
            if (!editMoney.getText().toString().equalsIgnoreCase("")) {
                if (clientInvoiceModel.getRemainValue() >= Double.parseDouble(editMoney.getText().toString())) {
                    addUnPaidInvoice(clientInvoiceModel,
                            editMoney.getText().toString(),
                            editNotes.getText().toString());
                    dialog.cancel();
                } else {
                    Toast.makeText(ClientProfileActivity.this, getString(R.string.creditvalue), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ClientProfileActivity.this, getString(R.string.empty_money_error), Toast.LENGTH_SHORT).show();

            }


        });

        image_exit.setOnClickListener(v -> dialog.cancel());
    }

    Uri userPhotoUri;
    ImageView imageSignature;

    public void ShowSignatureDialog(String invoiceID) {
        final Dialog dialog = new Dialog(ClientProfileActivity.this, R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(ClientProfileActivity.this, dialog);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_add_signature);
        dialog.show();

        imageSignature = dialog.findViewById(R.id.imageSignature);
        TextView textAdd = dialog.findViewById(R.id.textAdd);
        ImageView image_exit = dialog.findViewById(R.id.image_exit);

        imageSignature.setOnClickListener(v -> CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(ClientProfileActivity.this));

        textAdd.setOnClickListener(v ->
        {
            addSignature(invoiceID);
            dialog.cancel();

        });

        image_exit.setOnClickListener(v -> dialog.cancel());
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == -1) {
                this.userPhotoUri = result.getUri();
                this.imageSignature.setImageURI(this.userPhotoUri);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                result.getError();


            }
        }
    }

    private void addSignature(String invoiceId) {

        showSimpleProgressDialog(this, false);
        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);


        Call<ResponseBody> call = apiInterface.addSignature(invoiceId,
                preferenceHelper.getUserToken(),
                prepareFilePart("FileName", userPhotoUri));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                removeSimpleProgressDialog();


                Log.e(TAG, "" + response.body());

                getClientInvoices();
                Toast.makeText(ClientProfileActivity.this, getString(R.string.getSignature), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String name, Uri fileUri) {
        File file = FileUtils.getFile(ClientProfileActivity.this, fileUri);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        return MultipartBody.Part.createFormData(name, file.getName(), requestFile);
    }



    public void showDeleteDialog(String id) {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(this, dialog);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_confirm);
        dialog.show();

        TextView yes = dialog.findViewById(R.id.yes);
        TextView no = dialog.findViewById(R.id.no);
        TextView alert_message = dialog.findViewById(R.id.alert_message);
        EditText editPassword = dialog.findViewById(R.id.editPassword);

        alert_message.setText(getString(R.string.invoiceAlert));

        yes.setOnClickListener(v -> {

            if (editPassword.getText().toString().equalsIgnoreCase("123456"))
            {
                deleteInvoice(id);
                dialog.cancel();
            }




        });

        no.setOnClickListener(v -> dialog.cancel());
    }


    NoResponse noResponse;


    public void deleteInvoice(String id) {

        Log.e("id",""+id);

        showSimpleProgressDialog(this, false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<NoResponse> call = apiInterface.deleteInvoice(id,
                preferenceHelper.getUserToken());


        call.enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(@NonNull Call<NoResponse> call, @NonNull Response<NoResponse> response) {
                removeSimpleProgressDialog();

                noResponse = response.body();
                Log.e("noResponse",""+ response.body());

                finish();

            }

            @Override
            public void onFailure(@NonNull Call<NoResponse> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });


    }

}

