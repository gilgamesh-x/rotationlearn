package ru.gilgamesh.abon.motot.payload.request.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter@AllArgsConstructor
public class UpdateHelperInfoRequest {
    private Boolean imRoadHelper;
    private Integer radiusRoadHelper;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
