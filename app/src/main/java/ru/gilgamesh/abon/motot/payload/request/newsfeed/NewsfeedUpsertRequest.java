package ru.gilgamesh.abon.motot.payload.request.newsfeed;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class NewsfeedUpsertRequest {
    private Long id;
    private String text;
    private String image;
}
