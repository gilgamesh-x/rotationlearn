package ru.gilgamesh.abon.motot.payload.response.notification;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;
@Setter@Getter
public class PageableNotification extends PageResponse {
    List<NotificationItemApi> content;
}
