package ru.gilgamesh.abon.motot.payload.response.notification;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationItemApi {
    private Long id;
    private String createdDate;
    private String fromService;
    private String title;
    private String body;
    private Boolean readFlg;
    private Long idObject;
    //private JSONObject jsonObject;

    public NotificationItemApi(JSONObject jsonObject) {
        //this.jsonObject = jsonObject;

        try {
            if (jsonObject.has("id") && !jsonObject.isNull("id")) {
                this.id = jsonObject.getLong("id");
            }
            if (jsonObject.has("createdDate") && !jsonObject.isNull("createdDate")) {
                this.createdDate = jsonObject.getString("createdDate");
            }
            if (jsonObject.has("fromService") && !jsonObject.isNull("fromService")) {
                this.fromService = jsonObject.getString("fromService");
            }
            if (jsonObject.has("title") && !jsonObject.isNull("title")) {
                this.title = jsonObject.getString("title");
            }
            if (jsonObject.has("body") && !jsonObject.isNull("body")) {
                this.body = jsonObject.getString("body");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
