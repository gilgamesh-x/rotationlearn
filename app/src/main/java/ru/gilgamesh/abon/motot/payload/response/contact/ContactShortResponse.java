package ru.gilgamesh.abon.motot.payload.response.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactShortResponse {
    private Long id;
    private String nickName;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
