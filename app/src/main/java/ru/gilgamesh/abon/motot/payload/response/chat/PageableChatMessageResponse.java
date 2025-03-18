package ru.gilgamesh.abon.motot.payload.response.chat;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;
@Setter@Getter
public class PageableChatMessageResponse extends PageResponse {
    List<ChatMessageResponse> content;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
