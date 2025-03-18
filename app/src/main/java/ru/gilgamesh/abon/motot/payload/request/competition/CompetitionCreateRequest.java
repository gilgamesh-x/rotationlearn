package ru.gilgamesh.abon.motot.payload.request.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompetitionCreateRequest {
    private String name;
    private String description;
    private String type;
    private Boolean needPayFlg;
    private Integer cost;
    private String currencyCode;
    private Integer maxCountMember;
    private String imageData;
    private String startDate;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
