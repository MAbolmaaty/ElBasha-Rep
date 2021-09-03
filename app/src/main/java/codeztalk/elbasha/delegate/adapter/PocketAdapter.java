package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.fragments.PocketFragment;
import codeztalk.elbasha.delegate.models.PocketModel;

public class PocketAdapter extends RecyclerView.Adapter<PocketAdapter.DataObjectHolder> {
    private ArrayList<PocketModel> mDataSet;
    Context context;

    PocketFragment pocketFragment;

    public PocketAdapter(ArrayList<PocketModel> mDataSet, PocketFragment pocketFragment) {
        this.mDataSet = mDataSet;
        this.pocketFragment = pocketFragment;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textMoney;
        TextView textNotes;
        TextView textDate;

        ImageView imageDelete;

        public DataObjectHolder(View view) {
            super(view);

            this.textMoney = view.findViewById(R.id.textMoney);
            this.textNotes = view.findViewById(R.id.textNotes);
            this.textDate = view.findViewById(R.id.textDate);
            this.imageDelete = view.findViewById(R.id.imageDelete);


        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pocket, parent, false);
        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        final PocketModel hoursOfflineModel = mDataSet.get(position);

        holder.textMoney.setText(String.format("%s \t جنيه", hoursOfflineModel.getTransactionAmount()));
        holder.textNotes.setText(hoursOfflineModel.getNotes());
        holder.textDate.setText(hoursOfflineModel.getTransactionDate());


//        holder.imageDelete.setOnClickListener(v1 -> {
//            pocketFragment.deletePocket(hoursOfflineModel.getId()+"");
//            deleteItem(position);
//
//        });

    }

    public void addItem(PocketModel dataObj) {
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


}



