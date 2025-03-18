package ru.gilgamesh.abon.motot.payload.request.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactFCMRequest {
    private String firebaseToken;

    public ContactFCMRequest(String token) {
        this.firebaseToken = token;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

