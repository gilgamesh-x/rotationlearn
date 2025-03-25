package ru.gilgamesh.abon.motot.ui.sideNav.motoRating.distanceRating.recycleViewRatingDistance;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.model.CustomGlideApp;

public class RatingHolder extends RecyclerView.ViewHolder{
    TextView place, contactName, motorcycleName, distance;
    ImageView img;
    ConstraintLayout mainLayout;

    public RatingHolder(@NonNull View itemView) {
        super(itemView);
        this.place = itemView.findViewById(R.id.place_text);
        this.contactName = itemView.findViewById(R.id.nickName_text);
        this.motorcycleName = itemView.findViewById(R.id.motorcycle_text);
        this.distance = itemView.findViewById(R.id.distance_text);
        this.img = itemView.findViewById(R.id.member_img);
        this.mainLayout = itemView.findViewById(R.id.mainLayout);
    }

    void bind(RatingItem member, int position) {
        if (member.getImage() != null && member.getImage().getId() != null) {
            CustomGlideApp.getContactImageByIdByte(this.itemView.getContext(), member.getImage().getId(), this.img);
        } else {
            this.img.setImageResource(CustomGlideApp.getDefaultAvatarBySexResId(member.getSex()));
        }
        this.contactName.setText(member.getContactName());
        if (member.getMotorcycleName() == null || member.getMotorcycleName().isEmpty()) {
            this.motorcycleName.setVisibility(View.GONE);
        } else {
            this.motorcycleName.setText(member.getMotorcycleName());
            this.motorcycleName.setVisibility(View.VISIBLE);
        }
        int place = 0;
        if (member.getPlace() != null) {
            place = member.getPlace();
            this.place.setText(String.valueOf(place));
        } else {
            place = position + 1;
            this.place.setText(String.valueOf(place));
        }
        if (place >= 1 && place <= 3) {
            Shader shader = new LinearGradient(0, this.place.getTextSize(), this.place.getPaint().measureText((String) this.place.getText()), this.place.getTextSize(),
                    Color.parseColor("#FF7919"), Color.parseColor("#FFCF53"), Shader.TileMode.REPEAT);
            this.place.getPaint().setShader(shader);
        } else {
            Shader shader = new LinearGradient(0, this.place.getTextSize(), this.place.getPaint().measureText((String) this.place.getText()), this.place.getTextSize(),
                    Color.parseColor("#FCFCFC"), Color.parseColor("#FCFCFC"), Shader.TileMode.REPEAT);
            this.place.getPaint().setShader(shader);
        }
        if (member.getDistance() == null) {
            this.distance.setText(String.format(this.itemView.getContext().getString(R.string.dynamic_profile_distance_1), 0));
        } else {
            this.distance.setText(String.format(this.itemView.getContext().getString(R.string.dynamic_profile_distance_1), member.getDistance()));
        }
        if (member.getContactId() != null && App.contactEqual(member.getContactId())) {
            this.mainLayout.setBackground(AppCompatResources.getDrawable(this.itemView.getContext(), R.drawable.background_radius_8_rating_me));
        } else {
            this.mainLayout.setBackground(AppCompatResources.getDrawable(this.itemView.getContext(), R.drawable.background_radius_8_rating));
        }
    }
}
