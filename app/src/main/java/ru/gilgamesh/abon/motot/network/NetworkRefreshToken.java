package ru.gilgamesh.abon.motot.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.response.auth.RefreshTokenResponse;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;

@Deprecated
public class NetworkRefreshToken {
    private static final String TAG = NetworkRefreshToken.class.getSimpleName();
    public static SharedPreferences userInfo;

    public static boolean refreshIfNeededToken() {
        SharedPreferences _userInfo = null;// = App.contextApp.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        if (_userInfo == null) {
            _userInfo = userInfo;
        }
        if (App.loginInfo == null) {
            Log.e(TAG, "App login info empty");
            if (userInfo != null) {
                App.loginInfo = new Gson().fromJson(_userInfo.getString("jsonLogin", ""), SigninResponse.class);
                if (App.loginInfo == null) {
                    Log.e(TAG, "Storage empty");
                    return false;
                }
            } else {
                Log.e(TAG, "SharedPreferences is null");
                return false;
            }
        }
        final Long tokenDate = App.loginInfo.getDateLoginLong();
        if (tokenDate == null || tokenDate.equals(0L)) {
            Log.d(TAG, "refreshIfNeededToken: tokenDate is null");
            return false;
        }
        final String refreshToken = App.loginInfo.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            Log.d(TAG, "refreshIfNeededToken: refreshToken is null");
            return false;
        }

        Date dateLogin = new Date(tokenDate);
        Date today = new Date();
        long different = (today.getTime() - dateLogin.getTime());
        if (App.loginInfo.getTokenDurationMs() == null || different >= App.loginInfo.getTokenDurationMs() - 10000) {
            return refreshToken().isEmpty();
        }
        return true;
    }

    public static boolean refreshIfNeededToken(Context context) {
        SharedPreferences _userInfo = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        if (App.loginInfo == null) {
            Log.e(TAG, "App login info empty");
            App.loginInfo = new Gson().fromJson(_userInfo.getString("jsonLogin", ""), SigninResponse.class);
            if (App.loginInfo == null) {
                Log.e(TAG, "Storage empty");
                return false;
            }
        }
        final Long tokenDate = App.loginInfo.getDateLoginLong();
        if (tokenDate == null || tokenDate.equals(0L)) return false;
        final String refreshToken = App.loginInfo.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) return false;

        Date dateLogin = new Date(tokenDate);
        Date today = new Date();
        long different = (today.getTime() - dateLogin.getTime());
        if (App.loginInfo.getTokenDurationMs() == null || different >= App.loginInfo.getTokenDurationMs() - 10000) {
            return refreshToken().isEmpty();
        }
        return true;
    }

    private static String refreshToken() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (App.loginInfo == null || App.loginInfo.getType() == null || App.loginInfo.getToken() == null) {
            Log.e(TAG, "App login info empty");
            App.loginInfo = new Gson().fromJson(userInfo.getString("jsonLogin", ""), SigninResponse.class);
            if (App.loginInfo == null) {
                Log.e(TAG, "Storage empty");
                return "Storage empty";
            }
        }
        Log.d(TAG, "refreshToken");
        String errorMsg = "";
        StringBuilder resultMsg;
        final String urlRefresh = "api/auth/refreshtoken";
        URL url;
        try {
            url = new URL(BuildConfig.HOST_MICRO_URL + urlRefresh);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            client.setRequestProperty("Authorization", App.loginInfo.getType() + " " + App.loginInfo.getToken());
            client.setUseCaches(false);
            client.setDoOutput(true);

            JSONObject refreshRequest = new JSONObject();
            refreshRequest.put("refreshToken", App.loginInfo.getRefreshToken());
            Log.d(TAG, refreshRequest.toString());
            client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
            os.write(refreshRequest.toString());
            os.flush();
            os.close();

            resultMsg = new StringBuilder();
            if (client.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        resultMsg.append(line);
                    }
                    Log.d("BufferedReader", resultMsg.toString());
                } finally {
                    bufferedReader.close();
                    bufferedReader = null;
                    inputStreamReader.close();
                    inputStreamReader = null;
                }
            } else {
                errorMsg = App.getAppResources().getString(R.string.httpall);
            }
            Log.d(TAG, resultMsg.toString());
            if (resultMsg.length() > 0 && errorMsg.isEmpty()) {
                RefreshTokenResponse refreshTokenResponse = new Gson().fromJson(resultMsg.toString(), RefreshTokenResponse.class);
                if (App.loginInfo == null) App.loginInfo = new SigninResponse();
                //App.loginInfo.setAllData(refreshTokenResponse);
            }
        } catch (MalformedURLException error) {
            Log.e("MalformedURLException", error.toString());
            errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (SocketTimeoutException error) {
            Log.e("SocketTimeoutException", error.toString());
            errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (IOException error) {
            Log.e("IOException", error.toString());
            errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (JSONException error) {
            Log.e("IOException", error.toString());
            errorMsg = App.getAppResources().getString(R.string.httpall);
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }
        return errorMsg;
    }
}
