package ru.gilgamesh.abon.motot.model;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fingerprintjs.android.fingerprint.Fingerprinter;
import com.fingerprintjs.android.fingerprint.FingerprinterFactory;
import com.google.android.material.badge.BadgeDrawable;
import com.google.firebase.installations.FirebaseInstallations;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mobile.ads.common.MobileAds;

import java.util.List;
import java.util.Set;

import dagger.hilt.android.HiltAndroidApp;
import ru.gilgamesh.abon.motot.AuthAlertBSFragment;
import ru.gilgamesh.abon.motot.BuildConfig;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.payload.response.auth.SigninResponse;
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi;
import ru.gilgamesh.abon.motot.ui.bottomNav.route.RVRoute.ItemRoute;

@HiltAndroidApp
public class App extends Application {
    public static final String TAG = App.class.getSimpleName();
    public static final String FCM_INFO = "_"; //FCM
    public static final String APP_INFO = "UserInfo"; //Инфа по приложению
    public static final String USER_INFO = "UserInfo"; //Инфа для авторизации
    public static final String PROFILE_INFO = "ProfileInfo"; //Инфа для редактирования профиля
    public static final String IMAGE_INFO = "ImageInfo"; //Аватарка и фон профиля
    public static final String PHOTO_ALBUM = "PhotoAlbum"; //Фото альбом пользователя
    public static final String IMAGE_FULL = "ImageFull"; //Фото для открытия на весь экран
    public static final String ROUTE_INFO = "RouteInfo"; //Для редактирования сущ маршрута
    public static final String ROUTE_IMAGES = "RouteImages"; //Для редактирования сущ маршрута
    public static final String ROUTE_LOOKS = "RouteLooks"; //Просмотренные пользователем маршруты, чтобы не вызывать каждый раз интеграцию при открытии маршрута
    public static final String NOTIFICATION_INFO = "NotificationInfo"; //Для хранения уведомлений
    public static final String CHAT_INFO = "ChatInfo";
    public static final String MESSAGE_INFO = "MessageInfo";
    public static final String ROAD_HELP = "RoadHelpInfo";
    public static final String ROUTE_POINT_INFO = "RoutePointInfo"; //Для передачи данных когда из карточки маршрута открываем карточку точки маршрута
    public static final String COMPETITION_INFO = "CompetitionInfo";
    public static final String COMPETITION_MEMBER_INFO = "CompetitionMemberInfo";
    public static final String GROUP_INFO = "GroupInfo";
    public static final String GROUP_IMAGES = "GroupImages";
    @Deprecated
    public static SigninResponse loginInfo;
    public static UserInfoApi contactInfo;
    public static String context = "";
    public static int notificationCount = 0;
    public static Long msgCountUnread = 0L; //Общее кол-во по чатам не прочитанных сообщений
    public static Long currentChatUnread = 0L; //Чат в который перешли со вкладки чатов, для обновления при переходе обратно
    public static Bitmap imageFull;
    public static ImageView navAvatar;
    public static TextView navNick;
    public static BadgeDrawable badgeDrawableNotification;
    public static BadgeDrawable badgeDrawableChat;
    public static FragmentManager fragmentManager;
    public static List<ItemRoute> allRouteItems;
    public static List<ItemRoute> favoriteRouteItems;
    public static DrawerLayout sideMenu;
    public static Long activeGroupId = null;
    public static boolean isShowStartSeason = false;
    private static Resources resources;
    private static int notificationCountSett = 0;

    public static Resources getAppResources() {
        return resources;
    }

    public static void hideKeyboard(Activity activity) {
        View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            focusView.clearFocus();
            ((InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(focusView.getWindowToken(), HIDE_NOT_ALWAYS);
        }
    }

    public static void hideKeyboard(Context context, View view) {
        if (view != null) {
            view.clearFocus();
            ((InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), HIDE_NOT_ALWAYS);
        }
    }

    public static void calcNotificationIcon(int count, SharedPreferences uInfo) {
        notificationCount = count;
        notificationCountSett = uInfo.getInt("count", 0);
        calcNotificationIconUI();
    }

    public static void calcNotificationIcon(int count) {
        notificationCount = count;
        calcNotificationIconUI();
    }

    public static void calcNotificationChatIcon() {
        if (badgeDrawableChat != null) {
            if (msgCountUnread > 0) {
                badgeDrawableChat.setText(String.valueOf(msgCountUnread));
                badgeDrawableChat.setVisible(true, true);
            } else {
                badgeDrawableChat.setText("0");
                badgeDrawableChat.setVisible(false, true);
            }
        }
    }

    private static void calcNotificationIconUI() {
        if (badgeDrawableNotification != null) {
            if (notificationCount > 0) {
                badgeDrawableNotification.setText(String.valueOf(notificationCount));
                badgeDrawableNotification.setVisible(true, true);
            } else {
                badgeDrawableNotification.setText("0");
                badgeDrawableNotification.setVisible(false, true);
            }
        }
    }

    public static void clearLocalStorage(Context context) {
        //context.getSharedPreferences(App.FCM_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.PROFILE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.IMAGE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.PHOTO_ALBUM, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.IMAGE_FULL, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROUTE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.NOTIFICATION_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.CHAT_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.MESSAGE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROAD_HELP, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROUTE_POINT_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        exit();
    }

    public static void clearLocalStorageWithoutLogin(Context context) {
        context.getSharedPreferences(App.PROFILE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.IMAGE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.PHOTO_ALBUM, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.IMAGE_FULL, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROUTE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.NOTIFICATION_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.CHAT_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.MESSAGE_INFO, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROAD_HELP, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(App.ROUTE_POINT_INFO, Context.MODE_PRIVATE).edit().clear().apply();
    }

    public static void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
        fragmentTransaction.commit();
    }

    private static void exit() {
        allRouteItems = null;
        favoriteRouteItems = null;
    }

    /**
     * Сравниваем ид авторизованного пользователя с тем что на входе
     * @param contactId
     * @return
     */
    public static boolean contactEqual(Long contactId) {
        if (contactId == null || App.contactInfo == null || App.contactInfo.getId() == null)
            return false;
        return App.contactInfo.getId().equals(contactId);
    }

    /**
     *
     * @return id авторизованного пользователя
     */
    public static Long getContactIdAuth() {
        if (App.contactInfo == null || App.contactInfo.getId() == null) return null;
        return App.contactInfo.getId();
    }

    /**
     * Возвращает позицию пользователя, если ее почему-то нет, то центр москвы
     *
     * @return
     */
    public static Point getUserPosition() {
        Point point;
        if (App.contactInfo == null || App.contactInfo.getLatitude() == null || App.contactInfo.getLongitude() == null) {
            point = new Point(55.753051, 37.621647);
        } else {
            point = new Point(App.contactInfo.getLatitude(), App.contactInfo.getLongitude());
        }
        return point;
    }

    public static void setUserInfoDistancePlus(Double _distance) {
        if (App.contactInfo != null && _distance != null) {
            if (App.contactInfo.getDistance() == null || App.contactInfo.getDistance() == 0) {
                App.contactInfo.setDistance(_distance);
            } else {
                App.contactInfo.setDistance(App.contactInfo.getDistance() + _distance);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        final String MAPKIT_API_KEY = "d52d7abf-887d-49b5-81c0-a39b169ef9c7";
        //final String APPMETRIC_API_KEY = "c386d437-4640-4b66-9727-b3ac4b10d313";
        MapKitFactory.setApiKey(MAPKIT_API_KEY);        // Creating an extended library configuration.
        //AppMetricaConfig config = AppMetricaConfig.newConfigBuilder(APPMETRIC_API_KEY).build();
        // Initializing the AppMetrica SDK.
        //AppMetrica.activate(this, config);
        //AppMetricaPush.activate(getApplicationContext());
        resources = getResources();
        MobileAds.enableDebugErrorIndicator(!BuildConfig.BUILD_TYPE.equals("release"));
        if (!getSharedPreferences(App.FCM_INFO, MODE_PRIVATE).contains("fbi")) {
            FirebaseInstallations.getInstance().getId().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Installations", "Installation ID: " + task.getResult());
                    getSharedPreferences(App.FCM_INFO, MODE_PRIVATE).edit().putString("fbi", task.getResult()).apply();
                } else {
                    Log.e("Installations", "Unable to get Installation ID");
                    // Initialization
                    Fingerprinter fingerprinter = FingerprinterFactory.create(getApplicationContext());

                    // Usage
                    fingerprinter.getFingerprint(Fingerprinter.Version.V_5, fingerprint-> {

                        Log.d("FingerprinterInstallations", "Fingerprinter Installation ID: " + task.getResult());
                        getSharedPreferences(App.FCM_INFO, MODE_PRIVATE).edit().putString("fbi", fingerprint).apply();
                        // use fingerprint
                        return null;
                    });

                }
            });
        }
    }

    /**
     * Проверяем есть ли у пользователя роль ROLE_GUEST и если есть и она единственная, то
     * отображаем попап о необходимости пройти регистрацию
     * @param ctx
     * @param fragmentManager
     * @return
     */
    public static boolean checkGuestRolePopup(Context ctx, FragmentManager fragmentManager) {
        if (fragmentManager == null) return false;
        if (checkGuestRole(ctx)) {
            AuthAlertBSFragment authAlertBSFragment = new AuthAlertBSFragment();
            authAlertBSFragment.show(fragmentManager, authAlertBSFragment.getTag());
            return true;
        }
        return false;
    }

    /**
     * Проверяем есть ли у пользователя роль ROLE_GUEST
     * @param ctx
     * @return
     */
    public static boolean checkGuestRole(Context ctx) {
        if (ctx == null) return false;
        SharedPreferences usrInf = ctx.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        if (!usrInf.contains("roles")) return false;

        Set<String> roles = usrInf.getStringSet("roles", null);
        if (roles == null || roles.isEmpty()) return false;
        return roles.size() == 1 && roles.contains("ROLE_GUEST");
    }

    /**
     * Проверяем есть ли у пользователя роль ROLE_NO_ADS
     * @param ctx
     * @return
     */
    public static boolean checkNoAdsRole(Context ctx) {
        if (ctx == null) return false;
        SharedPreferences usrInf = ctx.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        if (!usrInf.contains("roles")) return false;

        Set<String> roles = usrInf.getStringSet("roles", null);
        if (roles == null || roles.isEmpty()) return false;
        return roles.contains("ROLE_NO_ADS");
    }

    /**
     * Проверяем есть ли у пользователя роль ROLE_NO_ADS
     * @param ctx
     * @return
     */
    public static boolean checkSuperLikeRole(Context ctx) {
        if (ctx == null) return false;
        SharedPreferences usrInf = ctx.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        if (!usrInf.contains("roles")) return false;

        Set<String> roles = usrInf.getStringSet("roles", null);
        if (roles == null || roles.isEmpty()) return false;
        return roles.contains("SUPER_LIKE_ROLE");
    }
}