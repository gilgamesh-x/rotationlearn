package ru.gilgamesh.abon.motot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.yandex.mapkit.MapKitFactory;

import java.time.LocalDate;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.gilgamesh.abon.motot.data.api.AuthApi;
import ru.gilgamesh.abon.motot.data.api.ChatApi;
import ru.gilgamesh.abon.motot.data.api.ContactApi;
import ru.gilgamesh.abon.motot.data.api.GroupApi;
import ru.gilgamesh.abon.motot.data.api.NotificationApi;
import ru.gilgamesh.abon.motot.data.api.RoadHelpApi;
import ru.gilgamesh.abon.motot.databinding.ActivityAppBinding;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.model.CustomGlideApp;
import ru.gilgamesh.abon.motot.network.FirebaseMessageReceiver;
import ru.gilgamesh.abon.motot.network.FirebaseMessageReceiverType;
import ru.gilgamesh.abon.motot.payload.CounterResponse;
import ru.gilgamesh.abon.motot.payload.request.contact.ContactFCMRequest;
import ru.gilgamesh.abon.motot.payload.request.contact.SetOnlinePositionRequest;
import ru.gilgamesh.abon.motot.payload.response.IdentifierResponse;
import ru.gilgamesh.abon.motot.payload.response.MessageResponse;
import ru.gilgamesh.abon.motot.payload.response.contact.UserInfoApi;
import ru.gilgamesh.abon.motot.payload.response.group.GroupResponse;
import ru.gilgamesh.abon.motot.ui.bottomNav.chat.ChatCardActivity;
import ru.gilgamesh.abon.motot.ui.bottomNav.chat.ChatFragment;
import ru.gilgamesh.abon.motot.ui.bottomNav.findPair.FindPairFragment;
import ru.gilgamesh.abon.motot.ui.bottomNav.group.GroupCardActivity;
import ru.gilgamesh.abon.motot.ui.bottomNav.group.GroupFragment;
import ru.gilgamesh.abon.motot.ui.bottomNav.group.RVGroup.ItemGroup;
import ru.gilgamesh.abon.motot.ui.bottomNav.home.HomeFragment;
import ru.gilgamesh.abon.motot.ui.bottomNav.news.NewsFragment;
import ru.gilgamesh.abon.motot.ui.bottomNav.route.RouteFragment;
import ru.gilgamesh.abon.motot.ui.notification.NotificationActivity;
import ru.gilgamesh.abon.motot.ui.profile.LoginActivity;
import ru.gilgamesh.abon.motot.ui.profile.ProfileActivity;
import ru.gilgamesh.abon.motot.ui.sideNav.feedback.FeedbackBottomSheetFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoCompetition.MotoCompetitionDetailActivity;
import ru.gilgamesh.abon.motot.ui.sideNav.motoCompetition.MotoCompetitionFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.MotoRatingFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRating.RatingMotorcycleFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRoadHelp.MotoRoadHelpCardFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoRoadHelp.MotoRoadHelpFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoStudy.MotoStudyFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.motoTowTruck.TowTruckFragment;
import ru.gilgamesh.abon.motot.ui.sideNav.setting.SettingFragment;

@AndroidEntryPoint
@ExperimentalBadgeUtils
public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    final static String TAG = AppActivity.class.getSimpleName();

    @Inject
    GroupApi groupApi;
    @Inject
    NotificationApi notificationApi;
    @Inject
    ChatApi chatApi;
    @Inject
    ContactApi contactApi;
    @Inject
    AuthApi authApi;
    @Inject
    RoadHelpApi roadHelpApi;

    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    private ActivityAppBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView navNick;
    private TextView navLogin;
    private ImageView navAvatar;
    private int firstOpenFragment = 0; //1 - Home, 2 - Route, 3 - News 4 - Group, 5 - Pair
    private LocationManager locationManager;
    private LocationListener locationListener;

    private static void unCheckAllMenuItems(@NonNull final Menu menu) {
        final int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setCheckable(false);
                item.setChecked(false);
                item.setCheckable(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == 999) {
            fillSidePanel();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            MapKitFactory.initialize(this);
        } catch (RuntimeException e) {
            Log.e(TAG, "onCMapKitFactory.initialize: ERROR\n" + e);
        }
        //setContentView(R.layout.activity_app);
        final String fcmToken = FirebaseMessageReceiver.getToken(this);
        Log.d("token", fcmToken);
        refreshTokenFCM();
        binding = ActivityAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        locationListener = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        requestLocation();

        /**
         * Обрабатываем сообщение от FCM если отрывалось с закрытым приложением
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String FIREBASE_TYPE = bundle.containsKey("FIREBASE_TYPE") ? (String) bundle.get("FIREBASE_TYPE") : "";
            if (FIREBASE_TYPE == null) FIREBASE_TYPE = "";
            String BUNDLE_TYPE = bundle.containsKey("type") ? (String) bundle.get("type") : "";
            if (BUNDLE_TYPE == null) BUNDLE_TYPE = "";

            if (BUNDLE_TYPE.equals("message")) {
                String chatId = bundle.get("chatId").toString();
                Long chatIdL = chatId == null || chatId.isEmpty() ? -1L : Long.valueOf(chatId);
                String chatName = bundle.get("chatName").toString();
                String companionId = bundle.get("fromId").toString();
                Long companionIdL = companionId == null || companionId.isEmpty() ? -1L : Long.valueOf(companionId);

                if (!App.context.equals("chat_" + chatId)) {
                    App.msgCountUnread++;
                    Intent intent = new Intent(this, ChatCardActivity.class);
                    intent.putExtra("chatId", chatIdL);
                    intent.putExtra("chatName", chatName);
                    intent.putExtra("companionId", companionIdL);
                    startActivity(intent);
                }
            } else if (FirebaseMessageReceiverType.COMPETITION_NEW_MEMBER.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.COMPETITION_NEW_MEMBER.equal(BUNDLE_TYPE) || FirebaseMessageReceiverType.COMPETITION_APPROVE_MEMBER.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.COMPETITION_APPROVE_MEMBER.equal(BUNDLE_TYPE) || FirebaseMessageReceiverType.COMPETITION.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.COMPETITION.equal(BUNDLE_TYPE)) {
                Intent intent = new Intent(this, MotoCompetitionDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (FirebaseMessageReceiverType.GROUP_INVITE.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.GROUP_INVITE.equal(BUNDLE_TYPE)) {
                firstOpenFragment = 4;
            } else if (FirebaseMessageReceiverType.GROUP_CHANGE_BASE.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.GROUP_CHANGE_BASE.equal(BUNDLE_TYPE) || FirebaseMessageReceiverType.GROUP_RIDING_MEMBER.equal(FIREBASE_TYPE) || FirebaseMessageReceiverType.GROUP_RIDING_MEMBER.equal(BUNDLE_TYPE)) {
                firstOpenFragment = 4;
                if (bundle.containsKey("id")) {
                    try {
                        String groupIdStr = bundle.get("id").toString();
                        final long groupId = groupIdStr.isEmpty() ? -1L : Long.valueOf(groupIdStr);
                        if (groupId > 0) {
                            new Thread(() -> {
                                groupApi.getGroupById(groupId).enqueue(new Callback<>() {
                                    @Override
                                    public void onResponse(@NonNull Call<GroupResponse> call, @NonNull Response<GroupResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            runOnUiThread(() -> {
                                                Intent groupCardIntent = new Intent(AppActivity.this, GroupCardActivity.class);
                                                groupCardIntent.putExtra("notificationGroup", new ItemGroup(response.body()).toString());
                                                startActivity(groupCardIntent);
                                            });
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<GroupResponse> call, @NonNull Throwable throwable) {

                                    }
                                });
                            }).start();
                        }
                    } catch (Exception ignored) {
                    }
                }
            } else if (bundle.containsKey("from") && !bundle.get("from").toString().isEmpty()) {
                Intent intent = new Intent(this, NotificationActivity.class);
                startActivity(intent);
            }
        }

        setSupportActionBar(binding.appBarMain.toolbar);

        // This callback is only called when MyFragment is at least started
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    //super.onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        drawerLayout = binding.containerapp;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, binding.appBarMain.toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        App.sideMenu = drawerLayout;

        NavigationView navigationView = binding.navigationDrawer;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().setGroupDividerEnabled(true);
        MenuCompat.setGroupDividerEnabled(navigationView.getMenu(), true);
        navigationView.setItemIconTintList(null);

        View navHeader = navigationView.getHeaderView(0);
        navNick = navHeader.findViewById(R.id.nav_header_nick);
        App.navNick = navNick;
        navLogin = navHeader.findViewById(R.id.nav_header_login);
        navAvatar = navHeader.findViewById(R.id.nav_header_avatar);
        App.navAvatar = navAvatar;

        navHeader.findViewById(R.id.header_side_bar_layout).setOnClickListener(v -> {
            if (App.checkGuestRolePopup(AppActivity.this, getSupportFragmentManager())) return;

            startActivity(new Intent(this, ProfileActivity.class));
            if (drawerLayout != null) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        fillSidePanel();

        BottomNavigationView bottomNavigationView = binding.navView;
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setItemIconTintList(null);
        //App.badgeDrawableChat = bottomNavigationView.getOrCreateBadge(R.id.navigation_chat);
        //App.badgeDrawableChat.setBackgroundColor(getColor(R.color.moto_badge_notification));
        //App.calcNotificationChatIcon();
        showHidePairFragment();
        bottomNavigationView.setOnItemSelectedListener(item -> {
            getSupportActionBar().show();
            unCheckAllMenuItems(navigationView.getMenu());
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                setTitle(getString(R.string.nav_event));
                openFragment(new HomeFragment());
                selectedBottomMenuItem(bottomNavigationView, R.id.navigation_home);
                showHidePairFragment();
                return false;
            } else if (itemId == R.id.navigation_route) {
                setTitle(getString(R.string.nav_route));
                openFragment(new RouteFragment());
                selectedBottomMenuItem(bottomNavigationView, R.id.navigation_route);
                showHidePairFragment();
                return false;
            } else if (itemId == R.id.navigation_news) {
                setTitle(getString(R.string.nav_news));
                openFragment(new NewsFragment());
                selectedBottomMenuItem(bottomNavigationView, R.id.navigation_news);
                showHidePairFragment();
                return false;
            } else if (itemId == R.id.navigation_group) {
                setTitle(getString(R.string.nav_group));
                openFragment(new GroupFragment());
                selectedBottomMenuItem(bottomNavigationView, R.id.navigation_group);
                showHidePairFragment();
                return false;
            } else if (itemId == R.id.navigation_find_pair) {
                setTitle(getString(R.string.nav_pair));
                openFragment(new FindPairFragment());
                selectedBottomMenuItem(bottomNavigationView, R.id.navigation_find_pair);
                showHidePairFragment();
                return false;
            } /*else if (itemId == R.id.navigation_chat) {
                setTitle(getString(R.string.nav_msg));
                openFragment(new ChatFragment());
                bottomNavigationView.getMenu().findItem(R.id.navigation_chat).setChecked(true);
                showHidePairFragment();
                return false;
            }*/
            return true;
        });

        fragmentManager = getSupportFragmentManager();
        App.fragmentManager = fragmentManager;
        if (firstOpenFragment == 4) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_group);
            selectedBottomMenuItem(bottomNavigationView, R.id.navigation_group);
        } else {
            bottomNavigationView.setSelectedItemId(R.id.navigation_news);
            selectedBottomMenuItem(bottomNavigationView, R.id.navigation_news);
            if (Locale.getDefault().getLanguage().equalsIgnoreCase("RU") && !App.isShowStartSeason) {
                // Получаем текущую дату
                LocalDate currentDate = LocalDate.now();

                // Задаем дату 01.05.2025
                LocalDate targetDate = LocalDate.of(2025, 5, 1);

                // Сравниваем даты
                if (currentDate.isBefore(targetDate)) {
                    App.isShowStartSeason = true;
                    StartSeasonAppActivityFragment startSeasonAppActivityFragment = new StartSeasonAppActivityFragment();
                    startSeasonAppActivityFragment.show(getSupportFragmentManager(), startSeasonAppActivityFragment.getTag());
                }
            }
        }
    }

    private void selectedBottomMenuItem(BottomNavigationView view, int itemId) {
        if (view == null) return;

        final Menu menu = view.getMenu();
        final int size = menu.size();
        for (int i = 0; i < size; i++) {
            menu.getItem(i).setChecked(menu.getItem(i).getItemId() == itemId);
        }

        if (itemId == R.id.navigation_home) {
            menu.findItem(itemId).setIcon(R.drawable.icon_menu_event_selected);
        } else {
            menu.findItem(R.id.navigation_home).setIcon(R.drawable.icon_menu_event);
        }

        if (itemId == R.id.navigation_route) {
            menu.findItem(itemId).setIcon(R.drawable.icon_menu_route_selected);
        } else {
            menu.findItem(R.id.navigation_route).setIcon(R.drawable.icon_menu_route);
        }

        if (itemId == R.id.navigation_news) {
            menu.findItem(itemId).setIcon(R.drawable.icon_menu_hub_selected);
        } else {
            menu.findItem(R.id.navigation_news).setIcon(R.drawable.icon_menu_hub);
        }

        if (itemId == R.id.navigation_group) {
            menu.findItem(itemId).setIcon(R.drawable.icon_menu_group_selected);
        } else {
            menu.findItem(R.id.navigation_group).setIcon(R.drawable.icon_menu_group);
        }

        if (itemId == R.id.navigation_find_pair) {
            menu.findItem(itemId).setIcon(R.drawable.icon_menu_pair_selected);
        } else {
            menu.findItem(R.id.navigation_find_pair).setIcon(R.drawable.icon_menu_pair);
        }
    }

    private void refreshTokenFCM() {
        final String token = FirebaseMessageReceiver.getToken(this);
        if (token == null) return;

        ContactFCMRequest contactFCMRequest = new ContactFCMRequest(token);
        contactApi.setTokenFCM(contactFCMRequest).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (!response.isSuccessful()) {
                    Log.e("updateTokenFCR", "error !response.isSuccessful()");
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable throwable) {
                Log.e("updateTokenFCR", "error throwable");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        if (menuItem != null) {
            View itemView = menuItem.getActionView();
            if (itemView != null) {
                itemView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
            }
        }
        calcNotificationIcon(App.notificationCount);
        calcChatMenuIcon();
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //TODO для API 10 мб и 11 нужно лочить кнопки чтобы не открывалось несколько окон
        if (item.getItemId() == R.id.action_profile) {
            if (App.checkGuestRolePopup(AppActivity.this, getSupportFragmentManager())) return true;
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (item.getItemId() == R.id.action_notification) {
            if (App.checkGuestRolePopup(AppActivity.this, getSupportFragmentManager())) return true;
            Intent notificationIntent = new Intent(this, NotificationActivity.class);
            startActivity(notificationIntent);
            return true;
        } else if (item.getItemId() == R.id.action_chat) {
            selectedBottomMenuItem(binding.navView, -1);
            setTitle(getString(R.string.nav_msg));
            openFragment(new ChatFragment());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }

        selectedBottomMenuItem(binding.navView, -1);

        int itemId = item.getItemId();
        if (itemId == R.id.nav_logout) {
            authApi.signout().enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {
                    App.clearLocalStorage(AppActivity.this);
                    runOnUiThread(() -> {
                        Intent loginIntent = new Intent(AppActivity.this, LoginActivity.class);
                        loginIntent.putExtra("auth", "false");
                        finish();
                        startActivity(loginIntent);
                    });
                }

                @Override
                public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable throwable) {

                }
            });
            return false;
        } else if (itemId == R.id.nav_road_help) { //Определяем, что открыть на помощи на дорогах
            roadHelpApi.getActualRoadHelp().enqueue(new Callback<IdentifierResponse>() {
                @Override
                public void onResponse(@NonNull Call<IdentifierResponse> call, @NonNull Response<IdentifierResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getId() == null || response.body().getId() < 1) {
                            runOnUiThread(() -> openFragment(new MotoRoadHelpFragment()));
                        } else {
                            runOnUiThread(() -> openFragment(new MotoRoadHelpCardFragment(response.body().getId())));
                        }
                    } else {
                        runOnUiThread(() -> openFragment(new MotoRoadHelpFragment()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<IdentifierResponse> call, @NonNull Throwable throwable) {
                    runOnUiThread(() -> openFragment(new MotoRoadHelpFragment()));
                }
            });
            showHidePairFragment();
            setTitle(getString(R.string.nav_road_help));
        } else if (itemId == R.id.nav_tow_truck) {
            getSupportActionBar().hide();
            openFragment(new TowTruckFragment());
        } else /*if (itemId == R.id.nav_service) {
            setTitle(getString(R.string.nav_service));
            openFragment(new MotoServiceFragment());
        } else*/ if (itemId == R.id.nav_competition) {
            setTitle(getString(R.string.nav_competition));
            openFragment(new MotoCompetitionFragment());
        } else if (itemId == R.id.nav_study) {
            getSupportActionBar().hide();
            openFragment(new MotoStudyFragment());
        } else /*if (itemId == R.id.nav_shops) {
            setTitle(getString(R.string.nav_shops));
            openFragment(new MotoShopFragment());
        } else*/ /*if (itemId == R.id.nav_storage) {
            setTitle(getString(R.string.nav_storage));
            openFragment(new MotoStorageFragment());
        } else*/ if (itemId == R.id.nav_setting) {
            setTitle(getString(R.string.nav_setting));
            openFragment(new SettingFragment());
        }/* else if (itemId == R.id.nav_subscribe) {
            setTitle(getString(R.string.nav_subscribe));
            openFragment(new SubscribeShopFragment());
        }*/ else if (itemId == R.id.nav_rating) {
            setTitle(getString(R.string.nav_rating));
            openFragment(new MotoRatingFragment());
        } if (itemId == R.id.nav_rating_moto) {
            setTitle(getString(R.string.nav_rating));
            openFragment(RatingMotorcycleFragment.Companion.newInstance());
        } else if (itemId == R.id.nav_feedback) {
            FeedbackBottomSheetFragment feedbackBottomSheetFragment = new FeedbackBottomSheetFragment();
            feedbackBottomSheetFragment.show(getSupportFragmentManager(), feedbackBottomSheetFragment.getTag());
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment, "tag");
        fragmentTransaction.commit();
        showHidePairFragment();
    }

    private int getCountNotification() {
        Call<CounterResponse> call = notificationApi.getCountNotification();
        Response<CounterResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().getCount() != null) {
                return response.body().getCount().intValue();
            } else {
                return 0;
            }
        } catch (Exception ex) {
            return 0;
        }
    }

    private Long getCountMessage() {
        Call<CounterResponse> call = chatApi.getCountUnreadMessage();
        Response<CounterResponse> response = null;
        try {
            response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().getCount() != null) {
                return response.body().getCount();
            } else {
                return 0L;
            }
        } catch (Exception ex) {
            return 0L;
        }
    }

    private void calcNotificationIcon(int count) {
        BadgeDrawable badge = BadgeDrawable.create(this);
        //badge.setBackgroundColor(getColor(R.color.moto_badge_notification));
        BadgeUtils.attachBadgeDrawable(badge, binding.appBarMain.toolbar, R.id.action_notification);
        App.badgeDrawableNotification = badge;
        App.calcNotificationIcon(count, getSharedPreferences(App.NOTIFICATION_INFO, MODE_PRIVATE));
    }

    private void calcChatMenuIcon() {
        BadgeDrawable badge = BadgeDrawable.create(this);
        BadgeUtils.attachBadgeDrawable(badge, binding.appBarMain.toolbar, R.id.action_chat);
        App.badgeDrawableChat = badge;
        App.calcNotificationChatIcon();
    }

    private void fillSidePanel() {
        SharedPreferences userInfo = getSharedPreferences(App.USER_INFO, MODE_PRIVATE);
        navLogin.setText(userInfo.getString("Username", ""));
        if (App.contactInfo != null && App.contactInfo.getNickName() != null && !App.contactInfo.getNickName().isEmpty()) {
            navNick.setText(App.contactInfo.getNickName());
        } else {
            SharedPreferences profileInfo = getSharedPreferences(App.PROFILE_INFO, MODE_PRIVATE);
            String json = profileInfo.getString("json", "");
            if (!json.isEmpty()) {
                UserInfoApi userInfoApi = new Gson().fromJson(json, UserInfoApi.class);
                navNick.setText(userInfoApi.getFirstName());
            }
        }

        if (App.contactInfo != null && App.contactInfo.getMiniAvatarId() != null) {
            CustomGlideApp.getContactImageByIdByte(navAvatar.getContext(), App.contactInfo.getMiniAvatarId(), navAvatar);
        }
    }

    /**
     * При возврате на главную страницу пересчитываем кол-во уведомлений на иконках
     */
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume");
        showHidePairFragment();

        new Thread(() -> {
            final int countNotification = getCountNotification();
            final Long countMessage = getCountMessage();
            App.calcNotificationIcon(countNotification);
            App.msgCountUnread = countMessage;
            App.calcNotificationChatIcon();
        }).start();
    }

    private void showHidePairFragment() {
        if (App.contactInfo != null && App.contactInfo.getIsWantFindPair() != null) {
            if (!App.contactInfo.getIsWantFindPair()) {
                binding.navView.findViewById(R.id.navigation_find_pair).setVisibility(View.GONE);
            } else {
                binding.navView.findViewById(R.id.navigation_find_pair).setVisibility(View.VISIBLE);
            }
        } else {
            binding.navView.findViewById(R.id.navigation_find_pair).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateUserLocation(location.getLatitude(), location.getLongitude(), AppActivity.this);
    }


    public void updateUserLocation(double _lat, double _lon, Context context) {
        if (context == null) return;
        double lat = _lat;
        double lon = _lon;
        double scale = Math.pow(10, 6);
        lat = Math.ceil(lat * scale) / scale;
        lon = Math.ceil(lon * scale) / scale;

        if (App.contactInfo != null) {
            App.contactInfo.setLatitude(lat);
            App.contactInfo.setLongitude(lon);
        }

        SharedPreferences settings = context.getSharedPreferences(App.USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("latitude", String.valueOf(lat));
        editor.putString("longitude", String.valueOf(lon));
        editor.apply();

        double finalLat = lat;
        double finalLon = lon;
        SetOnlinePositionRequest onlinePositionRequest = new SetOnlinePositionRequest();
        onlinePositionRequest.setLatitude(finalLat);
        onlinePositionRequest.setLongitude(finalLon);
        onlinePositionRequest.setUserLocale(Locale.getDefault().getLanguage().toUpperCase());
        onlinePositionRequest.setFirebaseToken(FirebaseMessageReceiver.getToken(context));
        onlinePositionRequest.setAppVersion(BuildConfig.VERSION_NAME);
        contactApi.setOnlinePosition(onlinePositionRequest).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<MessageResponse> call, @NonNull Response<MessageResponse> response) {

            }

            @Override
            public void onFailure(@NonNull Call<MessageResponse> call, @NonNull Throwable throwable) {

            }
        });
    }

    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(AppActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AppActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (App.contactInfo != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                Log.d(TAG, "getLastKnownLocation: " + location);
                App.contactInfo.setLatitude(location.getLatitude());
                App.contactInfo.setLongitude(location.getLongitude());
                new Thread(() -> {
                    updateUserLocation(location.getLatitude(), location.getLongitude(), AppActivity.this);
                }).start();
            }
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 0, locationListener);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }
    }

    @Override
    protected void onDestroy() {
        if (locationManager != null) {
            if (locationListener != null) {
                locationManager.removeUpdates(locationListener);
                locationManager.removeUpdates(this);
                locationListener = null;
            }
            locationManager = null;
        }
        fragmentManager = null;
        drawerLayout = null;
        mAppBarConfiguration = null;
        binding = null;
        App.fragmentManager = null;
        App.sideMenu = null;
        super.onDestroy();
    }
}