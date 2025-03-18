package ru.gilgamesh.abon.motot.payload.request.auth;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class SignupRequest {
    private String username;

    private String email;

    private String password;

    private String nickname;

    private String birtDate;

    private String sex;

    private String motoBrand;

    private String motoModel;

    private String userLocale;

    private Boolean isWantFindPair;

    private String firebaseToken;

    private String firebaseTokenInstall;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
