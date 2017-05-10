package br.com.leonardo.waller;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.leonardo.waller.view.DetailActivity;

/**
 * Created by Leonardo de Matos on 26/04/17.
 */

public class ImageDownloadTask extends AsyncTask<String, Integer, String> {

    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private DetailActivity.ImageDownloadCallback mDownloadCallback;
    private Bitmap mPhoto;
    private boolean isSave;

    public ImageDownloadTask(Context context, Bitmap bitmap, boolean isSave, DetailActivity.ImageDownloadCallback callback) {
        this.mContext = context;
        mDownloadCallback = callback;
        this.mPhoto = bitmap;
        this.isSave = isSave;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        if (isSave) {
            savePicture(mPhoto);
        } else {
            setWallpaper(mPhoto);
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();

    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mDownloadCallback.onFinish();
    }

    private void savePicture(Bitmap resource) {
        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Waller");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            File file = new File(folder.toString(), "wallpaper-" + UUID.randomUUID().toString() + ".png"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                resource.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void setWallpaper(Bitmap resource) {
        try {
            WallpaperManager.getInstance(mContext).setBitmap(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

