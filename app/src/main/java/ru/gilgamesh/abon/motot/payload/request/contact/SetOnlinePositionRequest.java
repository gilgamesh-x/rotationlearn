package ru.gilgamesh.abon.motot.payload.request.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SetOnlinePositionRequest {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String userLocale;
    private String firebaseToken;
    private String appVersion;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
