package ru.gilgamesh.abon.motot.payload.response.group;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;

@Setter@Getter
public class PageableGroupResponse extends PageResponse {
    List<GroupResponse> content;
}
