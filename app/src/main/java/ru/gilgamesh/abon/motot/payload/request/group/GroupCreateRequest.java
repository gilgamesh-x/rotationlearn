package ru.gilgamesh.abon.motot.payload.request.group;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @NoArgsConstructor
public class GroupCreateRequest {
    private String name;
    private String description;
    private String imageData;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
