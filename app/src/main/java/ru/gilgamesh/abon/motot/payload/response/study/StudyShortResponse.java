package ru.gilgamesh.abon.motot.payload.response.study;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StudyShortResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String marker;
    private Boolean activeFlg;
    private Boolean categoryAFlg;
    private Boolean escortRoadFlg;
    private Boolean improvingSkillFlg;
    private Boolean emergencyCoursesFlg;
    private Boolean motocrossFlg;
    private Long userId;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
