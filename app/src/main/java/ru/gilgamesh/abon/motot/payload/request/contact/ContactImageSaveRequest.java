package ru.gilgamesh.abon.motot.payload.request.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ContactImageSaveRequest {
    private String imageData;

    private String typeImg;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

