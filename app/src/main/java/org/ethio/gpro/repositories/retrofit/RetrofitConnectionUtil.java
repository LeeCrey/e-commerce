package org.ethio.gpro.repositories.retrofit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConnectionUtil {
    private static final String baseUrl = "https://5e8f-197-156-80-12.eu.ngrok.io/";
    private static Retrofit retrofit;

    public static synchronized Retrofit getRetrofitInstance(String bUrl) {
        if (null == retrofit) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .followRedirects(false)
                    .build();
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.WRAP_ROOT_VALUE);

            String finalUrl = (bUrl == null) ? baseUrl : bUrl;
            retrofit = new Retrofit.Builder()
                    .baseUrl(finalUrl)
                    .addConverterFactory(JacksonConverterFactory.create(mapper))
                    .client(client).build();
        }
        return retrofit;
    }
}
