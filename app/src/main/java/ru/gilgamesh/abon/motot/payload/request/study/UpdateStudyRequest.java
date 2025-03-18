package ru.gilgamesh.abon.motot.payload.request.study;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateStudyRequest {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String urlProfile;
    private String phoneCountry;
    private String phoneNumber;
    private String description;
    private Integer cost;
    private Boolean categoryAFlg;
    private Boolean escortRoadFlg;
    private Boolean improvingSkillFlg;
    private Boolean emergencyCoursesFlg;
    private Boolean motocrossFlg;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
