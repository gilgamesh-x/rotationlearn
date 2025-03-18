package ru.gilgamesh.abon.motot.payload.response.contact;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;

//{"id":10,"nickName":"bon","birthDate":"13.09.1993","age":30,"about":"nul\nчоооткп\n","countSubscriptions":2,"countSubscribers":1,"countPhotos":0,"latitude":55.7847388,"longitude":37.8544479,"firstName":"alex","sex":"M","dualLike":false,"avatar":null,"cover":null,"motoBrand":"BMW","motoModel":"F 850 GS Adventure","subscriber":false,"subscription":false}
@Setter @Getter
public class UserInfoApi implements Cloneable {
    private Long id;
    public String nickName;
    private String birthDate;
    private Integer age;
    private String about;
    private String aboutPair;
    private Integer countSubscriptions;
    private Integer countSubscribers;
    private Integer countPhotos;
    private Integer countMyRoute;
    private Integer countFinishRoute;
    private Integer countMyCompetition;
    private Integer countFinishCompetition;
    private Double latitude;
    private Double longitude;
    private String firstName;
    public String sex;
    private Boolean dualLike;
    public String motoBrand;
    public String motoModel;
    private Boolean subscriber;
    private Boolean subscription;
    //private ImageDataResponse avatar;
    //private ImageDataResponse cover;
    private Boolean imRoadHelper;
    private Integer radiusRoadHelper;
    private Boolean verifiedFlg;
    private UserAchievementResponse achievement;
    private Double distance;
    private Boolean isWantFindPair;
    private Long avatarId;
    public Long miniAvatarId;
    private Long coverId;

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        UserInfoApi cloned = (UserInfoApi) super.clone();
        if (achievement != null) {
            cloned.achievement = (UserAchievementResponse) achievement.clone();
        }
        return cloned;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
