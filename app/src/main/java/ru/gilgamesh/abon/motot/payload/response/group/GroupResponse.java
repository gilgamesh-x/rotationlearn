package ru.gilgamesh.abon.motot.payload.response.group;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class GroupResponse {
    private Long id;

    private Long contactId;

    private String name;

    private String description;

    private Double latitude;

    private Double longitude;

    private Integer memberCount;

    private Integer memberActiveCount;

    private Boolean invitingFlg;

    private String inviteNickName;

    private Long inviteContactId;

    private Long imageId;

    private Long routeId;

    private String meetDate;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
