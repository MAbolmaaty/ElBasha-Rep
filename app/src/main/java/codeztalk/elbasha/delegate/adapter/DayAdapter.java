package codeztalk.elbasha.delegate.adapter;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.DayModel;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DataObjectHolder> {
    private ArrayList<DayModel> mDataSet;
    Context context;


    public DayAdapter(ArrayList<DayModel> mDataSet, Context context) {
        this.mDataSet = mDataSet;
        this.context = context;

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        TextView textDayName;


        public DataObjectHolder(View view) {
            super(view);

            this.textDayName = view.findViewById(R.id.textDayName);


        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false);
        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        final DayModel hoursOfflineModel = mDataSet.get(position);

        holder.textDayName.setText(hoursOfflineModel.getDayAR());


        if (hoursOfflineModel.isSelected()) {
            holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.rgb(0, 72, 154), R.color.textColor, 1, 9));
            holder.textDayName.setTextColor(Color.WHITE);
        } else {

            holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.WHITE, R.color.textColor, 1, 9));
            holder.textDayName.setTextColor(Color.BLACK);
        }


        holder.textDayName.setOnClickListener(v -> {


            if (hoursOfflineModel.isSelected()) {
                hoursOfflineModel.setSelected(false);
                holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.WHITE, R.color.textColor, 1, 9));
                holder.textDayName.setTextColor(Color.BLACK);


            } else {
                hoursOfflineModel.setSelected(true);
                holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.rgb(0, 72, 154), R.color.textColor, 1, 9));
                holder.textDayName.setTextColor(Color.WHITE);

            }

        });

    }

    public void addItem(DayModel dataObj) {
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

    public void restoreItem(DayModel item, int position) {
        mDataSet.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

}


