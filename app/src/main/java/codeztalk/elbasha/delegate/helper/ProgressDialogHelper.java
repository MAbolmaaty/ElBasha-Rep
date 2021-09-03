package codeztalk.elbasha.delegate.helper;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import java.util.Objects;

import codeztalk.elbasha.delegate.R;


public class ProgressDialogHelper {

    private static ProgressDialog mProgressDialog;


    public static void showSimpleProgressDialog(Context context, boolean isCancelable) {
        try {

            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.show();

                mProgressDialog.setContentView(R.layout.progress_dialog_layout);
                Objects.requireNonNull(mProgressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                mProgressDialog.setCancelable(isCancelable);
                ProgressBar bar = mProgressDialog.findViewById(R.id.progress_bar);
                Drawable progressDrawable = bar.getIndeterminateDrawable().mutate();
                progressDrawable.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                bar.setProgressDrawable(progressDrawable);

            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (Exception ie) {
            ie.printStackTrace();
        }
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (Exception ie) {
            ie.printStackTrace();

        }

    }


}
