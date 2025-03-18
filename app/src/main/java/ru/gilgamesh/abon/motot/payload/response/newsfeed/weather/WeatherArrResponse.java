package ru.gilgamesh.abon.motot.payload.response.newsfeed.weather;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class WeatherArrResponse {
    private Long id;
    private String main;
    private String description;
    private String icon;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
