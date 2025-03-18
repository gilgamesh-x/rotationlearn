package ru.gilgamesh.abon.motot.payload.response.study;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.contact.ContactShortResponse;

@Setter@Getter
public class StudyResponse {
    private Long id;
    private String description;
    private String urlProfile;
    private String phoneCountry;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
    private Integer cost;
    private Boolean verifiedFlg;
    private Boolean categoryAFlg;
    private Boolean escortRoadFlg;
    private Boolean improvingSkillFlg;
    private Boolean emergencyCoursesFlg;
    private Boolean motocrossFlg;
    private Long userId;
    private Boolean creatorFlg;
    private ContactShortResponse contact;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
