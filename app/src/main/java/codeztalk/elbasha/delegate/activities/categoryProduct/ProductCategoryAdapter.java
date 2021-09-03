package codeztalk.elbasha.delegate.activities.categoryProduct;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.db.ForsahDB;

public class ProductCategoryAdapter extends RecyclerView.Adapter<ProductCategoryAdapter.DataObjectHolder> implements Filterable {
    ArrayList<Product> mDataSet;
    ArrayList<Product> mDataSetFiltered;


    Context context;

    CategoryProductsActivity allProductsActivity;
    ForsahDB db;
    private int finalQuantity;

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataSetFiltered = mDataSet;
                } else {
                    ArrayList<Product> filteredList = new ArrayList<>();
                    for (Product row : mDataSet) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getProductNameAR().toLowerCase().contains(charString.toLowerCase())
                                || row.getProductNameEN().toLowerCase().contains(charString.toLowerCase())) {
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
                mDataSetFiltered = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public ProductCategoryAdapter(ArrayList<Product> mDataSet, Context context, CategoryProductsActivity allProductsActivity) {
        this.mDataSetFiltered = mDataSet;
        this.mDataSet = mDataSet;

        this.context = context;
        this.allProductsActivity = allProductsActivity;
        db = new ForsahDB(context);

    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder {


        private EditText editAmount;
        private EditText editPrice;
        ImageView imagePlus;
        ImageView imageMinus;

        RadioButton radioUnit;
        RadioButton radioBox;


        private TextView textProductName;
        private TextView textProductEN;
        private TextView textPrice;
        private TextView textTotal;
        private TextView textProductStock;

        LinearLayout linearProduct;


        public DataObjectHolder(View view) {
            super(view);


            textProductName = itemView.findViewById(R.id.textProductName);
            textProductEN = itemView.findViewById(R.id.textProductEN);
            textPrice = itemView.findViewById(R.id.textPrice);
            textTotal = itemView.findViewById(R.id.textTotal);
            textProductStock = itemView.findViewById(R.id.textProductStock);

            editAmount = itemView.findViewById(R.id.editAmount);
            editPrice = itemView.findViewById(R.id.editPrice);
            imagePlus = itemView.findViewById(R.id.imagePlus);
            imageMinus = itemView.findViewById(R.id.imageMinus);

            radioUnit = itemView.findViewById(R.id.radioUnit);
            radioBox = itemView.findViewById(R.id.radioBox);

            linearProduct = itemView.findViewById(R.id.linearProduct);


        }


    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aa, parent, false);
        context = parent.getContext();

        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DataObjectHolder holder, int position) {

        holder.setIsRecyclable(false);


        final Product productModel = mDataSetFiltered.get(position);

        if (position % 2 == 0) {
            holder.linearProduct.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        holder.textProductName.setText(productModel.getProductNameAR().replace(productModel.getCategoryNameAR(),""));
        holder.textProductEN.setText(productModel.getProductNameEN().replace(productModel.getCategoryNameEN(),""));

//        holder.textProductName.setText(""+productModel.getBoxNumber());
//        holder.textProductEN.setText(productModel.getCategoryNameEN());


        holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));
        holder.textProductStock.setText(context.getString(R.string.stocky) + "\t" + productModel.getUnitStockQty());

        holder.textPrice.setText(String.valueOf(productModel.getOfficialUnitPrice()));
        holder.editPrice.setText(String.valueOf(productModel.getUnitPrice()));


        if (productModel.getProductAmount() > 0) {
            holder.editAmount.setText(String.valueOf(productModel.getProductAmount()));

        }

        if (productModel.getUnitStockQty() < productModel.getBoxUnit()) {
            holder.radioBox.setEnabled(false);
        }

        if (!holder.editAmount.getText().toString().equalsIgnoreCase(""))
        {

            holder.radioBox.setEnabled(Integer.parseInt(holder.editAmount.getText().toString()) <= productModel.getBoxNumber());
        }

        //0 is unit  1 is box
        if (productModel.getType() == 0) {
            holder.radioUnit.setChecked(true);
            holder.radioBox.setChecked(false);
            holder.textPrice.setText(String.valueOf(productModel.getUnitPrice()));
            holder.editPrice.setText(String.valueOf(productModel.getUnitPrice()));

        } else {
            holder.radioUnit.setChecked(false);
            holder.radioBox.setChecked(true);
            holder.textPrice.setText(String.valueOf(productModel.getBoxPrice()));
            holder.editPrice.setText(String.valueOf(productModel.getBoxPrice()));

        }


        holder.radioUnit.setOnClickListener(v ->
        {


            productModel.setType(0);
            holder.textPrice.setText(String.valueOf(productModel.getUnitPrice()));
            holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));
            holder.editPrice.setText(String.valueOf(productModel.getOfficialUnitPrice()));


            db.updateProductType(productModel.getId(), 0);
//            allProductsActivity.calculatePrice(mDataSet);
            allProductsActivity.calculatePrice();

        });


        holder.radioBox.setOnClickListener(v ->
        {
            if (finalQuantity <= productModel.getBoxNumber()) {


                productModel.setType(1);
                holder.textPrice.setText(String.valueOf(productModel.getBoxPrice()));
                holder.editPrice.setText(String.valueOf(productModel.getBoxPrice()));
                holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));
                db.updateProductType(productModel.getId(), 1);
                allProductsActivity.calculatePrice();

            }




        });


        holder.imagePlus.setOnClickListener(v -> {


            ProductCategoryAdapter.this.finalQuantity = productModel.getProductAmount() + 1;
            productModel.setProductAmount(finalQuantity);

//            productModel.setUnitStockQty(productModel.getUnitStockQty() - 1);
//            holder.textProductStock.setText(context.getString(R.string.stocky) + "\t" + productModel.getUnitStockQty());

            holder.editAmount.setText(String.valueOf(finalQuantity));


        });

        holder.imageMinus.setOnClickListener(v -> {

            if (productModel.getProductAmount() > 0) {
                ProductCategoryAdapter.this.finalQuantity = productModel.getProductAmount() - 1;
                productModel.setProductAmount(finalQuantity);
                holder.editAmount.setText(String.valueOf(finalQuantity));

//                productModel.setUnitStockQty(productModel.getUnitStockQty() + 1);
//                holder.textProductStock.setText(context.getString(R.string.stocky) + "\t" + productModel.getUnitStockQty());


            }

            if (productModel.getProductAmount() == 1) {

//                    ShowConfirmDialog(position);

            }


        });


        holder.editAmount.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                Log.e("editable", " >>" + editable.toString());


            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equalsIgnoreCase(""))
                {

                       if (Integer.parseInt(charSequence.toString()) <= productModel.getBoxNumber()) {
                         holder.radioBox.setEnabled(true);

                       }

                       else {
                           holder.radioBox.setEnabled(false);

                       }

                       //TODO : Change
//                    if (Integer.parseInt(charSequence.toString()) <= productModel.getUnitStockQty()
//                         &&productModel.getType()==0)
                        if (productModel.getType()==0)

                        {


                        productModel.setProductAmount(Integer.parseInt(charSequence.toString()));
                        holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));
                        db.updateProductQuantity(productModel.getId(), Integer.parseInt(charSequence.toString()));
                        allProductsActivity.calculatePrice();


                    }
                  else if (Integer.parseInt(charSequence.toString()) <= productModel.getBoxNumber()
                            &&productModel.getType()==1) {


                        productModel.setProductAmount(Integer.parseInt(charSequence.toString()));
                        holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));
                        db.updateProductQuantity(productModel.getId(), Integer.parseInt(charSequence.toString()));
                        allProductsActivity.calculatePrice();


                    }

                    else {
                        Toast.makeText(context, context.getString(R.string.noStock), Toast.LENGTH_SHORT).show();

//                    holder.editAmount.setText(String.valueOf(5));

                    }
                }


            }
        });



        holder.editPrice.addTextChangedListener(new TextWatcher() {

            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                Log.e("editable", " >>" + editable.toString());

            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (!charSequence.toString().equalsIgnoreCase("")||
                        !charSequence.toString().equalsIgnoreCase(".")) {

                    productModel.setUnitPrice(Double.parseDouble(charSequence.toString()));
                    holder.textTotal.setText(String.valueOf(productModel.getTotalPrice()));

                    db.updateProductPrice(productModel.getId(), Double.parseDouble(charSequence.toString()));
                    allProductsActivity.calculatePrice();

                }


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



