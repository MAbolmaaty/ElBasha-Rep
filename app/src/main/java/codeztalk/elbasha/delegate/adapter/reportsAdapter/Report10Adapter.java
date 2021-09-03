package codeztalk.elbasha.delegate.adapter.reportsAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.helper.MyContextWrapper;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.Report10Model;


public class Report10Adapter extends RecyclerView.Adapter<Report10Adapter.DataObjectHolder> {
    private ArrayList<Report10Model> mDataSet;
    Context context;


    public Report10Adapter(ArrayList<Report10Model> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textActionType;
        TextView textAmount;
        TextView textPreviousBalance;
        TextView textCurrentBalance;

        TextView textActionDate;


        LinearLayout linearReport;

        public DataObjectHolder(View view) {
            super(view);

            this.textActionType = view.findViewById(R.id.textActionType);
            this.textAmount = view.findViewById(R.id.textAmount);
            this.textPreviousBalance = view.findViewById(R.id.textPreviousBalance);
            this.textCurrentBalance = view.findViewById(R.id.textCurrentBalance);

            this.textActionDate = view.findViewById(R.id.textActionDate);


            this.linearReport = view.findViewById(R.id.linearReport);


        }

    }


    @NonNull
    @Override
    public Report10Adapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report10, parent, false);
        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Report10Adapter.DataObjectHolder holder, int position) {

        final Report10Model hoursOfflineModel = mDataSet.get(position);

        String actionDate = MyHelpers.formatDateFromOneToAnother(hoursOfflineModel.getActionDate(), "yyyy-MM-dd'T'hh:mm:ss", "MMM-dd");

        if (position % 2 == 0) {
            holder.linearReport.setBackgroundColor(Color.parseColor("#ffffff"));
        }


        holder.textActionType.setText(String.valueOf(hoursOfflineModel.getActiontype()));
        holder.textAmount.setText(String.valueOf(hoursOfflineModel.getAmount()));
        holder.textPreviousBalance.setText(String.valueOf(hoursOfflineModel.getPreviousBalance()));
        holder.textActionDate.setText(actionDate);
        holder.textCurrentBalance.setText(String.valueOf(hoursOfflineModel.getCurrentBalance()));


        holder.linearReport.setOnClickListener(view -> showLogOutDialog(hoursOfflineModel));
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }



    private void showLogOutDialog(Report10Model report10Model) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog);
        MyContextWrapper.forceRTLIfSupported(context, dialog);

        dialog.setCancelable(true);
        dialog.setContentView(R.layout.alert_details);
        dialog.show();

        ImageView image_exit = dialog.findViewById(R.id.image_exit);
        TextView alert_message = dialog.findViewById(R.id.alert_message);

        alert_message.setText(String.format("%s\t\t%s\n\n%s\t\t%s",
                context.getString(R.string.action_type),
                report10Model.getActiontype(),
                context.getString(R.string.notes),
                report10Model.getNotes()));

        image_exit.setOnClickListener(v -> {


            dialog.cancel();


        });

    }

}




