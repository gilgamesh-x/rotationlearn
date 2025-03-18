package ru.gilgamesh.abon.motot.payload.response.contact.pair;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PairResponse {
    private Long id;
    private String nickName;
    private Integer age;
    private String about;
    private Double latitude;
    private Double longitude;
    private String firstName;
    private String sex;
    private Long avatarId;
    private String motoBrand;
    private String motoModel;
}