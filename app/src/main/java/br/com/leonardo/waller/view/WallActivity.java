package br.com.leonardo.waller.view;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.List;

import br.com.leonardo.waller.Preferences;
import br.com.leonardo.waller.R;
import br.com.leonardo.waller.WallAdapter;
import br.com.leonardo.waller.model.Photo;
import br.com.leonardo.waller.presenter.WallPresenter;

public class WallActivity extends AppCompatActivity {

    private WallPresenter mWallPresenter;
    private RecyclerView mList;

    private int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        mList = (RecyclerView) findViewById(R.id.list_colors);
        mList.setHasFixedSize(true);
        mList.setLayoutManager(new GridLayoutManager(WallActivity.this, 2, GridLayoutManager.VERTICAL, false));

        mWallPresenter = new WallPresenter(this, new WallView() {
            @Override
            public void onPhotosLoaded(List<Photo> photos) {
                savePhotos(photos);
                if (mList.getAdapter() == null) {
                    mList.setAdapter(new WallAdapter(photos, onClickListener));
                } else {
                    currentPage++;
                    ((WallAdapter) mList.getAdapter()).addNewPage(photos);
                }
            }
        });

        mWallPresenter.fetchPhotos(currentPage);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v != null) {
                Intent intent = new Intent(WallActivity.this, DetailActivity.class);
                intent.putExtra("urls", ((String) v.getTag()));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(WallActivity.this, v.findViewById(R.id.image), "target").toBundle());
            } else {
                mWallPresenter.fetchPhotos(currentPage + 1);
            }
        }
    };

    public void savePhotos(List<Photo> photos) {
        Preferences.saveSharedPreference(WallActivity.this, Preferences.RANDOM_BUCKET, new Gson().toJson(photos));
    }
}

