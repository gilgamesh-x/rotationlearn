package ru.gilgamesh.abon.motot.payload.response.group;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class GroupMemberResponse {
    private Long contactId;
    private String nickName;
    private String sex;
    private Boolean invitingFlg;
    private Boolean goingFlg;
    private Double latitude;
    private Double longitude;
    private Long imageId;
    private String motorcycle;
    private String role;
    private Boolean muteFlg;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
