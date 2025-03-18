package ru.gilgamesh.abon.motot.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;
import ru.gilgamesh.abon.motot.ui.profile.LoginActivity;

@Deprecated
@Setter
@Getter
public class CustomGetRequest {
    private final String PREF_AUTH_TYPE = "type";
    private final String PREF_AUTH_TOKEN = "token";
    private final String host = BuildConfig.HOST_MICRO_URL;
    private String urlAfterHost;
    private String urlParams;
    private SharedPreferences setting;
    private String errorMsg = "";
    private boolean isArrayResp = false;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private Context context;
    StringBuilder resultmsg;
    public CustomGetRequest(String url) {
        this.urlAfterHost = url;
        this.urlParams = "";
        this.resultmsg = new StringBuilder();
    }

    public void clear() {
        this.setting = null;
        this.jsonArray = null;
        this.jsonObject = null;
        this.context = null;
        this.resultmsg = null;
    }

    public CustomGetRequest(String url, SharedPreferences setting) {
        this.urlAfterHost = url;
        this.setting = setting;
        this.urlParams = "";
        this.resultmsg = new StringBuilder();
    }

    public void sendGetRequest(boolean needAuth) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        resultmsg = new StringBuilder();
        URL url;
        try {
            url = new URL(this.host + this.urlAfterHost + this.urlParams);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        HttpURLConnection client = null;
        try {
            client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("GET");
            if (needAuth) {
                if (App.loginInfo == null || App.loginInfo.getType() == null || App.loginInfo.getToken() == null) {
                    Log.e("sendGetRequest", "App login info empty, use storage");
                    App.loginInfo = new Gson().fromJson(this.setting.getString("jsonLogin", ""), SigninResponse.class);
                    if (App.loginInfo == null) {
                        Log.e("sendGetRequest", "Storage empty");
                        return ;
                    }
                }
                NetworkRefreshToken.userInfo = this.setting;
                NetworkRefreshToken.refreshIfNeededToken();
                client.setRequestProperty("Authorization", App.loginInfo.getType() + " " + App.loginInfo.getToken());
            }
            client.setUseCaches(false);
            Log.i("STATUS" + this.host + this.urlAfterHost, String.valueOf(client.getResponseCode()));
            Log.i("MSG", client.getResponseMessage());
            if (client.getResponseCode() == HttpURLConnection.HTTP_OK) {
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
            } else if (client.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                this.errorMsg = App.getAppResources().getString(R.string.http401);
                if (context != null) {
                    Intent loginIntent = new Intent(context, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(loginIntent);
                }
            } else {
                this.errorMsg = App.getAppResources().getString(R.string.httpall);
            }

            if ((resultmsg.length() > 0) && this.errorMsg.isEmpty()) {
                if (this.isArrayResp) {
                    this.jsonArray = new JSONArray(resultmsg.toString());
                } else {
                    this.jsonObject = new JSONObject(resultmsg.toString());
                }
            }
        } catch (MalformedURLException error) {
            Log.e("MalformedURLException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (SocketTimeoutException error) {
            Log.e("SocketTimeoutException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (IOException error) {
            Log.e("IOException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } catch (JSONException error) {
            Log.e("IOException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } finally {
            if (client != null) { // Make sure the connection is not null.
                client.disconnect();
                client = null;
            }
        }
    }
}
