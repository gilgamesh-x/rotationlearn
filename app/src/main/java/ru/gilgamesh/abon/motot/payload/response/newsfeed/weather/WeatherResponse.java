package ru.gilgamesh.abon.motot.payload.response.newsfeed.weather;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherResponse {
    private Long id;
    private String type;
    private List<WeatherArrResponse> weather;
    private WeatherMainResponse main;
    private WeatherWindResponse wind;
    private WeatherRainResponse rain;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
