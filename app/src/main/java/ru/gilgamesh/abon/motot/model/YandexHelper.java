package ru.gilgamesh.abon.motot.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

import java.util.List;

import ru.gilgamesh.abon.motot.payload.LocationPoint;

public class YandexHelper {
    private static final String TAG = YandexHelper.class.getSimpleName();

    public static void buildRouteToPoint(Double latitude, Double longitude, Context context) {
        if (context == null || latitude == null || longitude == null) return;
        String helpLat = String.valueOf(latitude);
        String helpLon = String.valueOf(longitude);

        SharedPreferences sharedPreferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        final String lat = sharedPreferences.getString("latitude", "");
        final String lon = sharedPreferences.getString("longitude", "");
        if (!lat.isEmpty() && !lon.isEmpty()) {
            String routeParam = "?ll=" + lon + "%2C" + lat + "&rtext=" + lat + "%2C" + lon + "~" + helpLat + "%2C" + helpLon;
            routeParam += "&rtt=auto&z=11&l=map";

            PackageManager packageManager = context.getPackageManager();
            final String yandexLinkNavi = "yandexnavi://build_route_on_map?" + "lat_to=" + helpLat + "&lon_to=" + helpLon;
            Log.d("yandexLinkNavi", yandexLinkNavi);
            Uri uriNavi = Uri.parse(yandexLinkNavi);
            Intent intentYaNavi = new Intent(Intent.ACTION_VIEW, uriNavi);
            intentYaNavi.setPackage("ru.yandex.yandexnavi");
            if (!packageManager.queryIntentActivities(intentYaNavi, PackageManager.MATCH_ALL).isEmpty()) {
                context.startActivity(intentYaNavi);
            } else {
                final String yandexLinkMap = "yandexmaps://maps.yandex.ru/" + routeParam;
                Log.d(TAG, yandexLinkMap);
                Uri uriMap = Uri.parse(yandexLinkMap);
                Intent intentYaMap = new Intent(Intent.ACTION_VIEW, uriMap);
                intentYaMap.setPackage("ru.yandex.yandexmaps");
                if (!packageManager.queryIntentActivities(intentYaMap, PackageManager.MATCH_ALL).isEmpty()) {
                    context.startActivity(intentYaMap);
                } else {
                    final String yandexLink = "https://yandex.ru/maps/" + routeParam;
                    Log.d(TAG, yandexLink);
                    Uri uri = Uri.parse(yandexLink);
                    // Открываем страницу приложения Яндекс.Карты в Google Play.
                    Intent intentUrl = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intentUrl);
                }
            }
        }
    }

    public static void buildRouteByPoints(Double latitude, Double longitude, List<LocationPoint> listPoint, Context context) {
        if (context == null || latitude == null || longitude == null || listPoint == null || listPoint.isEmpty())
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(App.USER_INFO, Context.MODE_PRIVATE);
        final String lat = sharedPreferences.getString("latitude", "");
        final String lon = sharedPreferences.getString("longitude", "");

        PackageManager packageManager = context.getPackageManager();
        StringBuilder naviParam = new StringBuilder("yandexnavi://build_route_on_map?lat_from=").append(lat).append("&lon_from=").append(lon).append("&lat_to=").append(listPoint.get(listPoint.size() - 1).getLatitude()).append("&lon_to=").append(listPoint.get(listPoint.size() - 1).getLongitude());
        for (int i = 0; i < listPoint.size() - 1; i++) {
            naviParam.append("&lat_via_").append(i).append("=").append(listPoint.get(i).getLatitude()).append("&lon_via_").append(i).append("=").append(listPoint.get(i).getLongitude());
        }
        Log.d("yandexLinkNavi", naviParam.toString());
        Uri uriNavi = Uri.parse(naviParam.toString());
        Intent intentYaNavi = new Intent(Intent.ACTION_VIEW, uriNavi);
        intentYaNavi.setPackage("ru.yandex.yandexnavi");
        if (!packageManager.queryIntentActivities(intentYaNavi, PackageManager.MATCH_ALL).isEmpty()) {
            context.startActivity(intentYaNavi);
        } else {
            StringBuilder routeParam = new StringBuilder("?ll=" + lon + "%2C" + lat + "&mode=routes&rtext=");
            routeParam.append(lat).append("%2C").append(lon).append("~");
            for (int i = 0; i < listPoint.size(); i++) {
                routeParam.append(listPoint.get(i).getLatitude()).append("%2C").append(listPoint.get(i).getLongitude());
                if (i + 1 != listPoint.size()) {
                    routeParam.append("~");
                }
            }
            routeParam.append("&rtt=auto&z=11&l=map");

            final String yandexLinkMap = "yandexmaps://maps.yandex.ru/" + routeParam;
            Log.d("yandexLinkMap", yandexLinkMap);
            Uri uriMap = Uri.parse(yandexLinkMap);
            Intent intentYaMap = new Intent(Intent.ACTION_VIEW, uriMap);
            intentYaMap.setPackage("ru.yandex.yandexmaps");
            if (packageManager.queryIntentActivities(intentYaMap, PackageManager.MATCH_ALL).size() > 0) {
                context.startActivity(intentYaMap);
            } else {
                final String yandexLink = "https://yandex.ru/maps/" + routeParam;
                Log.d("yandexuri", yandexLink);
                Uri uri = Uri.parse(yandexLink);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        }

    }
}
