package ru.gilgamesh.abon.motot.payload.request.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gilgamesh.abon.motot.ui.bottomNav.route.RVRoutePoint.ItemRoutePoint;

@Setter@Getter@NoArgsConstructor
public class RoutePointRequest {
    private Long routeId;
    private Long id;
    private String marker;
    private String name;
    private String description;
    private Integer sequence;
    private Double latitude;
    private Double longitude;
    private String imageData;

    public RoutePointRequest (ItemRoutePoint item) {
        this.routeId = item.getRouteId();
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.sequence = item.getSequence();
        this.latitude = item.getLatitude();
        this.longitude = item.getLongitude();
        this.imageData = item.getImageData();
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
