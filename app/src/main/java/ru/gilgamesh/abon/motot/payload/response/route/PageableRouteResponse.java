package ru.gilgamesh.abon.motot.payload.response.route;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;

@Setter@Getter
public class PageableRouteResponse extends PageResponse {
    List<RouteItemApi> content;
}
