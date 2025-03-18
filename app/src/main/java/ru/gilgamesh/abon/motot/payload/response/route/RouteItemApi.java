package ru.gilgamesh.abon.motot.payload.response.route;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import ru.gilgamesh.abon.motot.payload.ImageDataResponse;

@Setter @Getter
public class RouteItemApi {
    private Long id;
    private String name;
    private ImageDataResponse routeImage;
    private Integer imageHashCode;
    private Long imageId;
    private String createdDate;
    private String description;
    private Integer distance;
    //private String looks;
    private Integer likes;
    private Boolean creator;
    private Boolean liked;
    private Long contactId;
    private String nickName;
    private Boolean isFinishedRoute;
    private String type;
    private String pointList;
    private Integer averageSpeed;
    private Long activeTime;
    private Long totalTime;
    private Integer looks;
    private String routeStyle;

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
