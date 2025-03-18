package ru.gilgamesh.abon.motot.payload.request.group;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class GroupUpdateBaseRequest {
    private Long groupId;
    private Double latitude;
    private Double longitude;
    private String meetDate;

    public GroupUpdateBaseRequest (Long groupId, Double latitude, Double longitude) {
        this.groupId = groupId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public GroupUpdateBaseRequest (Long groupId, Double latitude, Double longitude, String meetDate) {
        this.groupId = groupId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.meetDate = meetDate;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
