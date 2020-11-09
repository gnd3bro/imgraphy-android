package com.teamig.imgraphy.ui.graphy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.teamig.imgraphy.adapter.GraphyListAdapter;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.ui.viewer.ViewerFragmentDirections;

public class GraphyFragment extends Fragment {

    private GraphyViewModel viewModel;
    private View root;
    private NavController navController;

    private ImageButton graphyListRefresh;
    private EditText graphySearchInput;
    private ImageButton graphyClearInput;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(GraphyViewModel.class);
        root = inflater.inflate(R.layout.fragment_graphy, container, false);
        navController = Navigation.findNavController(container);

        initReferences();
        initObservers();
        initEvents();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.userID.postValue(GraphyFragmentArgs.fromBundle(getArguments()).getUserID());
        viewModel.keyword.postValue(GraphyFragmentArgs.fromBundle(getArguments()).getKeyword());
    }

    private void initReferences() {
        graphyListRefresh = (ImageButton) root.findViewById(R.id.GraphyListRefresh);
        graphySearchInput = (EditText) root.findViewById(R.id.GraphySearchInput);
        graphyClearInput = (ImageButton) root.findViewById(R.id.GraphyClearInput);

        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter();
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        graphyListLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);
    }

    private void initObservers() {
        viewModel.keyword.observe(getViewLifecycleOwner(), s -> {
            graphySearchInput.setText(s);
            refreshList(new ImgraphyType.Options.List(50, 0, s));
        });
    }

    private void initEvents() {
        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            navController.navigate(ViewerFragmentDirections.actionGlobalNavigationViewer(viewModel.userID.getValue(), new ImgraphyType.ParcelableGraphy(graphy)));
        });

        graphyListRefresh.setOnClickListener(v -> {
            refreshList(new ImgraphyType.Options.List(50, 0, graphySearchInput.getText().toString()));
        });

        graphyClearInput.setOnClickListener(v -> {
            graphySearchInput.setText("");
        });
    }

    private void refreshList(ImgraphyType.Options.List option) {
        viewModel.getGraphy(option).observe(getViewLifecycleOwner(), graphy -> {
            graphyListAdapter.updateList(graphy);
            graphyListAdapter.notifyDataSetChanged();
            graphyListView.scrollToPosition(0);
        });
    }
}