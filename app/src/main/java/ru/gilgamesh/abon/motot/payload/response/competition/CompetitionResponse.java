package ru.gilgamesh.abon.motot.payload.response.competition;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter
@Getter
public class CompetitionResponse {
    private Long id;
    private String createdDate;
    private String name;
    private String description;
    private ImageDataResponse competitionImage;
    private String type;
    private Boolean needPayFlg;
    private Integer cost;
    private String currencyCode;
    private Integer maxCountMember;
    private Integer countMember;
    private String startDate;
    private Double latitude;
    private Double longitude;
    private Long contactId;
    private String nickName;
    private Boolean activeFlg;
    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
