package ru.gilgamesh.abon.motot.payload.request.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RouteSaveRequest {
    private Long id;
    private String name;
    private String description;
    private String type;
    private Integer distance;
    private String imageData;
    private String routeStyle;
    private List<RoutePointRequest> points;
    private Double latitude;
    private Double longitude;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
