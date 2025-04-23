package ru.gilgamesh.abon.core.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.request.target.Target
import ru.gilgamesh.abon.core.BuildConfig
import ru.gilgamesh.abon.core.R
import javax.inject.Singleton


@Singleton
object CustomGlideMethod {
    private const val HEADER_TYPE_AUTH = "Authorization"
    private const val URL_CONTACT_IMG = "api/getContactImageByIdByte?imageId="
    private const val URL_ROUTE_IMG = "api/getRouteImageByte?routeImageId="
    private const val URL_POINT_IMG = "api/getPointImageByte?pointImageId="
    private const val URL_NEWSFEED_IMG = "api/newsfeed/getImageByIdByte?imageId="
    private const val URL_GROUP_IMG = "api/group/getGroupImageByte?imageId="
    private const val USER_INFO_TYPE = "type";
    private const val USER_INFO_TOKEN = "token";
    private const val USER_INFO = "UserInfo";

    private fun getGlideUrl(url: String, context: Context): GlideUrl {
        val preferences = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE)
        return GlideUrl(
            url, object : Headers {
                override fun getHeaders(): MutableMap<String?, String?> {
                    val map: MutableMap<String?, String?> = HashMap<String?, String?>()
                    map.put(
                        HEADER_TYPE_AUTH, preferences.getString(USER_INFO_TYPE, "") + " " + preferences.getString(USER_INFO_TOKEN, "")
                    )
                    return map
                }
            })
    }


    /**
     * Грузим аватарку или фон или фотоальбомную картинку
     * @param context
     * @param imageId
     * @param imageView
     */
    fun getContactImageByIdByte(context: Context, imageId: Long, imageView: ImageView) {
        com.bumptech.glide.Glide.with(context)
            .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_CONTACT_IMG + imageId, context))
            .into(imageView)
    }

    /**
     * Грузим аватарку или фон или фотоальбомную картинку в Bitmap
     * @param context
     * @param imageId
     */
    fun getContactImageByIdByteAsBitmap(context: Context, imageId: Long): Bitmap? {
        runCatching {
            return com.bumptech.glide.Glide.with(context).asBitmap()
                .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_CONTACT_IMG + imageId, context)).submit()
                .get()
        }.onFailure {
            return null
        }
        return null
    }

    /**
     * Грузим картинку с маршрута
     * @param context
     * @param imageId
     * @param imageView
     */
    fun getRouteImageByte(context: Context, imageId: Long, imageView: ImageView) {
        com.bumptech.glide.Glide.with(context)
            .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_ROUTE_IMG + imageId, context)).into(imageView)
    }

    /**
     * Грузим картинку с точки маршрута
     * @param context
     * @param imageId
     * @param imageView
     */
    fun getPointImageByte(context: Context, imageId: Long, imageView: ImageView) {
        com.bumptech.glide.Glide.with(context)
            .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_POINT_IMG + imageId, context)).into(imageView)
    }

    /**
     * Грузим картинку из интернета
     * @param context
     * @param url
     * @param imageView
     */
    fun loadImageFromCustomUrl(context: Context?, url: String?, imageView: ImageView?) {
        if (context == null || url == null || imageView == null) return
        com.bumptech.glide.Glide.with(context).load(url).into(imageView)
    }

    /**
     * Грузим аватарку или фон или фотоальбомную картинку
     * @param context
     * @param imageId
     * @param imageView
     */
    fun getNewsfeedImageByIdByte(context: Context, imageId: String, imageView: ImageView) {
        com.bumptech.glide.Glide.with(context)
            .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_NEWSFEED_IMG + imageId, context))
            .override(Target.SIZE_ORIGINAL).into(imageView)
    }

    /**
     * Грузим аватарку группы
     * @param context
     * @param imageId
     * @param imageView
     */
    fun getGroupImageByte(context: Context, imageId: Long, imageView: ImageView) {
        com.bumptech.glide.Glide.with(context)
            .load(getGlideUrl(BuildConfig.HOST_MICRO_URL + URL_GROUP_IMG + imageId, context)).into(imageView)
    }

    fun getDefaultAvatarBySex(sex: String?, context: Context): Bitmap {
        return if (sex != null && sex == "F") {
            BitmapFactory.decodeResource(context.resources, R.drawable.kandinsky_av7)
        } else {
            BitmapFactory.decodeResource(context.resources, R.drawable.kandinsky_av2)
        }
    }

    fun getDefaultAvatarBySexResId(sex: String?): Int {
        return if (sex != null && sex == "F") {
            R.drawable.kandinsky_av7
        } else {
            R.drawable.kandinsky_av2
        }
    }
}