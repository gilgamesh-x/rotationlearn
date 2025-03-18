package ru.gilgamesh.abon.motot.payload.response;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CodeResponse {
    private String codeText;
    private Integer codeInt;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}