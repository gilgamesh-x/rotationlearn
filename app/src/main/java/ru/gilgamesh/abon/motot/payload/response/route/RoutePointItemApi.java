package ru.gilgamesh.abon.motot.payload.response.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter @Getter
public class RoutePointItemApi {
    private Long id;
    private String name;
    private String description;
    private ImageDataResponse routePointImage;
    private Double latitude;
    private Double longitude;
    private int sequence;
    private Boolean isFinishedPoint;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
