package ru.gilgamesh.abon.motot.payload.request.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompetitionPlaceListRequest {
    private Long memberId;
    private Integer place;

    public CompetitionPlaceListRequest(Long memberId, Integer place) {
        this.memberId = memberId;
        this.place = place;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
