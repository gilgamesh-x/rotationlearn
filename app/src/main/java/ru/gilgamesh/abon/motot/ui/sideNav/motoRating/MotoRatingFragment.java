package ru.gilgamesh.abon.motot.ui.sideNav.motoRating;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.data.api.RatingApi;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.model.CustomGlideApp;
import ru.gilgamesh.abon.motot.payload.response.rating.PageableRatingItem;
import ru.gilgamesh.abon.motot.ui.profile.EnemyProfileActivity;
import ru.gilgamesh.abon.motot.ui.profile.ProfileActivity;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RecycleViewRating.RatingAdapter;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RecycleViewRating.RatingItem;

@AndroidEntryPoint
public class MotoRatingFragment extends Fragment {
    private static final String TAG = MotoRatingFragment.class.getSimpleName();

    @Inject
    public RatingApi ratingApi;
    private static final Integer CUR_YEAR = 2025;
    private Integer pickYear = 2025;

    RatingAdapter ratingAdapter;
    private List<RatingItem> ratingItemList;
    private Handler mHandler;
    private int pageNumber = 0;
    private boolean isLast = false, canLoadPage = true;

    @Override
    public void onDestroyView() {
        ratingAdapter = null;
        super.onDestroyView();
    }

    public MotoRatingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moto_rating, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(_view, savedInstanceState);
        ImageView avatar = _view.findViewById(R.id.img_profile_avatar);
        if (App.contactInfo != null) {
            if (App.contactInfo.getMiniAvatarId() != null) {
                CustomGlideApp.getContactImageByIdByte(avatar.getContext(), App.contactInfo.getMiniAvatarId(), avatar);
            } else {
                avatar.setImageResource(CustomGlideApp.getDefaultAvatarBySexResId(App.contactInfo.getSex()));
            }
        }
        TextView nickname_textView = _view.findViewById(R.id.nickname);
        TextView motorcycle_textView = _view.findViewById(R.id.motorcycle);
        if (App.contactInfo != null) {
            nickname_textView.setText(App.contactInfo.getNickName());
            if (App.contactInfo.getMotoBrand() != null && !App.contactInfo.getMotoBrand().isEmpty()) {
                if (App.contactInfo.getMotoModel() != null && !App.contactInfo.getMotoModel().isEmpty()) {
                    motorcycle_textView.setText(App.contactInfo.getMotoBrand() + " " + App.contactInfo.getMotoModel());
                } else {

                    motorcycle_textView.setText(App.contactInfo.getMotoBrand());
                }
            } else {
                motorcycle_textView.setText("");
            }
        } else {
            nickname_textView.setText("Nickname");
            motorcycle_textView.setText("");
        }

        TextView distance_textView = _view.findViewById(R.id.distance_textView);
        if (App.contactInfo == null || App.contactInfo.getDistance() == null) {
            distance_textView.setText(String.format(getString(R.string.dynamic_profile_distance_1), 0));
        } else {
            distance_textView.setText(String.format(getString(R.string.dynamic_profile_distance_1), App.contactInfo.getDistance().intValue()));
        }

        Shader shader = new LinearGradient(0, distance_textView.getTextSize(), distance_textView.getPaint().measureText((String) distance_textView.getText()), distance_textView.getTextSize(), Color.parseColor("#FF7919"), Color.parseColor("#FFCF53"), Shader.TileMode.REPEAT);
        distance_textView.getPaint().setShader(shader);


        RecyclerView ratingRecyclerView = _view.findViewById(R.id.ratingRecyclerView);
        ratingItemList = new ArrayList<>();
        ratingAdapter = new RatingAdapter(ratingItemList, (view, position) -> {
            if (ratingItemList.get(position).getContactId() != null) {

                if (App.checkGuestRolePopup(getContext(), getParentFragmentManager())) return;

                if (App.contactEqual(ratingItemList.get(position).getContactId())) {
                    startActivity(new Intent(this.getContext(), ProfileActivity.class));
                } else {
                    Intent intent = new Intent(this.getContext(), EnemyProfileActivity.class);
                    intent.putExtra("prevActivity", TAG);
                    intent.putExtra("contactId", ratingItemList.get(position).getContactId());
                    startActivity(intent);
                }
            }
        });
        ratingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //ratingRecyclerView.setItemAnimator(null);
        ratingRecyclerView.setAdapter(ratingAdapter);
        mHandler = new Handler(Looper.getMainLooper());
        ratingRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (isLast || !canLoadPage) return;
                if (layoutManager != null && layoutManager.findLastCompletelyVisibleItemPosition() >= ratingItemList.size() - 5) {
                    if (Objects.equals(pickYear, CUR_YEAR)) {
                        new Thread(() -> loadPageRating(pageNumber + 1)).start();
                    } else {
                        new Thread(()-> loadPageRatingByYear(pageNumber + 1)).start();
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView _recyclerView, int newState) {
                super.onScrollStateChanged(_recyclerView, newState);
            }
        });

        Chip chip2024 = _view.findViewById(R.id.chip2024);
        chip2024.setOnClickListener(view -> {
            if (!canLoadPage) return;
            pickYear = 2024;
            pageNumber = 0;
            isLast = false;
            ratingAdapter.clearItems();
            new Thread(()-> loadPageRatingByYear(0)).start();
        });

        Chip chip2025 = _view.findViewById(R.id.chip2025);
        chip2025.setOnClickListener(view -> {
            if (!canLoadPage) return;
            pickYear = 2025;
            pageNumber = 0;
            isLast = false;
            ratingAdapter.clearItems();
            new Thread(()-> loadPageRating(0)).start();
        });

        new Thread(()-> loadPageRating(0)).start();
    }

    private void loadPageRating(int pageNum) {
        if (isLast || !canLoadPage) return;
        canLoadPage = false;
        pageNumber = pageNum;
        ratingApi.getDistanceRating(pageNum, 20).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PageableRatingItem> call, @NonNull Response<PageableRatingItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isLast = response.body().getLast();
                    mHandler.post(() -> {
                        if (ratingAdapter != null) {
                            ratingAdapter.addItems(response.body().getContent());
                        }
                    });
                } else {
                    isLast = true;
                }
                canLoadPage = true;
            }

            @Override
            public void onFailure(@NonNull Call<PageableRatingItem> call, @NonNull Throwable throwable) {
                isLast = true;
                canLoadPage = true;
            }
        });
    }

    private void loadPageRatingByYear(int pageNum) {
        if (isLast || !canLoadPage) return;
        canLoadPage = false;
        pageNumber = pageNum;
        ratingApi.getDistanceRatingByYear(pageNum, 20, pickYear).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<PageableRatingItem> call, @NonNull Response<PageableRatingItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isLast = response.body().getLast();
                    mHandler.post(() -> {
                        if (ratingAdapter != null) {
                            ratingAdapter.addItems(response.body().getContent());
                        }
                    });
                } else {
                    isLast = true;
                }
                canLoadPage = true;
            }

            @Override
            public void onFailure(@NonNull Call<PageableRatingItem> call, @NonNull Throwable throwable) {
                isLast = true;
                canLoadPage = true;
            }
        });
    }

    protected boolean isSafe() {
        return !(this.isRemoving() || this.getActivity() == null || this.isDetached() || !this.isAdded() || this.getView() == null);
    }
}