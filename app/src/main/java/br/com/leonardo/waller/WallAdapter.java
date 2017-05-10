package br.com.leonardo.waller;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.util.List;

import br.com.leonardo.waller.model.Photo;

/**
 * Created by Leonardo de Matos on 09/04/17.
 */

public class WallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Photo> mPhotos;
    private View.OnClickListener mClickListener;

    public WallAdapter(List<Photo> photos, View.OnClickListener listener) {
        this.mClickListener = listener;
        this.mPhotos = photos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WallViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mClickListener);
        holder.itemView.setTag(new Gson().toJson(mPhotos.get(position)));
        Glide.with(holder.itemView.getContext())
                .load(mPhotos.get(position).urls.regular)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(((ImageView) holder.itemView.findViewById(R.id.image)));

        paintBackground(position, holder.itemView.findViewById(R.id.image));

        if (position == getItemCount() - 10) {
            mClickListener.onClick(null);
        }
    }

    private void paintBackground(int position, View view) {
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), (position % 5) == 0 ? R.color.black : R.color.blackish));
    }

    public void addNewPage(List<Photo> photos) {
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    private class WallViewHolder extends RecyclerView.ViewHolder {
        WallViewHolder(View itemView) {
            super(itemView);
        }
    }
}
