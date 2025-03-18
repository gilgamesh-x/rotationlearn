package ru.gilgamesh.abon.motot.payload.request.towTruck;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTowTruckRequest {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String urlProfile;
    private String towTruckNumber;
    private String phoneCountry;
    private String phoneNumber;
    private String description;
    private Boolean workWeekendsFlg;
    private Boolean aroundClockFlg;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
