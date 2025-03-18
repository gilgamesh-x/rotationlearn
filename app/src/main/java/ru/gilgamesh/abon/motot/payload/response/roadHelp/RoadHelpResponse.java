package ru.gilgamesh.abon.motot.payload.response.roadHelp;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi;

@Setter@Getter
public class RoadHelpResponse {
    private Long id;
    private String createdDate;
    private UserInfoApi contact;
    private List<UserInfoApi> helpers;
    private Boolean activeFlg;
    private String reason;
    private String comment;
    private String phoneCountry;
    private String phone;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
