package ru.gilgamesh.abon.motot.payload.request.auth;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter@AllArgsConstructor
public class LoginRequest {
    private String username;

    private String password;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
