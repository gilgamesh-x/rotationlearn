package ru.gilgamesh.abon.motot.payload.request.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompetitionUpdateLocationRequest {
    private Long id;
    private Double latitude;
    private Double longitude;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
