package ru.gilgamesh.abon.motot.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;

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

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.ui.profile.LoginActivity;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;

@Deprecated
@Setter@Getter
public class NetworkPostRequest {
    private final String PREF_AUTH_TYPE = "type";
    private final String PREF_AUTH_TOKEN = "token";

    private final String host = BuildConfig.HOST_MICRO_URL;
    private String urlAfterHost, requestBody;
    private String errorMsg = "";
    private int errorCode = 0;
    private Context context;
    private int connectTimeout = 0;
    private StringBuilder resultmsg;
    private String method;

    public NetworkPostRequest(String url) {
        this.urlAfterHost = url;
        this.resultmsg = new StringBuilder();
    }

    public void clear() {
        this.context = null;
        this.resultmsg = null;
    }

    public String sendPostRequest(boolean needAuth, SharedPreferences setting) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        resultmsg = new StringBuilder();
        URL url = null;
        try {
            url = new URL(this.host + this.urlAfterHost);
        } catch (
                MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            if (method != null) {
                client.setRequestMethod(method);
            } else {
                client.setRequestMethod("POST");
            }
            if (connectTimeout > 0) {
                client.setConnectTimeout(connectTimeout);
            }
            if (needAuth) {
                if (App.loginInfo == null || App.loginInfo.getType() == null || App.loginInfo.getToken() == null) {
                    Log.e("sendGetRequest", "App login info empty");
                    App.loginInfo = new Gson().fromJson(setting.getString("jsonLogin", ""), SigninResponse.class);
                    if (App.loginInfo == null) {
                        Log.e("sendGetRequest", "Storage empty");
                        return "";
                    }
                }
                NetworkRefreshToken.userInfo = setting;
                NetworkRefreshToken.refreshIfNeededToken();
                client.setRequestProperty("Authorization", App.loginInfo.getType() + " " + App.loginInfo.getToken());
            }
            client.setUseCaches(false);
            if (this.requestBody != null) {
                client.setDoOutput(true);
                client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                Log.i("JSON", this.requestBody);
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
                os.write(this.requestBody);
                os.flush();
                os.close();
                os = null;
            }
            errorCode = client.getResponseCode();
            Log.i("STATUS" + this.host + this.urlAfterHost, String.valueOf(errorCode));
            Log.i("MSG", client.getResponseMessage());

            if (errorCode == HttpURLConnection.HTTP_OK) {
                InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                try {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        resultmsg.append(line);
                    }
                    Log.d("BufferedReader", resultmsg.toString());
                } finally {
                    bufferedReader.close();
                    bufferedReader = null;
                    inputStreamReader.close();
                    inputStreamReader = null;
                }
            } else if (errorCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                this.errorMsg = App.getAppResources().getString(R.string.http401);
                if (context != null) {
                    if (context != null) {
                        Intent loginIntent = new Intent(context, LoginActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(loginIntent);
                    }
                }
                return null;
            } else if (errorCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getErrorStream()))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        resultmsg.append(line);
                    }
                    Log.d("BufferedReader", resultmsg.toString());
                    this.errorMsg = App.getAppResources().getString(R.string.httpall);
                }
            } else {
                this.errorMsg = App.getAppResources().getString(R.string.httpall);
            }
        } catch (MalformedURLException error) {
            Log.e("MalformedURLException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (
                SocketTimeoutException error) {
            Log.e("SocketTimeoutException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (
                IOException error) {
            Log.e("IOException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return this.resultmsg.toString();
    }

}
