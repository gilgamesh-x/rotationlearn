package ru.gilgamesh.abon.motot.payload.response.towTruck;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.contact.ContactShortResponse;

@Setter@Getter
public class TowTruckResponse {
    private Long id;
    private String description;
    private String urlProfile;
    private String towTruckNumber;
    private String phoneCountry;
    private String phoneNumber;
    private Boolean workWeekendsFlg;
    private Boolean aroundClockFlg;
    private Boolean verifiedFlg;
    private Long userId;
    private Boolean creatorFlg;
    private ContactShortResponse contact;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
