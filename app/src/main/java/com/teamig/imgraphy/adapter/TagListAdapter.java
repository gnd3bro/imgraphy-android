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
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> {

    private List<String> tagList;

    private View.OnClickListener onItemClickListener;

    @NonNull
    @Override
    public TagListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_tag_list_item, parent, false);

        if (onItemClickListener != null) {
            v.setOnClickListener(onItemClickListener);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView listItemTag = (TextView) holder.view.findViewById(R.id.ListItemTag);

        listItemTag.setText(tagList.get(position));
    }

    @Override
    public int getItemCount() {
        if (tagList== null) {

            return 0;
        }

        return tagList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public void updateList(List<String> list) {
        tagList = list;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onItemClickListener = onClickListener;
    }
}
