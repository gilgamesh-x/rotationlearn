package ru.gilgamesh.abon.motot.payload.response.towTruck;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TowTruckShortResponse {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String marker;
    private Boolean workWeekendsFlg;
    private Boolean aroundClockFlg;
    private Boolean activeFlg;
    private Long userId;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
