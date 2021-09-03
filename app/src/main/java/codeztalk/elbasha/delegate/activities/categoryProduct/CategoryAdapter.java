package codeztalk.elbasha.delegate.activities.categoryProduct;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.helper.MyHelpers;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.DataObjectHolder> {
    private ArrayList<CategoryProductModel> mDataSet;
    Context context;
    private int selected_position =0;

    CategoryProductsActivity categoryProductsActivity;


    public CategoryAdapter(ArrayList<CategoryProductModel> mDataSet, Context context,CategoryProductsActivity categoryProductsActivity) {
        this.mDataSet = mDataSet;
        this.context = context;
        this.categoryProductsActivity = categoryProductsActivity;

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

        final CategoryProductModel dayModel = mDataSet.get(position);

        holder.textDayName.setText(dayModel.getCategoryNameAR());

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

            Log.e("NameAR","  >> "+dayModel.getCategoryNameAR());
            Log.e("NameEN"," >>> "+dayModel.getCategoryNameEN());
            categoryProductsActivity.getProducts(dayModel.getCategoryNameEN());
//
            notifyDataSetChanged();

        });

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




