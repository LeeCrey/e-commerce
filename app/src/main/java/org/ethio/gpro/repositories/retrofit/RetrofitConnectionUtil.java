package org.ethio.gpro.repositories.retrofit;

import android.app.Application;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.ethio.gpro.R;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConnectionUtil {
    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofitInstance(@NonNull Application application) {
        if (null == retrofit) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .followRedirects(false)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .callTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRAP_ROOT_VALUE);

            String finalUrl = application.getString(R.string.base_url);
            retrofit = new Retrofit.Builder()
                    .baseUrl(finalUrl)
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .client(client).build();
        }
        return retrofit;
    }
}
