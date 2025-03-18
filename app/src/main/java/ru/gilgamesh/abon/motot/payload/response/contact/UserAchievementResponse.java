package ru.gilgamesh.abon.motot.payload.response.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class UserAchievementResponse implements Cloneable {
    private Boolean registrationFlg;
    private Boolean routeCreate1Flg;
    private Boolean routeCreate2Flg;
    private Boolean routeCreate3Flg;
    private Boolean subscription1Flg;
    private Boolean subscription2Flg;
    private Boolean subscription3Flg;
    private Boolean km1Flg;
    private Boolean km2Flg;
    private Boolean km3Flg;
    private Boolean event1Flg;
    private Boolean event2Flg;
    private Boolean event3Flg;

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
