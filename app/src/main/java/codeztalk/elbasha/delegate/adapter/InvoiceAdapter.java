package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.ClientProfileActivity;
import codeztalk.elbasha.delegate.activities.printer.TaskPrintActivity;
import codeztalk.elbasha.delegate.fragments.CreditInvoicesFragment;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.ClientInvoiceModel;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.DataObjectHolder> implements Filterable {
    private List<ClientInvoiceModel> mDataSet;
    private List<ClientInvoiceModel> mDataSetFiltered;

    Context context;

    ClientProfileActivity clientProfileActivity;
    CreditInvoicesFragment creditInvoicesFragment;
    String type;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataSetFiltered = mDataSet;
                } else {
                    List<ClientInvoiceModel> filteredList = new ArrayList<>();
                    for (ClientInvoiceModel row : mDataSet) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getClientName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mDataSetFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataSetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataSetFiltered = (List<ClientInvoiceModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public InvoiceAdapter(ArrayList<ClientInvoiceModel> mDataSet, ClientProfileActivity clientProfileActivity, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.clientProfileActivity = clientProfileActivity;
        this.type = type;

    }


    public InvoiceAdapter(ArrayList<ClientInvoiceModel> mDataSet, CreditInvoicesFragment creditInvoicesFragment, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;

        this.creditInvoicesFragment = creditInvoicesFragment;
        this.type = type;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        RelativeLayout relLayout;
        TextView textInvoiceNumber;
        TextView textInvoiceDate;
        TextView textInvoiceNotes;
        TextView textInvoiceTotal;
        TextView textInvoiceDiscount;
        TextView textTotalAfterDiscount;
        TextView textTax;
        TextView textPaid;
        TextView textInvoiceRemain;
        TextView textFinalTotal;

        TextView textInvoiceClient;

        LinearLayout linearActions;
        TextView textAddInvoice;
        TextView textAddSignature;
        TextView textDeleteInvoice;

        public DataObjectHolder(View view) {
            super(view);

            this.relLayout = view.findViewById(R.id.relLayout);
            this.textInvoiceClient = view.findViewById(R.id.textInvoiceClient);
            this.textInvoiceNumber = view.findViewById(R.id.textInvoiceNumber);
            this.textInvoiceDate = view.findViewById(R.id.textInvoiceDate);
            this.textInvoiceNotes = view.findViewById(R.id.textInvoiceNotes);
            this.textInvoiceTotal = view.findViewById(R.id.textInvoiceTotal);
            this.textInvoiceDiscount = view.findViewById(R.id.textInvoiceDiscount);
            this.textTotalAfterDiscount = view.findViewById(R.id.textTotalAfterDiscount);
            this.textTax = view.findViewById(R.id.textTax);
            this.textPaid = view.findViewById(R.id.textPaid);
            this.textInvoiceRemain = view.findViewById(R.id.textInvoiceRemain);
            this.textFinalTotal = view.findViewById(R.id.textFinalTotal);

            this.linearActions = view.findViewById(R.id.linearActions);
            this.textAddInvoice = view.findViewById(R.id.textAddInvoice);
            this.textAddSignature = view.findViewById(R.id.textAddSignature);
            this.textDeleteInvoice = view.findViewById(R.id.textDeleteInvoice);


        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, int position) {

        final ClientInvoiceModel hoursOfflineModel = mDataSetFiltered.get(position);

        String actionDate = MyHelpers.formatDateFromOneToAnother(hoursOfflineModel.getActionDate(), "yyyy-MM-dd'T'hh:mm:ss", "yyyy-MMM-dd");

        if (hoursOfflineModel.getRemainValue()>0) {
            holder.linearActions.setVisibility(View.VISIBLE);
            holder.relLayout.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        if (type.equalsIgnoreCase("all")) {
            holder.textInvoiceClient.setVisibility(View.VISIBLE);
        }


        if (DateUtils.isToday(MyHelpers.milliseconds(hoursOfflineModel.getActionDate()))) {
            holder.textDeleteInvoice.setVisibility(View.VISIBLE);

        } else {
            holder.textDeleteInvoice.setVisibility(View.GONE);

        }
        holder.textInvoiceClient.setText(String.format("%s\t:\t%s", context.getString(R.string.clientName), hoursOfflineModel.getClientName()));
        holder.textInvoiceNumber.setText(String.format("%s\t:\t%s", context.getString(R.string.invoiceNumber), hoursOfflineModel.getInvoiceTaxNumber()));
        holder.textInvoiceDate.setText(String.format("%s\t:\t%s", context.getString(R.string.date), actionDate));
        holder.textInvoiceNotes.setText(String.format("%s\t:\t%s", context.getString(R.string.notes), hoursOfflineModel.getNotes()));
        holder.textInvoiceTotal.setText(String.format("%s\t:\t%s", context.getString(R.string.total), hoursOfflineModel.getTotalValue()));
        holder.textInvoiceDiscount.setText(String.format("%s\t:\t%s", context.getString(R.string.discount), hoursOfflineModel.getDiscount()));
        holder.textTotalAfterDiscount.setText(String.format("%s\t:\t%s", context.getString(R.string.totalDiscount), hoursOfflineModel.getTotalAfterDiscount()));
        holder.textTax.setText(String.format("%s\t:\t%s", context.getString(R.string.tax), hoursOfflineModel.getTaxValue()));
        holder.textPaid.setText(String.format("%s\t:\t%s", context.getString(R.string.paid), hoursOfflineModel.getPaidValue()));
        holder.textInvoiceRemain.setText(String.format("%s\t:\t%s", context.getString(R.string.unPaid), hoursOfflineModel.getRemainValue()));
        holder.textFinalTotal.setText(String.format("%s\t:\t%s", context.getString(R.string.finalMoney), hoursOfflineModel.getFinalValue()));


        holder.textAddInvoice.setOnClickListener(view -> {

            if (type.equalsIgnoreCase("all")) {
                creditInvoicesFragment.ShowLogOutDialog(hoursOfflineModel);

            } else {
                clientProfileActivity.ShowLogOutDialog(hoursOfflineModel);
            }
        });

        holder.textAddSignature.setOnClickListener(view -> {

            if (type.equalsIgnoreCase("all")) {
                creditInvoicesFragment.ShowSignatureDialog(hoursOfflineModel.getId() + "");

            } else {
                clientProfileActivity.ShowSignatureDialog(hoursOfflineModel.getId() + "");
            }

        });


        holder.textDeleteInvoice.setOnClickListener(view -> {

            if (type.equalsIgnoreCase("all")) {
                creditInvoicesFragment.showDeleteDialog(hoursOfflineModel.getId() + "");

            } else {
                clientProfileActivity.showDeleteDialog(hoursOfflineModel.getId() + "");
            }

        });

        holder.relLayout.setOnClickListener(v -> {

            Intent i = new Intent(context, TaskPrintActivity.class);
            i.putExtra("clientInvoiceModel", hoursOfflineModel);
             context.startActivity(i);

        });

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataSetFiltered.size();
    }


}



