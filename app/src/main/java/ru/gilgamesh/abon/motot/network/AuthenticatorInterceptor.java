package ru.gilgamesh.abon.motot.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.data.api.AuthApi;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.request.auth.TokenRefreshRequest;
import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse;

@Deprecated
public class AuthenticatorInterceptor implements Authenticator {
    private static final String BASE_URL = BuildConfig.HOST_MICRO_URL;
    private static final String AUTH_STR = "Authorization";
    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NonNull Response response) throws IOException {
        if (response.code() == 401) {
            synchronized (this) {
                Retrofit.Builder buildeR = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofitR;
                OkHttpClient.Builder httpClientR = new OkHttpClient.Builder();
                httpClientR.addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder().header(AUTH_STR, App.loginInfo.getType() + " " + App.loginInfo.getToken()).addHeader("Connection", "close").build();
                    return chain.proceed(request);
                });
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor loggingR = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
                    httpClientR.addInterceptor(loggingR);
                }
                buildeR.client(httpClientR.build());
                retrofitR = buildeR.build();
                AuthApi authApi = retrofitR.create(AuthApi.class);
                Call<RefreshTokenResponse> responseCall = authApi.refreshAccessToken(new TokenRefreshRequest(App.loginInfo.getRefreshToken()));
                RefreshTokenResponse refreshTokenResponse = null;
                try {
                    retrofit2.Response<RefreshTokenResponse> tokenResponseResponse = responseCall.execute();
                    if (tokenResponseResponse.isSuccessful()) {
                        refreshTokenResponse = tokenResponseResponse.body();
                        //App.loginInfo.setAllData(refreshTokenResponse);
                        return response.request().newBuilder().header(AUTH_STR, App.loginInfo.getType() + " " + App.loginInfo.getToken()).addHeader("Connection", "close").build();
                    }
                    return null;
                } catch (Exception ex) {
                    return null;
                }
            }
        }
        return null;
    }
}
