package ru.gilgamesh.abon.motot.payload.request.complain;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class AddComplainRequest {
    private String type;
    private String text;
    private String typeObject;
    private Long idObject;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
