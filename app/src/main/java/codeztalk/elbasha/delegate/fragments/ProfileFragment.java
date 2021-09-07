package codeztalk.elbasha.delegate.fragments;

import static codeztalk.elbasha.delegate.helper.constants.baseURL;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.util.Objects;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.activities.HomeActivity;
import codeztalk.elbasha.delegate.activities.ReportActivity;
import codeztalk.elbasha.delegate.activities.stock.StockActivity;


public class ProfileFragment extends BaseFragment {

    TextView textEmployeeName;
    TextView textEmployeeEmail;
    TextView textEmployeeAddress;
    TextView textEmployeePhone;
    TextView textReportDay;
    TextView textReportStore;
    TextView textPocket;
    TextView textReport10;
    EditText editPaperHeight;
    TextView textSave;
    ImageView imageUser;
    ImageView imageUser2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, R.layout.fragment_profile);
        textEmployeeName = mView.findViewById(R.id.textEmployeeName);
        textEmployeeEmail = mView.findViewById(R.id.textEmployeeEmail);
        textEmployeeAddress = mView.findViewById(R.id.textEmployeeAddress);
        textEmployeePhone = mView.findViewById(R.id.textEmployeePhone);

        imageUser = mView.findViewById(R.id.imageUser);
        imageUser2 = mView.findViewById(R.id.imageUser2);

        textReportDay = mView.findViewById(R.id.textReportDay);
        textReportStore = mView.findViewById(R.id.textReportStore);
        textPocket = mView.findViewById(R.id.textPocket);

        textSave = mView.findViewById(R.id.textSave);
        editPaperHeight = mView.findViewById(R.id.editPaperHeight);

        textEmployeeName.setText(preferenceHelper.getEmpName());
        textEmployeePhone.setText(preferenceHelper.getUserPhone());
        textEmployeeAddress.setText(preferenceHelper.getUserAddress());
        textEmployeeEmail.setText(preferenceHelper.getUserEmail());

        Glide.with(Objects.requireNonNull(mContext))
                .load((baseURL + preferenceHelper.getUserPhoto()).replace("~", ""))
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .into(imageUser);

        Glide.with(Objects.requireNonNull(mContext))
                .load((baseURL + preferenceHelper.getUserPhoto()).replace("~", ""))
                .fitCenter()
                .placeholder(R.drawable.picture_placeholder)
                .into(imageUser2);

        textReportStore.setOnClickListener(v -> launchStock());
        textReportDay.setOnClickListener(v -> launchReport());
        textPocket.setOnClickListener(v -> launchPocket());

        editPaperHeight.setText(String.valueOf(preferenceHelper.getPaperHeight()));

        textSave.setOnClickListener(v -> preferenceHelper.setPaperHeight(Integer.parseInt(editPaperHeight.getText().toString())));

        return mView;
    }


    private void launchStock() {

        Intent intent = new Intent(getActivity(), StockActivity.class);
        startActivity(intent);

    }

    private void launchReport() {

        Intent intent = new Intent(getActivity(), ReportActivity.class);
        startActivity(intent);

    }

    private void launchPocket() {

        ((HomeActivity) Objects.requireNonNull(getActivity())).
                navigateToFragment(new PocketFragment(), false, getString(R.string.addPocket), "pocket");
    }

}