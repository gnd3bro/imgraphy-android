package com.teamig.imgraphy.ui.recommend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.GraphyListAdapter;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.ui.viewer.ViewerFragmentDirections;

public class RecommendFragment extends Fragment {

    private RecommendViewModel viewModel;
    private View root;
    private NavController navController;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(RecommendViewModel.class);
        root = inflater.inflate(R.layout.fragment_recommend, container, false);
        navController = Navigation.findNavController(container);

        initReferences();
        initObservers();
        initEvents();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.userID.postValue(RecommendFragmentArgs.fromBundle(getArguments()).getUserID());
    }

    private void initReferences() {
        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter();
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        graphyListLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);
    }

    private void initObservers() {
        viewModel.userID.observe(getViewLifecycleOwner(), s -> {
            viewModel.getRecommend(new ImgraphyType.Options.List(20, 0, null, null))
                    .observe(getViewLifecycleOwner(), graphy -> {
                        graphyListAdapter.updateList(graphy);
                        graphyListAdapter.notifyDataSetChanged();
                        graphyListView.scrollToPosition(0);
                    });
        });
    }

    private void initEvents() {
        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            navController.navigate(ViewerFragmentDirections.actionGlobalNavigationViewer(viewModel.userID.getValue(), new ImgraphyType.ParcelableGraphy(graphy)));
        });
    }
}