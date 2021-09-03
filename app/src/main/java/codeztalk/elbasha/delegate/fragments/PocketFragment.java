package codeztalk.elbasha.delegate.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.responses.NoResponse;
import codeztalk.elbasha.delegate.adapter.PocketAdapter;
import codeztalk.elbasha.delegate.models.PocketModel;
import codeztalk.elbasha.delegate.views.MyRecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class PocketFragment extends BaseFragment {

    private MyRecyclerView recyclerAll;
    PocketAdapter hoursOfflineAdapter;
    ArrayList<PocketModel> hoursOfflineArrayList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.layout_recycler);

        recyclerAll = mView.findViewById(R.id.recyclerAll);
        recyclerAll.setEmptyView(mView.findViewById(R.id.emptyView));
        recyclerAll.setEmptyImageView((mView.findViewById(R.id.emptyImage)));


        initializeHoursOffline();

        return mView;
    }


    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();
        hoursOfflineArrayList.clear();


        recyclerAll.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerAll.setLayoutManager(mLayoutManager);

        hoursOfflineAdapter = new PocketAdapter(hoursOfflineArrayList, PocketFragment.this);
        recyclerAll.setAdapter(hoursOfflineAdapter);
        hoursOfflineAdapter.notifyDataSetChanged();

        getEmployeePocket();
    }


    private void getEmployeePocket() {
        Log.e("on", "on");

        showSimpleProgressDialog(getActivity(), false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<PocketModel>> call = apiInterface.getEmployeePocket(preferenceHelper.getUserId(), preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<PocketModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<PocketModel>> call, @NonNull Response<List<PocketModel>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response.body());

                if (response.body() != null) {

                    hoursOfflineArrayList.addAll(response.body());
                    hoursOfflineAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<PocketModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    NoResponse noResponse;

    public void deletePocket(String id) {

       Log.e("id",""+id);

        showSimpleProgressDialog(getActivity(), false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<NoResponse> call = apiInterface.deleteEmployeePocket(id,
                preferenceHelper.getUserToken());


        call.enqueue(new Callback<NoResponse>() {
            @Override
            public void onResponse(@NonNull Call<NoResponse> call, @NonNull Response<NoResponse> response) {
                removeSimpleProgressDialog();

                noResponse = response.body();




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