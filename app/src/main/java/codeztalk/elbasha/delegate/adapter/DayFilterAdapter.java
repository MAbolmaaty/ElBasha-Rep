package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.fragments.HomeFragment;
import codeztalk.elbasha.delegate.helper.MyHelpers;
import codeztalk.elbasha.delegate.models.DayModel;

public class DayFilterAdapter extends RecyclerView.Adapter<DayFilterAdapter.DataObjectHolder> {
    private ArrayList<DayModel> mDataSet;
    Context context;
    private int selected_position =0;

    HomeFragment homeFragment;


    public DayFilterAdapter(ArrayList<DayModel> mDataSet, Context context,HomeFragment homeFragment) {
        this.mDataSet = mDataSet;
        this.context = context;
        this.homeFragment = homeFragment;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_filter, parent, false);
        context = parent.getContext();

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, int position) {

        final DayModel dayModel = mDataSet.get(position);

        holder.textDayName.setText(dayModel.getDayAR());


//        selected_position=getCurrentDay();


        if (selected_position == position) {

            holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.rgb(0, 72, 154), R.color.textColor, 1, 9));
            holder.textDayName.setTextColor(Color.WHITE);

        }
        else
            {
                holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.WHITE, R.color.textColor, 1, 9));
                holder.textDayName.setTextColor(Color.BLACK);
            }


        holder.textDayName.setOnClickListener(v -> {

            holder.textDayName.setBackground(MyHelpers.makeCustomShape(Color.rgb(0, 72, 154), R.color.textColor, 1, 9));
            holder.textDayName.setTextColor(Color.WHITE);
            selected_position = position;

            homeFragment.getDayClients(dayModel.getId());

            notifyDataSetChanged();

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




    int getCurrentDay()
    {

        Calendar calendar = Calendar.getInstance();

        Log.e("",""+Calendar.SATURDAY);
        return calendar.get(Calendar.DAY_OF_WEEK);

    }
}



