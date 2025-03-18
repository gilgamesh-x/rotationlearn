package ru.gilgamesh.abon.motot.payload;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.gilgamesh.abon.motot.ui.profile.RecyclerViewImgGallery.ItemImg;

@Setter @Getter @NoArgsConstructor
public class ImageDataResponse {
    private Long id;
    private String imageData;
    private Integer imageHashCode;

    public ImageDataResponse(ItemImg itemImg) {
        this.id = itemImg.getId();
        this.imageData = itemImg.getBase64();
    }

    public ImageDataResponse(Long id) {
        this.id = id;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
