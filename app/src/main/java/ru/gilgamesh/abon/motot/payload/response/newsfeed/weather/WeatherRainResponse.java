package ru.gilgamesh.abon.motot.payload.response.newsfeed.weather;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WeatherRainResponse {
    @SerializedName("1h")
    @Expose
    private Double oneHour;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
