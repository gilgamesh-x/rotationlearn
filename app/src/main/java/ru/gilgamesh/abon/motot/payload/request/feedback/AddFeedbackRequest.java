package ru.gilgamesh.abon.motot.payload.request.feedback;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter@Getter@AllArgsConstructor
public class AddFeedbackRequest {
    private String text;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
