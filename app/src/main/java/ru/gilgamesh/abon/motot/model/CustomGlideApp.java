package ru.gilgamesh.abon.motot.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.module.AppGlideModule;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.R;

@GlideModule
public class CustomGlideApp extends AppGlideModule {
    private static final String USER_INFO_TYPE = "type";
    private static final String USER_INFO_TOKEN = "token";

    /**
     * Грузим аватарку или фон или фотоальбомную картинку
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getContactImageByIdByte(Context context, Long imageId, ImageView imageView) {
        if (context == null || imageId == null || imageView == null) return;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/getContactImageByIdByte?imageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        GlideApp.with(context).load(glideUrl).into(imageView);
    }

    /**
     * Грузим аватарку или фон или фотоальбомную картинку в Bitmap
     * @param context
     * @param imageId
     */
    public static Bitmap getContactImageByIdByteAsBitmap(Context context, Long imageId) {
        if (context == null || imageId == null) return null;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/getContactImageByIdByte?imageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        try {
            return GlideApp.with(context).asBitmap().load(glideUrl).submit().get();
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    /**
     * Грузим картинку с маршрута
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getRouteImageByte(Context context, Long imageId, ImageView imageView) {
        if (context == null || imageId == null || imageView == null) return;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/getRouteImageByte?routeImageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        GlideApp.with(context).load(glideUrl).into(imageView);
    }

    /**
     * Грузим картинку с точки маршрута
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getPointImageByte(Context context, Long imageId, ImageView imageView) {
        if (context == null || imageId == null || imageView == null) return;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/getPointImageByte?pointImageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        GlideApp.with(context).load(glideUrl).into(imageView);
    }

    /**
     * Грузим картинку из интернета
     * @param context
     * @param url
     * @param imageView
     */
    public static void loadImageFromCustomUrl(Context context, String url, ImageView imageView) {
        if (context == null || url == null || imageView == null) return;
        GlideApp.with(context).load(url).into(imageView);
    }

    /**
     * Грузим аватарку или фон или фотоальбомную картинку
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getNewsfeedImageByIdByte(Context context, String imageId, ImageView imageView) {
        if (context == null || imageId == null || imageView == null) return;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/newsfeed/getImageByIdByte?imageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        GlideApp.with(context).load(glideUrl).into(imageView);
    }

    /**
     * Грузим аватарку группы
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getGroupImageByte(Context context, Long imageId, ImageView imageView) {
        if (context == null || imageId == null || imageView == null) return;

        SharedPreferences preferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        GlideUrl glideUrl = new GlideUrl(BuildConfig.HOST_MICRO_URL + "api/group/getGroupImageByte?imageId=" + imageId, new Headers() {
            @Override
            public java.util.Map<String, String> getHeaders() {
                java.util.Map<String, String> map = new HashMap<>();
                map.put("Authorization", preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, ""));
                return map;
            }
        });
        GlideApp.with(context).load(glideUrl).into(imageView);
    }

    public static Bitmap getDefaultAvatarBySex(String sex, Context context) {
        if (context == null) return null;
        Bitmap bitmap;
        if (sex != null && sex.equals("F")) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kandinsky_av7);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.kandinsky_av2);
        }
        return bitmap;
    }

    public static int getDefaultAvatarBySexResId(String sex) {
        if (sex != null && sex.equals("F")) {
            return R.drawable.kandinsky_av7;
        } else {
            return R.drawable.kandinsky_av2;
        }
    }
}
