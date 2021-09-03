package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.fragments.reports.Reports5Fragment;
import codeztalk.elbasha.delegate.fragments.reports.Reports6Fragment;
import codeztalk.elbasha.delegate.fragments.reports.Reports7Fragment;
import codeztalk.elbasha.delegate.fragments.reports.Reports8Fragment;
import codeztalk.elbasha.delegate.models.ClientModel;

public class ClientFilterAdapter extends RecyclerView.Adapter<ClientFilterAdapter.DataObjectHolder> implements Filterable {
    private List<ClientModel> mDataSet;
    private List<ClientModel> mDataSetFiltered;

    Context context;

    Reports5Fragment reports5Fragment;
    Reports6Fragment reports6Fragment;
    Reports7Fragment reports7Fragment;
    Reports8Fragment reports8Fragment;
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
                    List<ClientModel> filteredList = new ArrayList<>();
                    for (ClientModel row : mDataSet) {

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
                mDataSetFiltered = (List<ClientModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }




    public ClientFilterAdapter(ArrayList<ClientModel> mDataSet, Reports5Fragment reports1Fragment, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.reports5Fragment = reports1Fragment;
        this.type = type;


    }


    public ClientFilterAdapter(ArrayList<ClientModel> mDataSet, Reports6Fragment reports1Fragment, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.reports6Fragment = reports1Fragment;
        this.type = type;


    }

    public ClientFilterAdapter(ArrayList<ClientModel> mDataSet, Reports7Fragment reports1Fragment, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.reports7Fragment = reports1Fragment;
        this.type = type;


    }


    public ClientFilterAdapter(ArrayList<ClientModel> mDataSet, Reports8Fragment reports1Fragment, String type) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.reports8Fragment = reports1Fragment;
        this.type = type;


    }


    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textName;


        public DataObjectHolder(View view) {
            super(view);


            this.textName = view.findViewById(R.id.textName);


        }

    }


    @Override
    public ClientFilterAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false);
        context = parent.getContext();

        ClientFilterAdapter.DataObjectHolder dataObjectHolder = new ClientFilterAdapter.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final ClientFilterAdapter.DataObjectHolder holder, int position) {

        final ClientModel hoursOfflineModel = mDataSetFiltered.get(position);


        holder.textName.setText(hoursOfflineModel.getClientName());

        holder.textName.setOnClickListener(view -> {

          if (type.equalsIgnoreCase("report5")) {
                reports5Fragment.clientId = String.valueOf(hoursOfflineModel.getId());
                reports5Fragment.clientDialog.cancel();
                reports5Fragment.textClientName.setText(hoursOfflineModel.getClientName());
            }
            else if (type.equalsIgnoreCase("report6")) {
                reports6Fragment.clientId = String.valueOf(hoursOfflineModel.getId());
                reports6Fragment.clientDialog.cancel();
                reports6Fragment.textClientName.setText(hoursOfflineModel.getClientName());
            }
            else if (type.equalsIgnoreCase("report7")) {
                reports7Fragment.clientId = String.valueOf(hoursOfflineModel.getId());
                reports7Fragment.clientDialog.cancel();
                reports7Fragment.textClientName.setText(hoursOfflineModel.getClientName());
            }

            else if (type.equalsIgnoreCase("report8")) {
                reports8Fragment.clientId = String.valueOf(hoursOfflineModel.getId());
                reports8Fragment.clientDialog.cancel();
                reports8Fragment.textClientName.setText(hoursOfflineModel.getClientName());
            }


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





