package ru.gilgamesh.abon.motot.payload.response.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class RouteInfoResponse {
    private Long id;
    private String createdDate;
    private Long contactId;
    private String nickName;
    private Long imageId;
    private String name;
    private String description;
    private String type;
    private Integer distance;
    private Integer looks;
    private Integer likes;
    private Double latitudeStart;
    private Double longitudeStart;
    private Boolean isFinishedRoute;
    private String pointList;
    private Boolean trackingPublicFlg;
    private Integer averageSpeed;
    private Long activeTime;
    private Long totalTime;
    private Boolean creator;
    private Boolean liked;
    private Boolean favorite;
    private String routeStyle;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
