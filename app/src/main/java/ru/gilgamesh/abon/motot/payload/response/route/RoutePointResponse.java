package ru.gilgamesh.abon.motot.payload.response.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoutePointResponse {
    private Long id;
    private String name;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    private String description;
    private Long imageId;
    private Integer sequence;
    private Double latitude;
    private Double longitude;
    private Boolean isFinishedPoint;
}
