package com.teamig.imgraphy.adapter;

import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.teamig.imgraphy.R;
import com.teamig.imgraphy.service.ImgraphyType;

import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListAdapter.ViewHolder> {

    private List<String> tagList;

    private OnItemClickListener onItemClickListener;

    public TagListAdapter() {
        this.onItemClickListener = null;
    }

    @NonNull
    @Override
    public TagListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_tag_list_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView listItemTag = (TextView) holder.view.findViewById(R.id.ListItemTag);

        listItemTag.setText(tagList.get(position));

        holder.view.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(v, tagList.get(position));
            }
        });
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

    public interface OnItemClickListener {
        void onItemClick(View v, String s);
    }

    public void setOnItemClickListener(OnItemClickListener i) {
        this.onItemClickListener = i;
    }
}
