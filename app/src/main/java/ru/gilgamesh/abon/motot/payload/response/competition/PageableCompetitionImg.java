package ru.gilgamesh.abon.motot.payload.response.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;

@Setter
@Getter
public class PageableCompetitionImg extends PageResponse {
    private List<CompetitionImgResponse> content;
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}