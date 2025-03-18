package ru.gilgamesh.abon.motot.payload.request;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class IdentifierRequest {
    private Long id;

    public IdentifierRequest(Long id) {
        this.id = id;
    }
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
