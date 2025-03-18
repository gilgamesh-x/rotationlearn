package ru.gilgamesh.abon.motot.payload.response.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter@Getter
public class CompetitionMemberResponse {
    private Long id;
    private Long contactId;
    private String name;
    private String nickName;
    private Integer number;
    private Integer place;
    private ImageDataResponse image;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
