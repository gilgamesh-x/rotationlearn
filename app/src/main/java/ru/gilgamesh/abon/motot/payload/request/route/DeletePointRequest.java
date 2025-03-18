package ru.gilgamesh.abon.motot.payload.request.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class DeletePointRequest {
    private Long routeId;

    private Long id;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
