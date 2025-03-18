package ru.gilgamesh.abon.motot.payload.request.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactInfoUpdateRequest {
    private String nickName;
    private String birtDate;
    private String about;
    private String aboutPair;
    private String firstName;
    private Long id;
    private String sex;
    private String userLocale;
    private String firebaseToken;
    private String motoBrand;
    private String motoModel;
    private Boolean isWantFindPair;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
