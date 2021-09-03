package codeztalk.elbasha.delegate.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import codeztalk.elbasha.delegate.helper.PreferenceHelper;

public class BaseFragment extends Fragment {


    protected Context mContext;
    protected View mView;
   public PreferenceHelper preferenceHelper;

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        mContext = context;
        preferenceHelper = new PreferenceHelper(context);


    }

    public void onCreateView(LayoutInflater inflater, ViewGroup container, int layout) {
        mView = inflater.inflate(layout, container, false);
     }


}
