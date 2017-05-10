package br.com.leonardo.waller.view;

import java.util.List;

import br.com.leonardo.waller.model.Photo;

public interface WallView {
    void onPhotosLoaded(List<Photo> photos);
}
