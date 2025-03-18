package ru.gilgamesh.abon.motot.payload.response;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class MessageResponse {
    private String message;
    private Long messageLong;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
