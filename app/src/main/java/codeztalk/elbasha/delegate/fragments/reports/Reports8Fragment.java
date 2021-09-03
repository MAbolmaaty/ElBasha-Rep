package codeztalk.elbasha.delegate.fragments.reports;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.Report1Request;
import codeztalk.elbasha.delegate.adapter.ClientFilterAdapter;
import codeztalk.elbasha.delegate.adapter.reportsAdapter.Report8Adapter;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.fragments.BaseFragment;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.Report8Model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.getCurrentDate;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class Reports8Fragment extends BaseFragment {

    private RecyclerView recyclerReport;
    Report8Adapter hoursOfflineAdapter;
    ArrayList<Report8Model> hoursOfflineArrayList;

    TextView textDateFrom;
    ImageView imageFilter;


    TextView textInvoicesNumber;
    TextView textTotal1;
    TextView textTotal2;
    TextView textTotal3;


     public String clientId = "0";

     public TextView textClientName;

    public static Reports8Fragment newInstance() {
        return new Reports8Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_report_8);

        db = new ForsahDB(getActivity());
         clientArrayList = new ArrayList<>();

         clientArrayList.add(new ClientModel(-1,0, "الكل"));

        recyclerReport = mView.findViewById(R.id.recyclerReport);
        textDateFrom = mView.findViewById(R.id.textDateFrom);
        imageFilter = mView.findViewById(R.id.imageFilter);

        textDateFrom.setText(MyHelpers.getPreviousMonthAR(getCurrentDate()));

        textInvoicesNumber = mView.findViewById(R.id.textInvoicesNumber);
        textTotal1 = mView.findViewById(R.id.textTotal1);
        textTotal2 = mView.findViewById(R.id.textTotal2);
        textTotal3 = mView.findViewById(R.id.textTotal3);


         textClientName = mView.findViewById(R.id.textClientName);


        final Calendar myCalendarFrom = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener onDateSetListenerFrom = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendarFrom.set(Calendar.YEAR, year);
            myCalendarFrom.set(Calendar.MONTH, monthOfYear);
            myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            Locale locale = new Locale("ar");
            String myFormat = "dd-MMMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);
            textDateFrom.setText(sdf.format(myCalendarFrom.getTime()));

        };


        textDateFrom.setOnClickListener(view -> new DatePickerDialog(Objects.requireNonNull(getActivity()),
                onDateSetListenerFrom,
                myCalendarFrom.get(Calendar.YEAR),
                myCalendarFrom.get(Calendar.MONTH),
                myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show());


        imageFilter.setOnClickListener(v -> getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString())));

        initializeHoursOffline();


        if (db.getClientCount() > 0) {
            getClientsOffline();
        } else {
            getClients();
        }

         textClientName.setOnClickListener(view -> ShowClientDialog());

        return mView;
    }


    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();

        recyclerReport.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerReport.setLayoutManager(mLayoutManager);
        hoursOfflineAdapter = new Report8Adapter(hoursOfflineArrayList, getActivity());

        recyclerReport.setAdapter(hoursOfflineAdapter);

         getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString()));
    }

    private void getEmployeeReport(String startDate) {


        showSimpleProgressDialog(getActivity(), false);

        Log.e("startDate", "" + startDate);

        hoursOfflineArrayList.clear();

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Report1Request Report1Request = new Report1Request();

        Report1Request.setEmpId(preferenceHelper.getUserId());
        Report1Request.setClientId(clientId);
        Report1Request.setStartDate(startDate);


        Call<List<Report8Model>> call = apiInterface.getReport8(
                preferenceHelper.getUserToken(),
                Report1Request);


        call.enqueue(new Callback<List<Report8Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Report8Model>> call, @NonNull Response<List<Report8Model>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response);

                if (response.body() != null) {

                    hoursOfflineArrayList.addAll(response.body());
                    hoursOfflineAdapter.notifyDataSetChanged();

                    double total1 = 0;
                    double total2 = 0;
                    double total3 = 0;


                    for (int i = 0; i < hoursOfflineArrayList.size(); i++) {

                        total1 += hoursOfflineArrayList.get(i).getFinalValue();
                        total2 += hoursOfflineArrayList.get(i).getPaidValue();
                        total3 += hoursOfflineArrayList.get(i).getRemainValue();

                    }



                    textTotal1.setText(new DecimalFormat("##.##").format(total1));
                    textTotal2.setText(new DecimalFormat("##.##").format(total2));
                    textTotal3.setText(new DecimalFormat("##.##").format(total3));

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Report8Model>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


    ForsahDB db;



    private void getClientsOffline() {
        clientArrayList.addAll(db.getAllClients());

    }


    public Dialog clientDialog;
    public ArrayList<ClientModel> clientArrayList;

    private void ShowClientDialog() {
        clientDialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(getActivity(), clientDialog);
        MyContextWrapper.forceRTLIfSupported(getActivity(), clientDialog);

        clientDialog.setCancelable(true);
        clientDialog.setContentView(R.layout.alert_clients);
        clientDialog.show();

        RecyclerView recyclerAll = clientDialog.findViewById(R.id.recyclerAll);
        recyclerAll.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerCountries = new LinearLayoutManager(getActivity());
        recyclerAll.setLayoutManager(layoutManagerCountries);
        ClientFilterAdapter countriesAdapter = new ClientFilterAdapter(clientArrayList, Reports8Fragment.this, "report8");

        recyclerAll.setAdapter(countriesAdapter);
        countriesAdapter.notifyDataSetChanged();

        EditText editSearch = clientDialog.findViewById(R.id.editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                countriesAdapter.getFilter().filter(s);


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void getClients() {


        showSimpleProgressDialog(getActivity(), false);

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<List<ClientModel>> call = apiInterface.getClients(
                preferenceHelper.getUserId(),
                preferenceHelper.getUserToken());

        call.enqueue(new Callback<List<ClientModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ClientModel>> call, @NonNull Response<List<ClientModel>> response) {
                removeSimpleProgressDialog();


                if (response.body() != null) {

                    db.addToClient(response.body());
                    getClientsOffline();
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


}


