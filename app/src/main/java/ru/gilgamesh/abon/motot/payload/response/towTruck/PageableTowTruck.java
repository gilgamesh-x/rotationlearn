package ru.gilgamesh.abon.motot.payload.response.towTruck;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;
@Setter@Getter
public class PageableTowTruck extends PageResponse {
    List<TowTruckShortResponse> content;
}
