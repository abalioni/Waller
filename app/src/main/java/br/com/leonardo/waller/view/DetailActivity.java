package br.com.leonardo.waller.view;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;

import br.com.leonardo.waller.ImageDownloadTask;
import br.com.leonardo.waller.R;
import br.com.leonardo.waller.model.Photo;

/**
 * Created by Leonardo de Matos on 09/04/17.
 */

public class DetailActivity extends AppCompatActivity {

    private Photo mPhoto;
    private Bitmap mLoadedBitmap;
    boolean isSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        mPhoto = new Gson().fromJson(getIntent().getExtras().getString("urls"), Photo.class);

        Glide.with(this)
                .load(mPhoto.user.profile_image.large)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ImageView) findViewById(R.id.profile_image)));

        Glide.with(this)
                .load(mPhoto.urls.regular)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        ((ImageView) findViewById(R.id.image)).setImageBitmap(resource);
                        mLoadedBitmap = resource;
                    }
                });

        ((TextView) findViewById(R.id.user_name)).setText(mPhoto.user.name);
        ((TextView) findViewById(R.id.username)).setText("@" + mPhoto.user.username);

        findViewById(R.id.like_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
            }
        });

        findViewById(R.id.download_wallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = true;
                checkWifi();
            }
        });
        findViewById(R.id.set_wallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave = false;
                checkPermissions();

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        checkPermissions();
    }

    private void checkWifi() {
        if (!isWifiOn()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Atenção");
            builder.setMessage("Você está no 3G deseja continuar o download?");
            builder.setCancelable(true);
            builder.setPositiveButton("DOWNLOAD", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkPermissions();
                }
            });
            builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();
        } else {
            checkPermissions();
        }
    }

    private void checkPermissions() {
        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheckWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheckStorage != PackageManager.PERMISSION_GRANTED || permissionCheckWriteStorage != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        } else {
            download(isSave);
        }
    }

    private void download(final  boolean isSave) {
        findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.like_button).setVisibility(View.GONE);
        Glide.with(DetailActivity.this)
                .load(isSave ? mPhoto.urls.raw : mPhoto.urls.full)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {

                        final ImageDownloadTask downloadTask = new ImageDownloadTask(DetailActivity.this, mLoadedBitmap, isSave, new ImageDownloadCallback() {
                            @Override
                            public void onFinish() {
                                findViewById(R.id.progress_bar).setVisibility(View.GONE);
                                findViewById(R.id.like_button).setVisibility(View.VISIBLE);
                            }
                        });
                        downloadTask.execute();
                    }
                });
    }


    public boolean isWifiOn() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();
    }

    public interface ImageDownloadCallback {
        void onFinish();
    }
}


