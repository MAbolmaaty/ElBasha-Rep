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
import codeztalk.elbasha.delegate.models.Report8Model;

public class Report8Adapter extends RecyclerView.Adapter<Report8Adapter.DataObjectHolder> {
    private ArrayList<Report8Model> mDataSet;
    Context context;


    public Report8Adapter(ArrayList<Report8Model> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textInvoiceTaxNumber;
        TextView textEmployeeName;
        TextView textClientName;
        TextView textFinalValue;
        TextView textPaidValue;
        TextView textRemainValue;
        TextView textActionDate;


        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textInvoiceTaxNumber = view.findViewById(R.id.textInvoiceTaxNumber);
            this.textEmployeeName = view.findViewById(R.id.textEmployeeName);
            this.textClientName = view.findViewById(R.id.textClientName);
            this.textFinalValue = view.findViewById(R.id.textFinalValue);
            this.textPaidValue = view.findViewById(R.id.textPaidValue);
            this.textRemainValue = view.findViewById(R.id.textRemainValue);
            this.textActionDate = view.findViewById(R.id.textActionDate);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @Override
    public Report8Adapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report8, parent, false);
        context = parent.getContext();

        Report8Adapter.DataObjectHolder dataObjectHolder = new Report8Adapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final Report8Adapter.DataObjectHolder holder, int position) {

        final Report8Model hoursOfflineModel = mDataSet.get(position);

        String actionDate = MyHelpers.formatDateFromOneToAnother(hoursOfflineModel.getActionDate(), "yyyy-MM-dd'T'hh:mm:ss", "MMM-dd");

        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textInvoiceTaxNumber.setText(String.valueOf(hoursOfflineModel.getInvoiceTaxNumber()));
        holder.textEmployeeName.setText(String.valueOf(hoursOfflineModel.getEmpName()));
        holder.textClientName.setText(String.valueOf(hoursOfflineModel.getClientName()));
        holder.textActionDate.setText(actionDate);
        holder.textFinalValue.setText(String.valueOf(hoursOfflineModel.getFinalValue()));
        holder.textPaidValue.setText(String.valueOf(hoursOfflineModel.getPaidValue()));
        holder.textRemainValue.setText(String.valueOf(hoursOfflineModel.getRemainValue()));

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




