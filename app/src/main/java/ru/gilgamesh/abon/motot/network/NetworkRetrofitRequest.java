package ru.gilgamesh.abon.motot.network;

import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.payload.response.MessageResponse;

@Deprecated
@Singleton
public class NetworkRetrofitRequest {
    private static final String TAG = NetworkRetrofitRequest.class.getSimpleName();
    private static final String BASE_URL = BuildConfig.HOST_MICRO_URL;

    private static final Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static final Retrofit.Builder builderAuth = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
    private static final OkHttpClient.Builder httpClientAuth = new OkHttpClient.Builder()
            .addInterceptor(new HttpInterceptor())
            .authenticator(new AuthenticatorInterceptor())
            .callTimeout(30, TimeUnit.SECONDS);
    private static Retrofit retrofitAuth = builderAuth.client(httpClientAuth.build()).build();
    private static final HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    public static <S> S createService(Class<S> serviceClass) {
        if (BuildConfig.DEBUG && !httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
        }
        builder.client(httpClient
                //.connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                //.writeTimeout(30, TimeUnit.SECONDS)
                //.readTimeout(30, TimeUnit.SECONDS)
                //.retryOnConnectionFailure(false)
                .build());
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceAuth(Class<S> serviceClass) {
/*        if (BuildConfig.DEBUG) {
            httpClientAuth.addInterceptor(logging);
            retrofitAuth = builderAuth.build();
        }*/
//        if (App.loginInfo != null) {
//            if (BuildConfig.DEBUG) {
//                //httpClient.addInterceptor(logging);
//            }
//            builderAuth.client(httpClientAuth
//                    //.connectTimeout(30, TimeUnit.SECONDS)
//                    .callTimeout(30, TimeUnit.SECONDS)
//                    //.writeTimeout(30, TimeUnit.SECONDS)
//                    //.readTimeout(30, TimeUnit.SECONDS)
//                    //.retryOnConnectionFailure(false)
//                    .build());
//            retrofitAuth = builderAuth.build();
//        }
        return retrofitAuth.create(serviceClass);
    }

    public static MessageResponse parseError(Response<?> response) {
        Converter<ResponseBody, MessageResponse> converter = retrofit.responseBodyConverter(MessageResponse.class, new Annotation[0]);
        MessageResponse error;

        try {
            if (response.errorBody() == null) return null;
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new MessageResponse();
        }
        if (error == null || (error.getMessage() == null && error.getMessageLong() == null))
            return null;
        Log.d(TAG, error.toString());
        return error;
    }
}
