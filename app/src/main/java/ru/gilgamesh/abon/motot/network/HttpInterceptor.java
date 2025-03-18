package ru.gilgamesh.abon.motot.network;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.gilgamesh.abon.motot.model.App;

@Deprecated
public class HttpInterceptor implements Interceptor {
    private static final String TAG = HttpInterceptor.class.getSimpleName();
    private static final String AUTH_STR = "Authorization";
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();
        synchronized (this) {
            NetworkRefreshToken.refreshIfNeededToken();
        }
        Request request = original.newBuilder().header(AUTH_STR, App.loginInfo.getType() + " " + App.loginInfo.getToken()).addHeader("Connection", "close").build();
        Response response = chain.proceed(request);

        if (response.code() == 401) {
            Log.d(TAG, "intercept: 401");
        }
        return response;
    }
}
