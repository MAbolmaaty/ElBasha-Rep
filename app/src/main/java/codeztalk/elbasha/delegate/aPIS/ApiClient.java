package codeztalk.elbasha.delegate.aPIS;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import codeztalk.elbasha.delegate.helper.constants;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofit = null;




    private static OkHttpClient okHttpClient = new OkHttpClient().newBuilder().addInterceptor(chain ->
    {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest.newBuilder();

        Request newRequest = builder .build();

        return chain.proceed(newRequest);
    }).readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).build();


    public static Retrofit getClient() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

             retrofit = new Retrofit.Builder()
                    .baseUrl(constants.baseURL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }


}