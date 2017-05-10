package br.com.leonardo.waller.presenter;

import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.leonardo.waller.model.Photo;
import br.com.leonardo.waller.model.WallerService;
import br.com.leonardo.waller.model.WallerServiceCreator;
import br.com.leonardo.waller.view.WallView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Leonardo de Matos on 29/04/17.
 */

public class WallPresenter {

    @SuppressWarnings("all")
    private final String CLIENT_ID = "82acb689fad50cfcaa115b2d531f8f355480c8711b827edc09c59e658b8bb38d";

    private WallView mView;
    private WallerService mWallerService;

    public WallPresenter(Context context, WallView view) {
        mView = view;
        mWallerService = new WallerServiceCreator().create(context);
    }

    public void fetchPhotos(int page) {
        mWallerService
                .photos(CLIENT_ID, page, 40)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Photo>>() {
                    @Override
                    public void onCompleted() {
                        Log.i(this.getClass().getCanonicalName(), "There was a response");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(this.getClass().getCanonicalName(), "Error : ", e);
                    }

                    @Override
                    public void onNext(List<Photo> photo) {
                        mView.onPhotosLoaded(photo);
                    }
                });
    }
}
