package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter@Getter@NoArgsConstructor
public class RatingItem {
    public Long contactId;
    public String contactName;
    public String motorcycleName;
    public Integer distance;
    public Integer place;
    public ImageDataResponse image;
    public String sex;
}
