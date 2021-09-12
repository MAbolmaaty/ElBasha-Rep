package codeztalk.elbasha.delegate.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.AddClientActivity;
import codeztalk.elbasha.delegate.activities.ClientProfileActivity;
import codeztalk.elbasha.delegate.activities.categoryProduct.CategoryProductsActivity;
import codeztalk.elbasha.delegate.models.ClientModel;
import codeztalk.elbasha.delegate.models.ConnectedDevice;
import codeztalk.elbasha.delegate.swipereveallayout.SwipeRevealLayout;
import codeztalk.elbasha.delegate.swipereveallayout.ViewBinderHelper;


public class ClientSwipeAdapter extends RecyclerView.Adapter<ClientSwipeAdapter.ViewHolder> implements Filterable {

    private final List<ClientModel> mDataSet;
    private List<ClientModel> mDataSetFiltered;
    private final Context mContext;
    private LayoutInflater mInflater;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
    private ConnectedDevice mConnectedDevice;

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


    public ClientSwipeAdapter(List<ClientModel> mDataSet, Context context,
                              ConnectedDevice connectedDevice) {
        this.mDataSet = mDataSet;
        this.mDataSetFiltered = mDataSet;
        this.mContext = context;
        try {
            mInflater = LayoutInflater.from(context);
        } catch (Exception e) {
            Log.e("Exception"," >"+e.toString());
        }
        this.mConnectedDevice = connectedDevice;
     }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {

        if (mDataSetFiltered != null && 0 <= position && position < mDataSetFiltered.size()) {

            final ClientModel clientModel = mDataSetFiltered.get(position);


            // Use ViewBindHelper to restore and save the open/close state of the SwipeRevealView
            // put an unique string id as value, can be any string which uniquely define the data
//            binderHelper.bind(holder.swipeLayout, clientModel.getClientName());
            binderHelper.bind(((ViewHolder) h).swipeLayout, position + "");

            // Bind your data here
            ((ViewHolder) h).bind(clientModel);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataSetFiltered == null)
            return 0;
        return mDataSetFiltered.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private SwipeRevealLayout swipeLayout;
        private TextView textMap;
        private TextView textCall;
        private TextView textUpdate;
        private TextView textAddInvoice;

        private TextView textClientName;
        private TextView textClientAddress;
        private TextView textDelegateName;

        LinearLayout linearMain;

        public ViewHolder(View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.swipe_layout);

            textClientName = itemView.findViewById(R.id.textClientName);
            textClientAddress = itemView.findViewById(R.id.textClientAddress);
            textDelegateName = itemView.findViewById(R.id.textDelegateName);

            textMap = itemView.findViewById(R.id.textMap);
            textCall = itemView.findViewById(R.id.textCall);
            textUpdate = itemView.findViewById(R.id.textUpdate);
            textAddInvoice = itemView.findViewById(R.id.textAddInvoice);
            linearMain = itemView.findViewById(R.id.linear_main);
        }

        public void bind(final ClientModel clientModel) {

//            swipeLayout.setLockDrag(true);

            textClientName.setText(clientModel.getClientName());
            textClientAddress.setText(clientModel.getCreatedDate());
            textDelegateName.setText(String.format("%s\t%s", mContext.getString(R.string.clientResponsible), clientModel.getDelgateMan()));

            textAddInvoice.setOnClickListener(view -> {

                Intent i = new Intent(mContext, CategoryProductsActivity.class);
                i.putExtra("clientModel", clientModel);
                if (mConnectedDevice != null){
                    i.putExtra("printer_name", mConnectedDevice.getName());
                    i.putExtra("printer_mac_address", mConnectedDevice.getMacAddress());
                }
                mContext.startActivity(i);
            });

            linearMain.setOnClickListener(v -> {

                Intent i = new Intent(mContext, ClientProfileActivity.class);
                i.putExtra("clientModel", clientModel);
                mContext.startActivity(i);

            });

            textUpdate.setOnClickListener(v -> {

                Intent i = new Intent(mContext, AddClientActivity.class);
                i.putExtra("clientModel", clientModel);
                mContext.startActivity(i);
                swipeLayout.close(true);

            });

            textMap.setOnClickListener(view -> {


                if (!clientModel.getGPSLocation().equalsIgnoreCase("")) {
                    String uri = String.format(Locale.ENGLISH, "geo:%s", clientModel.getGPSLocation());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    mContext.startActivity(intent);
                    swipeLayout.close(true);

                } else {

                    Toast.makeText(mContext, R.string.empty_location_error, Toast.LENGTH_SHORT).show();
                }

            });

            textCall.setOnClickListener(v -> {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", clientModel.getMobile1(), null));
                mContext.startActivity(intent);
                swipeLayout.close(true);


            });


        }
    }


}
