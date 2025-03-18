package ru.gilgamesh.abon.motot.payload.response.chat;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatMessageResponse {
    private Long id;
    private String createdDate;
    private Long chatId;
    private Long memberId;
    private Long contactId;
    private String msg;
    private boolean sender;
    private Boolean readFlg;
    private Long replayId;
    private String replayMsg;
    private Boolean replayIsSender;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
