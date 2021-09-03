package codeztalk.elbasha.delegate.adapter.reportsAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.Report7Model;

public class Report7Adapter extends RecyclerView.Adapter<Report7Adapter.DataObjectHolder> {
    private ArrayList<Report7Model> mDataSet;
    Context context;


    public Report7Adapter(ArrayList<Report7Model> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textInvoiceTaxNumber;
         TextView textEmployeeName;
        TextView textClientName;
        TextView textIssuedDate;
        TextView textPaidValue;
        TextView textReminder;


        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textInvoiceTaxNumber = view.findViewById(R.id.textInvoiceTaxNumber);
             this.textEmployeeName = view.findViewById(R.id.textEmployeeName);
            this.textClientName = view.findViewById(R.id.textClientName);
            this.textIssuedDate = view.findViewById(R.id.textIssuedDate);
            this.textPaidValue = view.findViewById(R.id.textPaidValue);
            this.textReminder = view.findViewById(R.id.textReminder);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @Override
    public Report7Adapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report7, parent, false);
        context = parent.getContext();

        Report7Adapter.DataObjectHolder dataObjectHolder = new Report7Adapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final Report7Adapter.DataObjectHolder holder, int position) {

        final Report7Model hoursOfflineModel = mDataSet.get(position);

        String actionDate = MyHelpers.formatDateFromOneToAnother(hoursOfflineModel.getIssuedDate(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MMM-dd");

        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textInvoiceTaxNumber.setText(String.valueOf(hoursOfflineModel.getInvoiceId()));
         holder.textEmployeeName.setText(String.valueOf(hoursOfflineModel.getEmpName()));
        holder.textClientName.setText(String.valueOf(hoursOfflineModel.getClientName()));
        holder.textIssuedDate.setText(actionDate);
        holder.textPaidValue.setText(String.valueOf(hoursOfflineModel.getPaidValue()));
        holder.textReminder.setText(String.valueOf(hoursOfflineModel.getReminder()));

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}




