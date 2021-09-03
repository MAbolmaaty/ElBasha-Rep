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
import codeztalk.elbasha.delegate.models.Report6Model;

public class Report6Adapter extends RecyclerView.Adapter<Report6Adapter.DataObjectHolder> {
    private ArrayList<Report6Model> mDataSet;
    Context context;


    public Report6Adapter(ArrayList<Report6Model> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textInvoiceTaxNumber;
        TextView textEmployeeName;
        TextView textClientName;
        TextView textUnPaidValue;
        TextView textActionDate;


        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textInvoiceTaxNumber = view.findViewById(R.id.textInvoiceTaxNumber);
            this.textEmployeeName = view.findViewById(R.id.textEmployeeName);
            this.textClientName = view.findViewById(R.id.textClientName);
            this.textUnPaidValue = view.findViewById(R.id.textUnPaidValue);
            this.textActionDate = view.findViewById(R.id.textActionDate);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @Override
    public Report6Adapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report6, parent, false);
        context = parent.getContext();

        Report6Adapter.DataObjectHolder dataObjectHolder = new Report6Adapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final Report6Adapter.DataObjectHolder holder, int position) {

        final Report6Model hoursOfflineModel = mDataSet.get(position);

        String actionDate = MyHelpers.formatDateFromOneToAnother(hoursOfflineModel.getActionDate(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MMM-dd");


        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.textInvoiceTaxNumber.setText(String.valueOf(hoursOfflineModel.getInvoiceTaxNumber()));
        holder.textEmployeeName.setText(String.valueOf(hoursOfflineModel.getEmpName()));
        holder.textClientName.setText(String.valueOf(hoursOfflineModel.getClientName()));
        holder.textUnPaidValue.setText(String.valueOf(hoursOfflineModel.getFinalValue() - hoursOfflineModel.getPaidValue()));
        holder.textActionDate.setText(actionDate);


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





