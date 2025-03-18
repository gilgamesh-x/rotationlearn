package ru.gilgamesh.abon.motot.payload.response.study;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.response.PageResponse;

@Setter@Getter
public class PageableStudy extends PageResponse {
    List<StudyShortResponse> content;
}
