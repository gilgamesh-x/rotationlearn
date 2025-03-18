package ru.gilgamesh.abon.motot.payload.response.newsfeed.weather;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class WeatherWindResponse {
    private Double speed;
    private Double deg;
    private Double gust;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
