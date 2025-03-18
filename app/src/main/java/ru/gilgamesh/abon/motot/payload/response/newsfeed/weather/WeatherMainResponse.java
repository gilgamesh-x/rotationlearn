package ru.gilgamesh.abon.motot.payload.response.newsfeed.weather;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class WeatherMainResponse {
    private Double temp;
    private Double feels_like;
    private Double temp_min;
    private Double temp_max;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
