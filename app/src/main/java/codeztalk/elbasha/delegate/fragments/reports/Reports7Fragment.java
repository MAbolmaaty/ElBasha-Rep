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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.Report1Request;
import codeztalk.elbasha.delegate.adapter.ClientFilterAdapter;
import codeztalk.elbasha.delegate.adapter.reportsAdapter.Report7Adapter;
import codeztalk.elbasha.delegate.db.ForsahDB;
import codeztalk.elbasha.delegate.fragments.BaseFragment;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.Report7Model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.getCurrentDate;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class Reports7Fragment extends BaseFragment {

    private RecyclerView recyclerReport;
    Report7Adapter hoursOfflineAdapter;
    ArrayList<Report7Model> hoursOfflineArrayList;

    TextView textDateFrom;
    TextView textDateTo;
    ImageView imageFilter;


    TextView textInvoicesNumber;
    TextView textTotal1;
    TextView textTotal2;



     public String clientId = "0";

     public TextView textClientName;

    public static Reports7Fragment newInstance() {
        return new Reports7Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_report_7);

        db = new ForsahDB(getActivity());
         clientArrayList = new ArrayList<>();

         clientArrayList.add(new ClientModel( -1,0,"الكل"));

        recyclerReport = mView.findViewById(R.id.recyclerReport);
        textDateFrom = mView.findViewById(R.id.textDateFrom);
        textDateTo = mView.findViewById(R.id.textDateTo);
        imageFilter = mView.findViewById(R.id.imageFilter);

        textDateTo.setText(getCurrentDate());
        textDateFrom.setText(MyHelpers.getPreviousMonthAR(getCurrentDate()));

        textInvoicesNumber = mView.findViewById(R.id.textInvoicesNumber);
        textTotal1 = mView.findViewById(R.id.textTotal1);
        textTotal2 = mView.findViewById(R.id.textTotal2);


         textClientName = mView.findViewById(R.id.textClientName);


        final Calendar myCalendarFrom = Calendar.getInstance();
        final Calendar myCalendarTo = Calendar.getInstance();


        final DatePickerDialog.OnDateSetListener onDateSetListenerFrom = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendarFrom.set(Calendar.YEAR, year);
            myCalendarFrom.set(Calendar.MONTH, monthOfYear);
            myCalendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);


            Date dateFrom = myCalendarTo.getTime();
            Date dateSpecified = myCalendarFrom.getTime();

            Log.e("dateSpecified ", ": " + dateFrom);

            if (dateSpecified.after(dateFrom)) {
                Toast.makeText(getActivity(), getString(R.string.dateBeforenow), Toast.LENGTH_SHORT).show();

            } else {
                Locale locale = new Locale("ar");
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);
                textDateFrom.setText(sdf.format(myCalendarFrom.getTime()));

            }


        };

        final DatePickerDialog.OnDateSetListener onDateSetListenerTo = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendarTo.set(Calendar.YEAR, year);
            myCalendarTo.set(Calendar.MONTH, monthOfYear);
            myCalendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);


            Date dateFrom = myCalendarFrom.getTime();
            Date dateSpecified = myCalendarTo.getTime();

            Log.e("dateSpecified ", ": " + dateFrom);

            if (dateSpecified.before(dateFrom)) {
                Toast.makeText(getActivity(), getString(R.string.dateafternow), Toast.LENGTH_SHORT).show();

            } else {
                Locale locale = new Locale("ar");
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);
                textDateTo.setText(sdf.format(myCalendarTo.getTime()));

            }


        };

        textDateFrom.setOnClickListener(view -> new DatePickerDialog(Objects.requireNonNull(getActivity()),
                onDateSetListenerFrom,
                myCalendarFrom.get(Calendar.YEAR),
                myCalendarFrom.get(Calendar.MONTH),
                myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show());


        textDateTo.setOnClickListener(v -> {

            if (!textDateFrom.getText().toString().equalsIgnoreCase("")) {
                new DatePickerDialog(Objects.requireNonNull(getActivity()),
                        onDateSetListenerTo,
                        myCalendarTo.get(Calendar.YEAR),
                        myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            } else {
                Toast.makeText(getActivity(), getString(R.string.dateFrom), Toast.LENGTH_SHORT).show();


            }

        });

        imageFilter.setOnClickListener(v -> getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString()),
                MyHelpers.formatSendingDate(textDateTo.getText().toString())));

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
        hoursOfflineAdapter = new Report7Adapter(hoursOfflineArrayList, getActivity());

        recyclerReport.setAdapter(hoursOfflineAdapter);

        getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString()),
                MyHelpers.formatSendingDate(textDateTo.getText().toString()));

    }

    private void getEmployeeReport(String startDate, String endDate) {


        showSimpleProgressDialog(getActivity(), false);

        Log.e("startDate", "" + startDate);
        Log.e("endDate", "" + endDate);

        hoursOfflineArrayList.clear();

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Report1Request Report1Request = new Report1Request();

        Report1Request.setEmpId(preferenceHelper.getUserId());
        Report1Request.setClientId(clientId);
        Report1Request.setStartDate(startDate);
        Report1Request.setEndDate(endDate);


        Call<List<Report7Model>> call = apiInterface.getReport7(
                preferenceHelper.getUserToken(),
                Report1Request);


        call.enqueue(new Callback<List<Report7Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Report7Model>> call, @NonNull Response<List<Report7Model>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response);

                if (response.body() != null) {

                    hoursOfflineArrayList.addAll(response.body());
                    hoursOfflineAdapter.notifyDataSetChanged();

                    double total1 = 0;
                    double total2 = 0;


                    for (int i = 0; i < hoursOfflineArrayList.size(); i++)
                    {

                        total1 += hoursOfflineArrayList.get(i).getPaidValue();

                        if(hoursOfflineArrayList.get(i).getReminder() != null)
                        {
                            total2 += hoursOfflineArrayList.get(i).getReminder();

                        }
                    }



                    textTotal1.setText(new DecimalFormat("##.##").format(total1));
                    textTotal2.setText(new DecimalFormat("##.##").format(total2));


                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Report7Model>> call, @NonNull Throwable t) {
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

        clientDialog.setCancelable(true);
        clientDialog.setContentView(R.layout.alert_clients);
        clientDialog.show();

        RecyclerView recyclerAll = clientDialog.findViewById(R.id.recyclerAll);
        recyclerAll.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManagerCountries = new LinearLayoutManager(getActivity());
        recyclerAll.setLayoutManager(layoutManagerCountries);
        ClientFilterAdapter countriesAdapter = new ClientFilterAdapter(clientArrayList, Reports7Fragment.this, "report7");

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


