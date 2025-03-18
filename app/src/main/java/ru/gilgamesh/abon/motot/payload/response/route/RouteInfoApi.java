package ru.gilgamesh.abon.motot.payload.response.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

@Setter@Getter
public class RouteInfoApi {
    private Long id;
    private String name;
    private String description;
    private String type;
    private String pointList;
    private Integer distance;
    private Integer difficulty;
    //private ImageDataResponse routeImage;
    //private Integer imageHashCode;
    private Long imageId;
    private Integer likes;
    private Boolean creator;
    private String createdDate;
    private Long contactId;
    private String nickName;
    private Boolean liked;
    private Boolean isFinishedRoute;
    private Integer averageSpeed;
    private Long activeTime;
    private Long totalTime;
    private Integer looks;
    private String routeStyle;

    public RouteInfoApi (RouteInfoResponse item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.type = item.getType();
        this.pointList = item.getPointList();
        this.distance = item.getDistance();
        this.imageId = item.getImageId();
        this.likes = item.getLikes();
        this.creator = item.getCreator();
        this.createdDate = item.getCreatedDate();
        this.contactId = item.getContactId();
        this.nickName = item.getNickName();
        this.liked = item.getLiked();
        this.isFinishedRoute = item.getIsFinishedRoute();
        this.averageSpeed = item.getAverageSpeed();
        this.activeTime = item.getActiveTime();
        this.totalTime = item.getTotalTime();
        this.looks = item.getLooks();
        this.routeStyle = item.getRouteStyle();
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
