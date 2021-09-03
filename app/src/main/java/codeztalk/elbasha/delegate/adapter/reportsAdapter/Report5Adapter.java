package codeztalk.elbasha.delegate.adapter.reportsAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.models.Report5Model;

public class Report5Adapter extends RecyclerView.Adapter<Report5Adapter.DataObjectHolder> {
    private ArrayList<Report5Model> mDataSet;
    Context context;


    public Report5Adapter(ArrayList<Report5Model> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textInvoiceTaxNumber;
        TextView textProductName;
        TextView textEmployeeName;
        TextView textClientName;
        TextView textUnitQTY;
        TextView textUnitPrice;
        TextView textOfficialUnitPrice;
        TextView textBalance;


        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textInvoiceTaxNumber = view.findViewById(R.id.textInvoiceTaxNumber);
            this.textProductName = view.findViewById(R.id.textProductName);
            this.textEmployeeName = view.findViewById(R.id.textEmployeeName);
            this.textClientName = view.findViewById(R.id.textClientName);
            this.textUnitQTY = view.findViewById(R.id.textUnitQTY);
            this.textUnitPrice = view.findViewById(R.id.textUnitPrice);
            this.textOfficialUnitPrice = view.findViewById(R.id.textOfficialUnitPrice);
            this.textBalance = view.findViewById(R.id.textBalance);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @Override
    public Report5Adapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report5, parent, false);
        context = parent.getContext();

        Report5Adapter.DataObjectHolder dataObjectHolder = new Report5Adapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Report5Adapter.DataObjectHolder holder, int position) {

        final Report5Model hoursOfflineModel = mDataSet.get(position);

        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textInvoiceTaxNumber.setText(String.valueOf(hoursOfflineModel.getInvoiceTaxNumber()));
//        holder.textProductName.setText(String.format("%s\n%s", hoursOfflineModel.getProductNameAR(), hoursOfflineModel.getProductNameEN()));
        holder.textProductName.setText(hoursOfflineModel.getProductNameAR());
        holder.textEmployeeName.setText(String.valueOf(hoursOfflineModel.getEmpName()));
        holder.textClientName.setText(String.valueOf(hoursOfflineModel.getClientName()));
        holder.textUnitQTY.setText(String.valueOf(hoursOfflineModel.getUnitQTY()));
        holder.textUnitPrice.setText(String.valueOf(hoursOfflineModel.getUnitPrice()));
        holder.textOfficialUnitPrice.setText(String.valueOf(hoursOfflineModel.getOfficialUnitPrice()));
        holder.textBalance.setText(String.valueOf(hoursOfflineModel.getBalance()));

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




