package codeztalk.elbasha.delegate.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.aPIS.requests.ReportRequest;
import codeztalk.elbasha.delegate.adapter.ReportAdapter;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ReportModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.getCurrentDate;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;

public class ReportActivity extends BaseActivity {

    private RecyclerView recyclerProducts;
    ReportAdapter hoursOfflineAdapter;
    ArrayList<ReportModel> hoursOfflineArrayList;

    TextView textDateFrom;
    TextView textDateTo;
    ImageView imageFilter;

    TextView textInvoicesQty;
    TextView textInvoicesAmount;
    TextView textInvoicesCashedAmount;
    TextView textInvoicesCreditAmount;
    TextView textCreditAmount;
    TextView textTotalCollectedAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        ImageView imageBack = findViewById(R.id.imageBack);
        TextView textToolbar = findViewById(R.id.textToolbar);
        textToolbar.setText(getString(R.string.report1));

        imageBack.setOnClickListener(v -> onBackPressed());

        recyclerProducts = findViewById(R.id.recyclerProducts);
        textDateFrom = findViewById(R.id.textDateFrom);
        textDateTo = findViewById(R.id.textDateTo);
        imageFilter = findViewById(R.id.imageFilter);

        textInvoicesQty = findViewById(R.id.textInvoicesQty);
        textInvoicesAmount = findViewById(R.id.textInvoicesAmount);
        textInvoicesCashedAmount = findViewById(R.id.textInvoicesCashedAmount);
        textInvoicesCreditAmount = findViewById(R.id.textInvoicesCreditAmount);
        textCreditAmount = findViewById(R.id.textCreditAmount);
        textTotalCollectedAmount = findViewById(R.id.textTotalCollectedAmount);



        textDateTo.setText(getCurrentDate());
        textDateFrom.setText(MyHelpers.getPreviousMonthAR(getCurrentDate()));


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
                Toast.makeText(this, getString(R.string.dateBeforenow), Toast.LENGTH_SHORT).show();

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
                Toast.makeText(this, getString(R.string.dateafternow), Toast.LENGTH_SHORT).show();

            } else {

                Locale locale = new Locale("ar");
                String myFormat = "dd-MMMM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, locale);
                textDateTo.setText(sdf.format(myCalendarTo.getTime()));

            }


        };

        textDateFrom.setOnClickListener(view -> new DatePickerDialog(ReportActivity.this,
                onDateSetListenerFrom,
                myCalendarFrom.get(Calendar.YEAR),
                myCalendarFrom.get(Calendar.MONTH),
                myCalendarFrom.get(Calendar.DAY_OF_MONTH)).show());


        textDateTo.setOnClickListener(v -> {

            if (!textDateFrom.getText().toString().equalsIgnoreCase("")) {
                new DatePickerDialog(ReportActivity.this,
                        onDateSetListenerTo,
                        myCalendarTo.get(Calendar.YEAR),
                        myCalendarTo.get(Calendar.MONTH),
                        myCalendarTo.get(Calendar.DAY_OF_MONTH)).show();
            } else {
                Toast.makeText(this, getString(R.string.dateFrom), Toast.LENGTH_SHORT).show();


            }

        });



        imageFilter.setOnClickListener(v -> getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString()),
                MyHelpers.formatSendingDate(textDateTo.getText().toString())));

        initializeHoursOffline();
    }


    public void initializeHoursOffline() {
        //initialize views
        hoursOfflineArrayList = new ArrayList<>();

        recyclerProducts.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerProducts.setLayoutManager(mLayoutManager);
        hoursOfflineAdapter = new ReportAdapter(hoursOfflineArrayList, this);

        recyclerProducts.setAdapter(hoursOfflineAdapter);


        getEmployeeReport(MyHelpers.formatSendingDate(textDateFrom.getText().toString()),
                MyHelpers.formatSendingDate(textDateTo.getText().toString()));

    }

    private void getEmployeeReport(String startDate, String endDate) {



        showSimpleProgressDialog(this, false);

        Log.e("startDate", "" + startDate);
        Log.e("endDate", "" + endDate);

        hoursOfflineArrayList.clear();

        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        ReportRequest reportRequest = new ReportRequest();

        reportRequest.setEmpId(preferenceHelper.getUserId());
        reportRequest.setStartDate(startDate);
        reportRequest.setEndDate(endDate);


        Call<List<ReportModel>> call = apiInterface.getEmployeeReport(
                preferenceHelper.getUserToken(),
                reportRequest);


        call.enqueue(new Callback<List<ReportModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ReportModel>> call, @NonNull Response<List<ReportModel>> response) {
                removeSimpleProgressDialog();

                Log.e("address : ", "  ## :  ## " + new Gson().toJson(response.body()));
                Log.e("response : ", "  ## :  ## " + response);

                if (response.body() != null) {

                     hoursOfflineArrayList.addAll(response.body());
                    hoursOfflineAdapter.notifyDataSetChanged();

                    double invoicesQty = 0;
                    double invoicesAmount = 0;
                    double invoicesCashedAmount = 0;
                    double invoicesCreditAmount = 0;
                    double creditAmount = 0;
                    double totalCollectedAmount = 0;

                    for (int i = 0; i < hoursOfflineArrayList.size(); i++) {

                        invoicesQty += hoursOfflineArrayList.get(i).getInvoicesQty();
                        invoicesAmount += hoursOfflineArrayList.get(i).getInvoicesAmount();
                        invoicesCashedAmount += hoursOfflineArrayList.get(i).getInvoicesCashedAmount();
                        invoicesCreditAmount += hoursOfflineArrayList.get(i).getInvoicesCreditAmount();
                        creditAmount += hoursOfflineArrayList.get(i).getCreditAmount();
                        totalCollectedAmount += hoursOfflineArrayList.get(i).getTotalCollectedAmount();
                    }

                    textInvoicesQty.setText(String.valueOf(invoicesQty));
                    textInvoicesAmount.setText(String.valueOf(invoicesAmount));
                    textInvoicesCashedAmount.setText(String.valueOf(invoicesCashedAmount));
                    textInvoicesCreditAmount.setText(String.valueOf(invoicesCreditAmount));
                    textCreditAmount.setText(String.valueOf(creditAmount));
                    textTotalCollectedAmount.setText(String.valueOf(totalCollectedAmount));





                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ReportModel>> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();

            }
        });
    }


}
