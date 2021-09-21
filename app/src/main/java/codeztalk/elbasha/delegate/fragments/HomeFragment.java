package codeztalk.elbasha.delegate.fragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.AddLocationRequest;
import codeztalk.elbasha.delegate.aPIS.responses.NoResponse;
import codeztalk.elbasha.delegate.adapter.ClientSwipeAdapter;
import codeztalk.elbasha.delegate.adapter.DayFilterAdapter;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.models.DayModel;
import codeztalk.elbasha.delegate.view_models.PrinterViewModel;
import codeztalk.elbasha.delegate.views.MyRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;


public class HomeFragment extends BaseFragment {

    private  static final String TAG = HomeFragment.class.getSimpleName();

    private MyRecyclerView recyclerAll;
    public ClientSwipeAdapter clientSwipeAdapter;
    public ArrayList<ClientModel> clientArrayList;
    ImageView imageFilter;
    ImageView imageSearch;
    EditText editSearch;
    CharSequence search = "";
    private RecyclerView recyclerDays;

    private PrinterViewModel mPrinterViewModel;
    private ConnectedDevice mConnectedDevice;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_home);
        mPrinterViewModel = ViewModelProviders.of(getActivity()).get(PrinterViewModel.class);
        mPrinterViewModel.getPrinter().observe(this, new Observer<ConnectedDevice>() {
            @Override
            public void onChanged(ConnectedDevice connectedDevice) {
                mConnectedDevice = connectedDevice;
            }
        });
        Log.d(TAG, "Home Fragment Created");

        recyclerAll = mView.findViewById(R.id.recyclerAll);
        recyclerAll.setEmptyView(mView.findViewById(R.id.emptyView));
        recyclerAll.setEmptyImageView((mView.findViewById(R.id.emptyImage)));
        recyclerDays = mView.findViewById(R.id.recyclerDays);

        imageFilter = mView.findViewById(R.id.imageFilter);
        imageFilter.setVisibility(View.VISIBLE);
        imageSearch = mView.findViewById(R.id.imageSearch);
        imageFilter = mView.findViewById(R.id.imageFilter);
        editSearch = mView.findViewById(R.id.editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clientSwipeAdapter.getFilter().filter(s);
                search = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        Objects.requireNonNull(getActivity()).
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getClients();
        initializeDays();

        imageFilter.setOnClickListener(v -> {

            if(recyclerDays.getVisibility()==View.VISIBLE)
            {
                recyclerDays.setVisibility(View.GONE);

            }
            else
                {
                    recyclerDays.setVisibility(View.VISIBLE);

                }
        });

//        checkLocation();

//        addEmployeeLocation();

        return mView;
    }



    public void initializeRecycler(ArrayList<ClientModel> clientList) {
        //initialize views


        recyclerAll.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAll.setLayoutManager(mLayoutManager);

        clientSwipeAdapter = new ClientSwipeAdapter(clientList, getActivity(), mConnectedDevice);
        recyclerAll.setAdapter(clientSwipeAdapter);


    }


    private void getClients() {
        Log.e("getUserId", ">>>> "+preferenceHelper.getUserId());
        Log.e("getUserToken", ">><><><>< "+preferenceHelper.getUserToken());

        clientArrayList = new ArrayList<>();
        clientArrayList.clear();
        showSimpleProgressDialog(getActivity(), false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<ClientModel>> call = apiInterface.getClients(
                preferenceHelper.getUserId(),
                preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<ClientModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClientModel>> call, @NonNull Response<List<ClientModel>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response.body());

                if (response.body() != null) {

                    clientArrayList.addAll(response.body());


                    initializeRecycler(clientArrayList);

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ClientModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    ArrayList<ClientModel> dayClientArrayList;

    public void getDayClients(int day) {
        dayClientArrayList = new ArrayList<>();
        dayClientArrayList.clear();

        if (day == 0) {
            dayClientArrayList.addAll(clientArrayList);
        } else {
            for (ClientModel productInventory : clientArrayList) {
                if (productInventory.getClientDays().contains(day)) {
                    dayClientArrayList.add(productInventory);
                }


            }
        }


        initializeRecycler(dayClientArrayList);
        clientSwipeAdapter.notifyDataSetChanged();

        Log.e("size ", ">>> " + dayClientArrayList.size());

    }


    public void initializeDays() {
        //initialize views
        ArrayList<DayModel> dayModelArrayList = new ArrayList<>();
        dayModelArrayList.clear();

        dayModelArrayList.add(new DayModel(0, "all", "الكل", false));
        dayModelArrayList.add(new DayModel(1, "sa", "السبت", false));
        dayModelArrayList.add(new DayModel(2, "sa", "الأحد", false));
        dayModelArrayList.add(new DayModel(3, "sa", "الاثنين", false));
        dayModelArrayList.add(new DayModel(4, "sa", "الثلاثاء", false));
        dayModelArrayList.add(new DayModel(5, "sa", "الأربعاء", false));
        dayModelArrayList.add(new DayModel(6, "sa", "الخميس", false));
//        dayModelArrayList.add(new DayModel(7, "sa", "الجمعة", false));

        recyclerDays.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerDays.setLayoutManager(mLayoutManager);

        DayFilterAdapter dayAdapter =
                new DayFilterAdapter(dayModelArrayList, getActivity(), HomeFragment.this);
        recyclerDays.setAdapter(dayAdapter);
        dayAdapter.notifyDataSetChanged();
    }


    public void addEmployeeLocation() {


        long millisHour = 3600000;
        long currentTime = new Date().getTime();

        if(currentTime-preferenceHelper.getUpdateDate()>millisHour)
        {
            preferenceHelper.setUpdateDate(currentTime);


            AddLocationRequest addLocationRequest = new AddLocationRequest();

            addLocationRequest.setEmpId(preferenceHelper.getUserId());
            addLocationRequest.setGPSLocation(preferenceHelper.getUserLocation());

            ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

            Call<NoResponse> call = apiInterface.addEmployeeLocation(preferenceHelper.getUserToken(), addLocationRequest);


            call.enqueue(new Callback<NoResponse>() {
                @Override
                public void onResponse(@NonNull Call<NoResponse> call, @NonNull Response<NoResponse> response) {


                }

                @Override
                public void onFailure(@NonNull Call<NoResponse> call, @NonNull Throwable t) {
                    Log.e("fail", "is : " + call.toString());
                    t.printStackTrace();

                }
            });

        }



    }







}