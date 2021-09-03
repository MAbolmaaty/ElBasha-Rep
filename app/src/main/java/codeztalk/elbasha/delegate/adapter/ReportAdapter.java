package codeztalk.elbasha.delegate.adapter;

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
import codeztalk.elbasha.delegate.models.ReportModel;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.DataObjectHolder> {
    private ArrayList<ReportModel> mDataSet;
    Context context;


    public ReportAdapter(ArrayList<ReportModel> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textInvoicesQty;
        TextView textInvoicesAmount;
        TextView textInvoicesCashedAmount;
        TextView textInvoicesCreditAmount;
        TextView textCreditAmount;
        TextView textTotalCollectedAmount;

        TextView textDate;

        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textInvoicesQty = view.findViewById(R.id.textInvoicesQty);
            this.textInvoicesAmount = view.findViewById(R.id.textInvoicesAmount);
            this.textInvoicesCashedAmount = view.findViewById(R.id.textInvoicesCashedAmount);
            this.textInvoicesCreditAmount = view.findViewById(R.id.textInvoicesCreditAmount);
            this.textCreditAmount = view.findViewById(R.id.textCreditAmount);
            this.textTotalCollectedAmount = view.findViewById(R.id.textTotalCollectedAmount);

            this.textDate = view.findViewById(R.id.textDate);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        final ReportModel hoursOfflineModel = mDataSet.get(position);

        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textInvoicesQty.setText(String.valueOf(hoursOfflineModel.getInvoicesQty()));
        holder.textInvoicesAmount.setText(String.valueOf(hoursOfflineModel.getInvoicesAmount()));
        holder.textInvoicesCashedAmount.setText(String.valueOf(hoursOfflineModel.getInvoicesCashedAmount()));
        holder.textInvoicesCreditAmount.setText(String.valueOf(hoursOfflineModel.getInvoicesCreditAmount()));
        holder.textCreditAmount.setText(String.valueOf(hoursOfflineModel.getCreditAmount()));
        holder.textTotalCollectedAmount.setText(String.valueOf(hoursOfflineModel.getTotalCollectedAmount()));

        holder.textDate.setText(hoursOfflineModel.getActionDate());







    }

    public void addItem(ReportModel dataObj) {
        mDataSet.add(dataObj);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mDataSet.remove(index);
        notifyItemRemoved(index);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void removeItem(int position) {
        mDataSet.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(ReportModel item, int position) {
        mDataSet.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}


