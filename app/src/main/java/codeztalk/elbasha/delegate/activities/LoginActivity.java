package codeztalk.elbasha.delegate.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import codeztalk.elbasha.delegate.R;
import codeztalk.elbasha.delegate.aPIS.ApiClient;
import codeztalk.elbasha.delegate.aPIS.ApiService;
import codeztalk.elbasha.delegate.models.EmployeeModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static codeztalk.elbasha.delegate.helper.MyHelpers.isEmpty;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.removeSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.ProgressDialogHelper.showSimpleProgressDialog;
import static codeztalk.elbasha.delegate.helper.constants.authenticate;
import static codeztalk.elbasha.delegate.helper.constants.baseURL;

public class LoginActivity extends BaseActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    TextView textLogin;

    EditText editEmail;
    EditText editPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textLogin = findViewById(R.id.textLogin);
        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);

        if (preferenceHelper.getLanguageID() == 1) {
            editPassword.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        } else {
            editPassword.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);

        }


        textLogin.setOnClickListener(view -> {

            if (validateInputs()) {

                login();
            }

        });


    }


    private boolean validateInputs() {
        if (isEmpty(editEmail)) {
            editEmail.setError(LoginActivity.this.getString(R.string.empty_name_error));
            return false;
        }

        if (isEmpty(editPassword)) {
            editPassword.setError(LoginActivity.this.getString(R.string.empty_password_error));

            return false;
        }

        if (editPassword.getText().length() < 6) {
            editPassword.setError(LoginActivity.this.getString(R.string.length_password_error));

            return false;
        }

        return true;
    }

    private void launchHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();

    }


    void login() {
        showSimpleProgressDialog(this, false);

        AndroidNetworking.post(baseURL + authenticate)
                .addBodyParameter("username", editEmail.getText().toString())
                .addBodyParameter("password", editPassword.getText().toString())
                .addBodyParameter("grant_type", "password")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        Log.e("response", " >>" + response);
                        try {
                            String userToken = response.getString("access_token");
                            preferenceHelper.setUserToken("Bearer " + userToken);
                            getEmployeeProfile(editEmail.getText().toString(), "Bearer " + userToken);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error

                        Log.e("getErrorBody", " >>" + error.getErrorBody());
                        Log.e("getErrorDetail", " >>" + error.getErrorDetail());
                        Log.e("getMessage", " >>" + error.getMessage());

                        removeSimpleProgressDialog();


                    }
                });

    }


    EmployeeModel employeeModel;

    private void getEmployeeProfile(String userName, String token) {
        ApiService apiInterface = ApiClient.getClient().create(ApiService.class);

        Call<EmployeeModel> call = apiInterface.getEmployeeProfileByName(userName, token);

        call.enqueue(new Callback<EmployeeModel>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeModel> call,
                                   @NonNull Response<EmployeeModel> response) {
                removeSimpleProgressDialog();

                if (response.body() != null) {

                    employeeModel = response.body();

                    preferenceHelper.setUserLogged(true);
                    preferenceHelper.setUserId(employeeModel.getId() + "");
                    preferenceHelper.setEmpName(employeeModel.getEmpName());
                    preferenceHelper.setUserPhoto(employeeModel.getEmpImagePath());
                    preferenceHelper.setUserName(employeeModel.getUserName());
                    preferenceHelper.setUserPhone(employeeModel.getPhoneNumber());
                    preferenceHelper.setUserEmail(employeeModel.getEmail());
                    preferenceHelper.setUserAddress(employeeModel.getEmpAddress());
                    launchHome();
                }
            }

            @Override
            public void onFailure(@NonNull Call<EmployeeModel> call, @NonNull Throwable t) {
                removeSimpleProgressDialog();
                Log.e("fail", "is : " + call.toString());
                t.printStackTrace();
            }
        });
    }


}
