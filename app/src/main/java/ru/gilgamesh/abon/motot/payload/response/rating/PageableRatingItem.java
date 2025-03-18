package ru.gilgamesh.abon.motot.payload.response.rating;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RecycleViewRating.RatingItem;

@Setter
@Getter
public class PageableRatingItem extends PageResponse {
    private List<RatingItem> content;
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}