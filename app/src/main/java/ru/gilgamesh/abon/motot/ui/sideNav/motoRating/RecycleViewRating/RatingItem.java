package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RecycleViewRating;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter@Getter@NoArgsConstructor
public class RatingItem {
    private Long contactId;
    private String contactName;
    private String motorcycleName;
    private Integer distance;
    private Integer place;
    private ImageDataResponse image;
    private String sex;
}
