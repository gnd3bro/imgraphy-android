package com.teamig.imgraphy.ui.graphy;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.adapter.GraphyListAdapter;
import com.teamig.imgraphy.adapter.TagListAdapter;
import com.teamig.imgraphy.service.Imgraphy;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.tool.TagParser;

import java.util.List;

public class GraphyFragment extends Fragment {

    private GraphyViewModel viewModel;
    private View root;

    private Button graphyListRefresh;
    private EditText graphySearchInput;
    private Button graphyClearInput;
    private ScrollView graphyViewContainer;
    private ImageView graphyViewImage;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

    private RecyclerView tagListView;
    private TagListAdapter tagListAdapter;
    private StaggeredGridLayoutManager tagListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GraphyViewModel.class);
        root = inflater.inflate(R.layout.fragment_graphy, container, false);

        graphyListRefresh = (Button) root.findViewById(R.id.GraphyListRefresh);
        graphySearchInput = (EditText) root.findViewById(R.id.GraphySearchInput);
        graphyClearInput = (Button) root.findViewById(R.id.GraphyClearInput);
        graphyViewContainer = (ScrollView) root.findViewById(R.id.GraphyViewContainer);
        graphyViewImage = (ImageView) root.findViewById(R.id.GraphyViewImage);

        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter();
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);

        tagListView = (RecyclerView) root.findViewById(R.id.TagListView);
        tagListAdapter = new TagListAdapter();
        tagListLayoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.HORIZONTAL);

        tagListView.setHasFixedSize(true);
        tagListView.setAdapter(tagListAdapter);
        tagListView.setLayoutManager(tagListLayoutManager);

        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            List<String> tagList = TagParser.parse(graphy.tag);
            String url = "https://api.novang.tk/imgraphy/files/img/" + graphy.uuid + "/" + graphy.uuid;

            Glide.with(root)
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL)
                    .into(graphyViewImage);

            tagListAdapter.updateList(tagList);
            graphyViewContainer.setVisibility(View.VISIBLE);
        });

        graphyListRefresh.setOnClickListener(v -> {
            refreshList(new ImgraphyType.Options.List(50, 0, graphySearchInput.getText().toString()));
        });

        refreshList(new ImgraphyType.Options.List(50, 0, graphySearchInput.getText().toString()));

        graphyClearInput.setOnClickListener(v -> {
            graphySearchInput.setText("");
        });

        return root;
    }

    private void refreshList(ImgraphyType.Options.List option) {
        viewModel.getGraphy(option)
                .observe(getViewLifecycleOwner(), graphy -> {
                    graphyListAdapter.updateList(graphy);
                    graphyListAdapter.notifyDataSetChanged();
                    graphyListView.scrollToPosition(0);
                });
    }
}