package ru.gilgamesh.abon.motot.payload;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class CounterResponse {
    private Long count;
    private Integer countInteger;
}
