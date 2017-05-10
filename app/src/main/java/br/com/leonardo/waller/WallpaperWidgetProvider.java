package br.com.leonardo.waller;

import android.app.PendingIntent;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Random;

import br.com.leonardo.waller.model.Photo;

/**
 * Created by Leonardo de Matos on 26/04/17.
 */

public class WallpaperWidgetProvider extends AppWidgetProvider {

    public static String UPDATE_WALLPAPER_ACTION = "UPDATE_WALLPAPER_ACTION";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int widgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_wallpaper);

            Intent intent = new Intent(context, WallpaperWidgetProvider.class);
            intent.setAction(UPDATE_WALLPAPER_ACTION);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            remoteViews.setOnClickPendingIntent(R.id.update_button, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(UPDATE_WALLPAPER_ACTION)) {
            final WallpaperManager myWallpaperManager
                    = WallpaperManager.getInstance(context);
            Type listType = new TypeToken<Photo[]>() {
            }.getType();

            Photo[] photo = new Gson().fromJson(Preferences.getSharedPreference(context, Preferences.RANDOM_BUCKET), listType);
            Random r = new Random();
            int i1 = r.nextInt(10 - 1) + 1;
            Toast.makeText(context, "Carregando...", Toast.LENGTH_LONG).show();
            Glide.with(context)
                    .load(photo[i1].urls.regular)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                            Toast.makeText(context, "Pronto!", Toast.LENGTH_LONG).show();
                            try {
                                myWallpaperManager.setBitmap(resource);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }
}