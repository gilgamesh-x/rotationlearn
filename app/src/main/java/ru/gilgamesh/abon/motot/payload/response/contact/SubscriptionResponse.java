package ru.gilgamesh.abon.motot.payload.response.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter@Getter
public class SubscriptionResponse {
    private Long id;
    private String nickName;
    private String sex;
    private ImageDataResponse avatar;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
