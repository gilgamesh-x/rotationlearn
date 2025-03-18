package ru.gilgamesh.abon.motot.payload.request.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompetitionFinishRequest {
    private Long competitionId;
    private List<CompetitionPlaceListRequest> placeList;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
