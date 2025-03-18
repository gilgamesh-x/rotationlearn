package ru.gilgamesh.abon.motot.payload.response.chat;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class ChatMessageApiResponse {
    private String msg;
    private boolean sender;
    private String createdDate;

    private Long contactId;

    public ChatMessageApiResponse(JSONObject jsonObject) {
        try {
            final String msgTag = "msg";
            if (jsonObject.has(msgTag) && !jsonObject.isNull(msgTag)) {
                this.msg = jsonObject.getString(msgTag);
            }
            final String createdDateTag = "createdDate";
            if (jsonObject.has(createdDateTag) && !jsonObject.isNull(createdDateTag)) {
                this.createdDate = jsonObject.getString(createdDateTag);
            }
            final String senderTag = "sender";
            if (jsonObject.has(senderTag) && !jsonObject.isNull(senderTag)) {
                this.sender = jsonObject.getBoolean(senderTag);
            }
            final String contactIdTag = "contactId";
            if (jsonObject.has(contactIdTag) && !jsonObject.isNull(contactIdTag)) {
                this.contactId = jsonObject.getLong(contactIdTag);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
