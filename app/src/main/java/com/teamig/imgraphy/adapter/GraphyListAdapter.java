package com.teamig.imgraphy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class GraphyListAdapter extends RecyclerView.Adapter<GraphyListAdapter.ViewHolder> {

    private List<ImgraphyType.Graphy> graphyList;

    private OnItemClickListener onItemClickListener;

    public GraphyListAdapter() {
        this.onItemClickListener = null;
    }

    @NonNull
    @Override
    public GraphyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_graphy_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView listItemImage = (ImageView) holder.view.findViewById(R.id.ListItemImage);
        TextView listItemShareCount = (TextView) holder.view.findViewById(R.id.ListItemShareCount);
        TextView listItemFavCount = (TextView) holder.view.findViewById(R.id.ListItemFavCount);

        ImgraphyType.Graphy graphy = graphyList.get(position);


        /* TODO: Neumorphism Library Bug */
        // Temporary Work
        // Soup's Neumorphism Library has performance issue with recycle view
        Glide.with(holder.view)
                .load("https://api.novang.tk/imgraphy/files/thumb/" + graphy.uuid)
                .transform(new GranularRoundedCorners(22, 22, 0, 0))
                .placeholder(R.drawable.ic_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(Target.SIZE_ORIGINAL)
                .into(listItemImage);

        listItemShareCount.setText(String.valueOf(graphy.shrcnt));
        listItemFavCount.setText(String.valueOf(graphy.favcnt));

        holder.view.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, graphy);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (graphyList== null) {

            return 0;
        }

        return graphyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void updateList(List<ImgraphyType.Graphy> list) {
        graphyList = list;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View v, ImgraphyType.Graphy graphy);
    }

    public void setOnItemClickListener(OnItemClickListener i) {
        this.onItemClickListener = i;
    }
}
