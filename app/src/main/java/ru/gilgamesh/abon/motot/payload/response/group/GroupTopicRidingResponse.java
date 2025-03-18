package ru.gilgamesh.abon.motot.payload.response.group;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class GroupTopicRidingResponse {
    private Long contactId;
    private Long groupId;
    private Double latitude;
    private Double longitude;

    public boolean fillAllData() {
        return this.contactId != null && this.groupId != null && this.latitude != null && this.longitude != null;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
