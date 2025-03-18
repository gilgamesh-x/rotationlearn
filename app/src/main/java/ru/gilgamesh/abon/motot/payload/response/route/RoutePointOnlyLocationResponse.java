package ru.gilgamesh.abon.motot.payload.response.route;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class RoutePointOnlyLocationResponse {
    private Long id;
    private Integer sequence;
    private Double latitude;
    private Double longitude;
}
