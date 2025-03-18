package ru.gilgamesh.abon.motot.payload.request.chat;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ReadMsgRequest {
    private Long chatId;
    private Long msgId;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
