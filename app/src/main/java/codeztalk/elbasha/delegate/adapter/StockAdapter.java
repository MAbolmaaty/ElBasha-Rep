package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.models.ProductModel;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.DataObjectHolder> implements Filterable {
    ArrayList<ProductModel> mDataSet;
    ArrayList<ProductModel> mDataSetFiltered;

    Context context;


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataSetFiltered = mDataSet;
                } else {
                    ArrayList<ProductModel> filteredList = new ArrayList<>();
                    for (ProductModel row : mDataSet) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getmProductNameAR().toLowerCase().contains(charString.toLowerCase())
                                || row.getmProductNameEN().toLowerCase().contains(charString.toLowerCase())) {
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
                mDataSetFiltered = (ArrayList<ProductModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public StockAdapter(ArrayList<ProductModel> mDataSet, Context context ) {
        this.mDataSetFiltered = mDataSet;
        this.mDataSet = mDataSet;

        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        private TextView textProductName;
        private TextView textProductEN;
        private TextView textSerial;
        private TextView textStock1;
        private TextView textStock2;
        private TextView textStock3;

        LinearLayout linearStock;


        public DataObjectHolder(View view) {
            super(view);


            textProductName = itemView.findViewById(R.id.textProductName);
            textProductEN = itemView.findViewById(R.id.textProductEN);
            textSerial = itemView.findViewById(R.id.textSerial);
            textStock1 = itemView.findViewById(R.id.textStock1);
            textStock2 = itemView.findViewById(R.id.textStock2);
            textStock3 = itemView.findViewById(R.id.textStock3);



            linearStock = itemView.findViewById(R.id.linearStock);


        }


    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stock, parent, false);
        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, int position) {

        holder.setIsRecyclable(false);


        final ProductModel productModel = mDataSetFiltered.get(position);

        if (position % 2 == 0) {
            holder.linearStock.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.textSerial.setText(String.valueOf(position+1));

        holder.textProductName.setText(productModel.getmProductNameAR());
        holder.textProductEN.setText(productModel.getmProductNameEN());
        holder.textStock1.setText(String.valueOf(productModel.getUnitInQty()));
        holder.textStock2.setText(String.valueOf(productModel.getUnitOutQty()));
        holder.textStock3.setText(String.valueOf(productModel.getUnitStockQty()));



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


