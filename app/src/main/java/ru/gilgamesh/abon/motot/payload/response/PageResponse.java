package ru.gilgamesh.abon.motot.payload.response;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class PageResponse {
    //private String pageable;
    public Boolean last;
    private Integer totalPages;
    private Integer totalElements;
    private Integer size;
    private Integer number;
    private Boolean first;
    private Integer numberOfElements;
    private Boolean empty;
}
