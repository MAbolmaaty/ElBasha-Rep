package codeztalk.elbasha.delegate.fragments.reports;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import codeztalk.elbasha.delegate.aPIS.requests.ReportRequest;
import codeztalk.elbasha.delegate.adapter.reportsAdapter.Report10Adapter;
import codeztalk.elbasha.delegate.fragments.BaseFragment;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.Report10Model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.getCurrentDate;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class Reports10Fragment extends BaseFragment {

    private RecyclerView recyclerReport;
    Report10Adapter hoursOfflineAdapter;
    ArrayList<Report10Model> hoursOfflineArrayList;

    TextView textDateFrom;
    TextView textDateTo;
    ImageView imageFilter;



    TextView textInvoicesNumber;
    TextView textTotal1;
    TextView textTotal2;
    TextView textTotal3;

    public static Reports10Fragment newInstance() {
        return new Reports10Fragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_report_10);



        recyclerReport = mView.findViewById(R.id.recyclerReport);
        textDateFrom = mView.findViewById(R.id.textDateFrom);
        textDateTo = mView.findViewById(R.id.textDateTo);
        imageFilter = mView.findViewById(R.id.imageFilter);

        textDateTo.setText(getCurrentDate());
        textDateFrom.setText(MyHelpers.getPreviousMonthAR(getCurrentDate()));

        textInvoicesNumber = mView.findViewById(R.id.textInvoicesNumber);
        textTotal1 = mView.findViewById(R.id.textTotal1);
        textTotal2 = mView.findViewById(R.id.textTotal2);
        textTotal3 = mView.findViewById(R.id.textTotal3);



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





        return mView;
    }


    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();

        recyclerReport.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerReport.setLayoutManager(mLayoutManager);
        hoursOfflineAdapter = new Report10Adapter(hoursOfflineArrayList, getActivity());

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

        ReportRequest report1Request = new ReportRequest();

        report1Request.setEmpId(preferenceHelper.getUserId());
        report1Request.setStartDate(startDate);
        report1Request.setEndDate(endDate);


        Call<List<Report10Model>> call = apiInterface.getReport10(
                preferenceHelper.getUserToken(),
                report1Request);


        call.enqueue(new Callback<List<Report10Model>>() {
            @Override
            public void onResponse(@NonNull Call<List<Report10Model>> call, @NonNull Response<List<Report10Model>> response) {
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

                        total1 += hoursOfflineArrayList.get(i).getAmount();
                        total2 += hoursOfflineArrayList.get(i).getPreviousBalance();
                        total3 += hoursOfflineArrayList.get(i).getCurrentBalance();
                    }

//                    textInvoicesNumber.setText(String.format("%s\t%d", getString(R.string.InvoicesQty), hoursOfflineArrayList.size()));


                    textTotal1.setText(new DecimalFormat("##.##").format(total1));
                    textTotal2.setText(new DecimalFormat("##.##").format(total2));
                    textTotal3.setText(new DecimalFormat("##.##").format(total3));

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Report10Model>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }

















}


