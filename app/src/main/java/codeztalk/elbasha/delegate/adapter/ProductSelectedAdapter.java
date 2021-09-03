package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.categoryProduct.Product;
import codeztalk.elbasha.delegate.swipereveallayout.SwipeRevealLayout;
import codeztalk.elbasha.delegate.swipereveallayout.ViewBinderHelper;

public class ProductSelectedAdapter extends RecyclerView.Adapter<ProductSelectedAdapter.ViewHolder> {

    private final List<Product> mDataSet;

    private final Context mContext;


    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();






    public ProductSelectedAdapter(List<Product> dataSet, Context context) {
        mContext = context;
        mDataSet = dataSet;
        mInflater = LayoutInflater.from(context);

        // uncomment if you want to open only one row at a time
        // binderHelper.setOpenOnlyOne(true);
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_product_swipe, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        if (mDataSet != null && 0 <= position && position < mDataSet.size()) {

            final Product productModel = mDataSet.get(position);


            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
            binderHelper.bind(((ViewHolder) h).swipeLayout, productModel.getProductNameAR());

            // Bind your data here
            ((ViewHolder) h).bind(productModel);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSet == null)
            return 0;
        return mDataSet.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private final SwipeRevealLayout swipeLayout;

        private TextView textDelete;

        private TextView textAmount;



        private TextView textSerial;
        private TextView textProductName;
        private TextView textProductEN;
        private TextView textProductType;
        private TextView textPrice;
        private TextView textTotal;

        LinearLayout linearProduct;


        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);

            textSerial = itemView.findViewById(R.id.textSerial);
            textProductName = itemView.findViewById(R.id.textProductName);
            textProductEN = itemView.findViewById(R.id.textProductEN);
            textProductType = itemView.findViewById(R.id.textProductType);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTotal = itemView.findViewById(R.id.textTotal);

            textDelete = itemView.findViewById(R.id.textDelete);
            textAmount = itemView.findViewById(R.id.textAmount);


            linearProduct = itemView.findViewById(R.id.linearProduct);
        }

        public void bind(final Product productModel)
        {

            swipeLayout.setLockDrag(true);

            textDelete.setOnClickListener(v -> Toast.makeText(mContext, textDelete.getText().toString(), Toast.LENGTH_SHORT).show());

            textDelete.setOnClickListener(v -> {
                mDataSet.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());

            });


            if (mDataSet.indexOf(productModel) % 2 == 0) {
                linearProduct.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            textSerial.setText(String.valueOf(getAdapterPosition()+1));

            textProductName.setText(productModel.getProductNameAR());
            textProductEN.setText(productModel.getProductNameEN());
//            textAmount.setText(String.valueOf(productModel.getProductAmount()));
            textTotal.setText(String.valueOf(productModel.getTotalPrice()));
            textPrice.setText(String.valueOf(productModel.getPrice()));

            if(productModel.getType()==0)
            {
//                textProductType.setText("unit");
                textAmount.setText(productModel.getProductAmount()+"\t"+mContext.getString(R.string.unit2));

            }
            else
                {
                    textAmount.setText(productModel.getProductAmount()+"\t"+mContext.getString(R.string.box2));

                }



            linearProduct.setOnClickListener(view -> {


                Log.e("getProductAmount", ">> " + productModel.getProductAmount());

            });
        }
    }



}
