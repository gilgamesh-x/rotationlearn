package ru.gilgamesh.abon.motot.payload.request.auth;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter@Getter@NoArgsConstructor
public class LoginWORRequest {
    private String firebaseTokenInstall;

    private String userLocale;

    private String username;

    private String password;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
