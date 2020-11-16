package com.teamig.imgraphy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.tool.GlideApp;

import java.util.List;

public class GraphyListAdapter extends RecyclerView.Adapter<GraphyListAdapter.ViewHolder> {

    private boolean isOnLoading;
    private boolean isEndOfRequest;

    private List<ImgraphyType.Graphy> graphyList;
    private RecyclerView graphyListView;
    private OnItemClickListener onItemClickListener;

    public GraphyListAdapter() {
        onItemClickListener = null;
        isOnLoading = false;
        isEndOfRequest = false;
    }

    public GraphyListAdapter(RecyclerView graphyListView) {
        this.graphyListView = graphyListView;
        onItemClickListener = null;
        isOnLoading = false;
        isEndOfRequest = false;
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

        GlideApp.with(holder.view)
                .load("https://api.novang.tk/imgraphy/files/thumb/" + graphy.uuid)
                .placeholder(R.drawable.ic_image)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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
        if (graphyList == null) {

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
        isOnLoading = false;
        isEndOfRequest = false;
        graphyList = list;
        notifyDataSetChanged();
    }

    public void putList(List<ImgraphyType.Graphy> list) {
        if (list.size() == 0) {
            isEndOfRequest = true;
        } else if (list != null && !isEndOfRequest) {
            int lastSize = graphyList.size();

            graphyList.addAll(list);
            notifyItemRangeInserted(lastSize, list.size());
        }
    }

    public void setOnLoading(boolean bool) {
        isOnLoading = bool;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, ImgraphyType.Graphy graphy);
    }

    public interface OnScrollLastItemListener {
        void onItemClick(GraphyListAdapter adapter);
    }

    public void setOnItemClickListener(OnItemClickListener i) {
        this.onItemClickListener = i;
    }

    public void setOnScrollLastItemListener(OnScrollLastItemListener onScrollLastItemListener) {
        graphyListView.clearOnScrollListeners();
        graphyListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                GraphyListAdapter listAdapter = (GraphyListAdapter) recyclerView.getAdapter();
                int[] lastVisibleItemPositions = new int[2];
                layoutManager.findLastCompletelyVisibleItemPositions(lastVisibleItemPositions);
                int lastItemPosition = Math.max(lastVisibleItemPositions[0], lastVisibleItemPositions[1]);

                if(listAdapter.getItemCount() - 1 == lastItemPosition && !isOnLoading && !isEndOfRequest) {
                    listAdapter.setOnLoading(true);
                    onScrollLastItemListener.onItemClick(listAdapter);
                }
            }
        });
    }
}
