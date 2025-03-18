package ru.gilgamesh.abon.motot.payload.response.chat;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatResponse {
    private Long id;
    private String createdDate;
    private String name;
    private String type;
    private String imageData;
    private String sex;
    private String companionNick;
    private String lastMsg;
    private String lastMsgDt;
    private Long countUnreadMsg;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
