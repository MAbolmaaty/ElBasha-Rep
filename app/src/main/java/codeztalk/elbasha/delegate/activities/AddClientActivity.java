package codeztalk.elbasha.delegate.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.AddClientRequest;
import codeztalk.elbasha.delegate.aPIS.requests.UpdateClientRequest;
import codeztalk.elbasha.delegate.aPIS.responses.AddClientResponse;
import codeztalk.elbasha.delegate.aPIS.responses.NoResponse;
import codeztalk.elbasha.delegate.adapter.DayAdapter;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.DayModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.isEmpty;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class AddClientActivity extends BaseActivity {


    private static final String TAG = AddClientActivity.class.getSimpleName();

    private RecyclerView recyclerDays;

    private EditText editClientName;
    private EditText editClientPhone1;
    private EditText editClientPhone2;
    private EditText editClientResponsible;

    ArrayList<DayModel> dayModelArrayList;

    ClientModel clientModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);

        clientModel = (ClientModel) getIntent().getSerializableExtra("clientModel");


        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);

        recyclerDays = findViewById(R.id.recyclerDays);

        editClientName = findViewById(R.id.editClientName);
        editClientPhone1 = findViewById(R.id.editClientPhone1);
        editClientPhone2 = findViewById(R.id.editClientPhone2);
        editClientResponsible = findViewById(R.id.editClientResponsible);

        TextView textAdd = findViewById(R.id.textAdd);
        TextView textUpdate = findViewById(R.id.textUpdate);
        TextView textDelete = findViewById(R.id.textDelete);

        if (clientModel != null) {

            textAdd.setText(getString(R.string.updateClient));

            editClientName.setText(clientModel.getClientName());
            editClientPhone1.setText(clientModel.getMobile1());
            editClientPhone2.setText(clientModel.getMobile2());
            editClientResponsible.setText(clientModel.getDelgateMan());

            textToolbar.setText(getString(R.string.updateClient2));


        } else {
            textToolbar.setText(getString(R.string.addClient));

        }

        editClientPhone1.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        editClientPhone2.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);


        initializeHoursOffline();

        imageBack.setOnClickListener(v -> onBackPressed());

        textAdd.setOnClickListener(v -> {

            if (validateInputs()) {


                {
                    if (clientModel == null) {
                        addClient();

                    } else {
                        updateClient();

                    }
                }


            }

        });

//        textUpdate.setOnClickListener(v -> {
//
//
//            if (validateInputs()) {
//
//                if (!checkPermissions())
//                {
//                    requestPermissions();
//                } else {
//                    updateClient();
//                }
//
//            }
//
//        });

//        textDelete.setOnClickListener(v -> {
//
//
//            deleteClient();
//
//
//        });

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onBackPressed() {
        finish();
    }

    void launchHome() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void initializeHoursOffline() {
        //initialize views
        dayModelArrayList = new ArrayList<>();
        dayModelArrayList.clear();

        dayModelArrayList.add(new DayModel(1, "sa", "السبت", false));
        dayModelArrayList.add(new DayModel(2, "sa", "الأحد", false));
        dayModelArrayList.add(new DayModel(3, "sa", "الاثنين", false));
        dayModelArrayList.add(new DayModel(4, "sa", "الثلاثاء", false));
        dayModelArrayList.add(new DayModel(5, "sa", "الأربعاء", false));
        dayModelArrayList.add(new DayModel(6, "sa", "الخميس", false));

        recyclerDays.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerDays.setLayoutManager(mLayoutManager);

        DayAdapter dayAdapter = new DayAdapter(dayModelArrayList, AddClientActivity.this);
        recyclerDays.setAdapter(dayAdapter);
        dayAdapter.notifyDataSetChanged();

        if (clientModel != null) {
            for (int j = 0; j < dayModelArrayList.size(); j++) {

                for (int ii = 0; ii < clientModel.getClientDays().size(); ii++) {
                    if (clientModel.getClientDays().get(ii) == dayModelArrayList.get(j).getId()) {
                        dayModelArrayList.get(j).setSelected(true);

                    } else {
                        Log.e("yeeeeeeeeees", "  " + dayModelArrayList.get(j).getDayAR());
                    }

                }

            }
        }


    }


    AddClientResponse addClientResponse;

    void addClient() {
        List<AddClientRequest.ClientDay> clientDays = new ArrayList<>();

        for (int i = 0; i < this.dayModelArrayList.size(); i++) {
            if (dayModelArrayList.get(i).isSelected()) {
                clientDays.add(new AddClientRequest.ClientDay(dayModelArrayList.get(i).getId() + ""));

            }
        }

        if (clientDays.size() > 0) {
            AddClientRequest addClientRequest = new AddClientRequest();

            addClientRequest.setClientName(editClientName.getText().toString());
            addClientRequest.setMobile1(editClientPhone1.getText().toString());
            addClientRequest.setMobile2(editClientPhone2.getText().toString());
            addClientRequest.setGPSLocation(preferenceHelper.getUserLocation());
            addClientRequest.setDelgateMan(editClientResponsible.getText().toString());
            addClientRequest.setEmployeeId(preferenceHelper.getUserId());
            addClientRequest.setClientDays(clientDays);

            showSimpleProgressDialog(this, false);

            ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

            Call<AddClientResponse> call = apiInterface.addNewClient(preferenceHelper.getUserToken(), addClientRequest);


            call.enqueue(new Callback<AddClientResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddClientResponse> call, @NonNull Response<AddClientResponse> response) {
                    removeSimpleProgressDialog();

                    addClientResponse = response.body();

                    Log.e(TAG, "" + call.request().body());

                    if (addClientResponse != null) {
                        Log.e(TAG, "" + addClientResponse.getClientName());

                        launchHome();
                    }


                }

                @Override
                public void onFailure(@NonNull Call<AddClientResponse> call, @NonNull Throwable t) {
                    removeSimpleProgressDialog();
                    Log.e("fail", "is : " + call.toString());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.empty_days_error), Toast.LENGTH_SHORT).show();
        }

        Log.e("getUserLocation()"," >>>> "+preferenceHelper.getUserLocation());

    }


    void updateClient() {
        List<UpdateClientRequest.UpdateClientDay> clientDays = new ArrayList<>();

        for (int i = 0; i < this.dayModelArrayList.size(); i++) {
            if (dayModelArrayList.get(i).isSelected()) {
                clientDays.add(new UpdateClientRequest.UpdateClientDay(
                        dayModelArrayList.get(i).getId() + "",
                        clientModel.getId() + ""));

            }
        }

        if (clientDays.size() > 0) {
            UpdateClientRequest addClientRequest = new UpdateClientRequest();

            addClientRequest.setId(clientModel.getId()+"");
            addClientRequest.setClientName(editClientName.getText().toString());
            addClientRequest.setMobile1(editClientPhone1.getText().toString());
            addClientRequest.setMobile2(editClientPhone2.getText().toString());
            addClientRequest.setGPSLocation(preferenceHelper.getUserLocation());
            addClientRequest.setDelgateMan(editClientResponsible.getText().toString());
            addClientRequest.setEmployeeId(preferenceHelper.getUserId());
            addClientRequest.setmClientDays(clientDays);

            showSimpleProgressDialog(this, false);

            ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

            Call<AddClientResponse> call = apiInterface.updateClient(clientModel.getId() + "",
                    preferenceHelper.getUserToken(),
                    addClientRequest);


            call.enqueue(new Callback<AddClientResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddClientResponse> call, @NonNull Response<AddClientResponse> response) {
                    removeSimpleProgressDialog();

                    addClientResponse = response.body();

                    Log.e(TAG, "" + call.request().body());

                    if (addClientResponse != null) {
                        Log.e(TAG, "" + addClientResponse.getClientName());

                        launchHome();
                    }


                }

                @Override
                public void onFailure(@NonNull Call<AddClientResponse> call, @NonNull Throwable t) {
                    removeSimpleProgressDialog();
                    Log.e("fail", "is : " + call.toString());
                    t.printStackTrace();

                }
            });
        } else {
            Toast.makeText(this, getString(R.string.empty_days_error), Toast.LENGTH_SHORT).show();
        }


    }

    NoResponse noResponse;

    void deleteClient() {


        showSimpleProgressDialog(this, false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<NoResponse> call = apiInterface.deleteClient(clientModel.getId() + "",
                preferenceHelper.getUserToken());


        call.enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(@NonNull Call<NoResponse> call, @NonNull Response<NoResponse> response) {
                removeSimpleProgressDialog();

                noResponse = response.body();

                Log.e(TAG, "" + call.request().body());

                if (noResponse != null) {

                    launchHome();
                }


            }

            @Override
            public void onFailure(@NonNull Call<NoResponse> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });


    }


    private boolean validateInputs() {
        if (isEmpty(editClientName)) {
            editClientName.setError(AddClientActivity.this.getString(R.string.empty_client_error));
            return false;
        }

        if (isEmpty(editClientPhone1)) {
            editClientPhone1.setError(AddClientActivity.this.getString(R.string.empty_phone1_error));

            return false;
        }
        if (isEmpty(editClientPhone2)) {
            editClientPhone2.setError(AddClientActivity.this.getString(R.string.empty_phone2_error));

            return false;
        }

        if (isEmpty(editClientResponsible)) {
            editClientResponsible.setError(AddClientActivity.this.getString(R.string.empty_delegate_error));

            return false;
        }

        return true;
    }


}
