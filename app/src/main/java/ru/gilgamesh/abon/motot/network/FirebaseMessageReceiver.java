package ru.gilgamesh.abon.motot.network;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.core.app.NotificationCompat;

import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.gilgamesh.abon.motot.AppActivity;
import ru.gilgamesh.abon.motot.MainActivity;
import ru.gilgamesh.abon.motot.R;
import ru.gilgamesh.abon.motot.data.api.ContactApi;
import ru.gilgamesh.abon.motot.model.App;
import ru.gilgamesh.abon.motot.payload.request.contact.ContactFCMRequest;
import ru.gilgamesh.abon.motot.payload.response.MessageResponse;
import ru.gilgamesh.abon.motot.ui.bottomNav.chat.ChatCardActivity;
import ru.gilgamesh.abon.motot.ui.notification.NotificationActivity;
import ru.gilgamesh.abon.motot.ui.sideNav.motoCompetition.MotoCompetitionDetailActivity;

@AndroidEntryPoint
public class FirebaseMessageReceiver extends FirebaseMessagingService {
    @Inject
    ContactApi contactApi;
    public static final String TAG = FirebaseMessageReceiver.class.getSimpleName();
    private static final String channel_id = "notification_channel"; // Assign channel ID

    public static String getToken(Context context) {
        return context.getSharedPreferences(App.FCM_INFO, MODE_PRIVATE).getString("fb", "empty");
    }

    public void clearDeviceNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences(App.FCM_INFO, MODE_PRIVATE).edit().putString("fb", s).apply();
        ContactFCMRequest contactFCMRequest = new ContactFCMRequest(s);
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

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly
            // from FCM, the title and the body can be
            // fetched directly as below.
            Map<String, String> data = remoteMessage.getData();
            if (data.containsKey("type")) {
                final String title = remoteMessage.getNotification().getTitle();
                final String body = remoteMessage.getNotification().getBody();
                String type = data.get("type");
                if (type == null) type = "";

                switch (type) {
                    case "message" -> {
                        String chatId = data.get("chatId");
                        Long chatIdL = chatId == null || chatId.isEmpty() ? -1L : Long.valueOf(chatId);
                        String chatName = data.get("chatName");
                        String companionId = data.get("fromId");
                        Long companionIdL = companionId == null || companionId.isEmpty() ? -1L : Long.valueOf(companionId);

                        if (!App.context.equals("chat_" + chatId)) {
                            Intent intent;
                            if (isAppOnForeground(this)) {
                                App.msgCountUnread++;
                                App.calcNotificationChatIcon();
                                intent = new Intent(this, ChatCardActivity.class);
                            } else {
                                intent = new Intent(this, MainActivity.class);
                            }
                            intent.putExtra("chatId", chatIdL);
                            intent.putExtra("chatName", chatName);
                            intent.putExtra("companionId", companionIdL);
                            showNotification(intent, title, body, FirebaseMessageReceiverType.MESSAGE);
                        }
                    }
                    case "COMPETITION_NEW_MEMBER", "COMPETITION_APPROVE_MEMBER" -> {
                        String competitionId = data.get("id");
                        Long competitionIdL = competitionId == null || competitionId.isEmpty() ? -1L : Long.valueOf(competitionId);
                        Intent intent;
                        if (isAppOnForeground(this)) {
                            intent = new Intent(this, MotoCompetitionDetailActivity.class);
                        } else {
                            intent = new Intent(this, MainActivity.class);
                        }
                        intent.putExtra("competitionId", competitionIdL);
                        intent.putExtra("prevActivity", TAG);
                        showNotification(intent, title, body, FirebaseMessageReceiverType.COMPETITION);
                    }
                    case "GROUP_INVITE", "GROUP_CHANGE_BASE", "GROUP_RIDING_MEMBER" -> {
                        Intent intent;
                        if (isAppOnForeground(this)) {
                            intent = new Intent(this, AppActivity.class);
                        } else {
                            intent = new Intent(this, MainActivity.class);
                        }
                        intent.putExtra("type", type);
                        intent.putExtra("id", data.get("id"));
                        showNotification(intent, title, body, FirebaseMessageReceiverType.NOTIFICATION);
                    }
                    default -> {
                        Intent intent;
                        if (isAppOnForeground(this)) {
                            intent = new Intent(this, NotificationActivity.class);
                        } else {
                            intent = new Intent(this, MainActivity.class);
                        }
                        showNotification(intent, title, body, FirebaseMessageReceiverType.NOTIFICATION);
                        App.calcNotificationIcon(App.notificationCount + 1);
                    }
                }
            }

            //clearDeviceNotification(0);
        }
    }

    // Method to display the notifications
    public void showNotification(Intent _intent, String title, String message, FirebaseMessageReceiverType firebaseMessageReceiverType) {
        // Pass the intent to switch to the MainActivity
        Intent intent = _intent;
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id).setSmallIcon(R.drawable.logo_app).setAutoCancel(true).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setOnlyAlertOnce(true)
                //.setNumber(App.notificationCount + App.msgCountUnread.intValue())
                .setContentIntent(pendingIntent);

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        /*if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(
                    getCustomDesign(title, message));
        }*/ // If Android Version is lower than Jelly Beans,
        // customized layout cannot be used and thus the
        // layout is set as follows
        //else {
        builder = builder.setContentTitle(title).setContentText(message);
        //.setSmallIcon(R.drawable.gfg);
        //}
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the Android Version is greater than Oreo
        NotificationChannel notificationChannel = new NotificationChannel(channel_id, firebaseMessageReceiverType.getType(), NotificationManager.IMPORTANCE_HIGH);
        notificationManager.createNotificationChannel(notificationChannel);

        notificationManager.notify(firebaseMessageReceiverType.getCode(), builder.build());
    }

    // Метод для проверки, открыто ли приложение
    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
