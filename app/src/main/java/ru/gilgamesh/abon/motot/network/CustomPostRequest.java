package ru.gilgamesh.abon.motot.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.ui.profile.LoginActivity;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;

@Deprecated
@Setter@Getter
public class CustomPostRequest {
    private final String PREF_AUTH_TYPE = "type";
    private final String PREF_AUTH_TOKEN = "token";

    private final String host = BuildConfig.HOST_MICRO_URL;
    private String urlAfterHost;
    private SharedPreferences setting;
    private JSONObject jsonObject;
    private String errorMsg = "";
    private int errorCode = 0;
    private boolean isArrayResp = false;
    private JSONArray jsonArray;
    private Context context;
    private int connectTimeout = 0;
    private StringBuilder resultmsg;
    private String method;

    public CustomPostRequest(String url) {
        this.urlAfterHost = url;
        this.resultmsg = new StringBuilder();
    }

    public CustomPostRequest(String url, SharedPreferences setting) {
        this.urlAfterHost = url;
        this.setting = setting;
        this.resultmsg = new StringBuilder();
    }

    public void clear() {
        this.setting = null;
        this.jsonArray = null;
        this.jsonObject = null;
        this.context = null;
        this.resultmsg = null;
    }

    public JSONObject sendPostRequest(boolean needAuth) {
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
                    App.loginInfo = new Gson().fromJson(this.setting.getString("jsonLogin", ""), SigninResponse.class);
                    if (App.loginInfo == null) {
                        Log.e("sendGetRequest", "Storage empty");
                        return new JSONObject();
                    }
                }
                NetworkRefreshToken.userInfo = this.setting;
                NetworkRefreshToken.refreshIfNeededToken();
                client.setRequestProperty("Authorization", App.loginInfo.getType() + " " + App.loginInfo.getToken());
            }
            client.setUseCaches(false);
            if (this.jsonObject != null) {
                client.setDoOutput(true);
                client.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                Log.i("JSON", this.jsonObject.toString());
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));
                os.write(this.jsonObject.toString());
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
                    if (resultmsg.length() > 0) {
                        this.jsonObject = new JSONObject(resultmsg.toString());
                    }
                }
            } else {
                this.errorMsg = App.getAppResources().getString(R.string.httpall);
            }

            if (resultmsg.length() > 0 && this.errorMsg.isEmpty()) {
                if (this.isArrayResp) {
                    this.jsonArray = new JSONArray(resultmsg.toString());
                } else {
                    this.jsonObject = new JSONObject(resultmsg.toString());
                }
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
        } catch (JSONException error) {
            Log.e("IOException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return this.jsonObject;
    }

    public JSONObject sendFilePostRequest(boolean needAuth, Uri path) {
        File file = new File(path.getPath());
        try {
            return sendFilePostRequestByte(needAuth, FileUtils.readFileToByteArray(file), file.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject sendFilePostRequest(boolean needAuth, byte[] bytesFile, String nameFile) {
        return sendFilePostRequestByte(needAuth, bytesFile, nameFile);
    }

    public JSONObject sendFilePostRequestByte(boolean needAuth, byte[] bytesFile, String nameFile) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
            client.setRequestMethod("POST");
            if (needAuth) {
                if (App.loginInfo == null || App.loginInfo.getType() == null || App.loginInfo.getToken() == null) {
                    Log.e("sendGetRequest", "App login info empty");
                    App.loginInfo = new Gson().fromJson(this.setting.getString("jsonLogin", ""), SigninResponse.class);
                    if (App.loginInfo == null) {
                        Log.e("sendGetRequest", "Storage empty");
                        return new JSONObject();
                    }
                }
                NetworkRefreshToken.userInfo = this.setting;
                NetworkRefreshToken.refreshIfNeededToken();
                client.setRequestProperty("Authorization", App.loginInfo.getType() + " " + App.loginInfo.getToken());
            }

            client.setUseCaches(false);
            client.setDoOutput(true);

            client.setRequestProperty("Connection", "Keep-Alive");
            client.setRequestProperty("Cache-Control", "no-cache");
            String boundary = UUID.randomUUID().toString();
            client.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream request = new DataOutputStream(client.getOutputStream());

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"data\"\r\n\r\n");
            request.writeBytes("data" + "\r\n");

            request.writeBytes("--" + boundary + "\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"data\"; filename=\"" + nameFile + "\"\r\n\r\n");
            request.write(bytesFile);
            request.writeBytes("\r\n");
            request.writeBytes("--" + boundary + "\r\n");
            Log.d("json", jsonObject.toString());
            if (this.jsonObject != null) {
                Iterator<String> keys = jsonObject.keys();
                while(keys.hasNext()) {
                    String key = keys.next();
                    Log.d("json1", key);
                    request.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"\r\n");
                    request.writeBytes("Content-Type: text/plain" + "\r\n");
                    request.writeBytes("\r\n");
                    request.writeBytes((jsonObject.get(key)).toString());
                    request.writeBytes("\r\n");

                    request.writeBytes("--" + boundary + "--\r\n");
                }
            }
            request.flush();

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
                    Intent loginIntent = new Intent(context, LoginActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(loginIntent);
                }
            } else {
                this.errorMsg = App.getAppResources().getString(R.string.httpall);
            }

            if (resultmsg.length() > 0) {
                this.jsonObject = new JSONObject(resultmsg.toString());
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
        } catch (JSONException error) {
            Log.e("IOException", error.toString());
            this.errorMsg = App.getAppResources().getString(R.string.httpall);
        } finally {
            if (client != null) // Make sure the connection is not null.
                client.disconnect();
        }

        return this.jsonObject;
    }
}
