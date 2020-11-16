package com.teamig.imgraphy.ui.account;

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

public class AccountFragment extends Fragment {


    private AccountViewModel viewModel;
    private View root;
    private NavController navController;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

    private TextView imgraphyUserId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        root = inflater.inflate(R.layout.fragment_account, container, false);
        navController = Navigation.findNavController(container);

        initReferences();
        initObservers();
        initEvents();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.userID.postValue(AccountFragmentArgs.fromBundle(getArguments()).getUserID());
    }

    private void initReferences() {
        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter(graphyListView);
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        graphyListLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);

        imgraphyUserId = (TextView) root.findViewById(R.id.ImgraphyUserId);
    }

    private void initObservers() {
        viewModel.userID.observe(getViewLifecycleOwner(), s -> {
            viewModel.getGraphy(new ImgraphyType.Options.List(30, 0, s))
                    .observe(getViewLifecycleOwner(), graphy -> {
                        graphyListAdapter.updateList(graphy);
                        graphyListAdapter.notifyDataSetChanged();
                        graphyListView.scrollToPosition(0);
                    });
        });
    }

    private void initEvents() {
        graphyListAdapter.setOnScrollLastItemListener(adapter -> {
            viewModel.getGraphy(new ImgraphyType.Options.List(30, adapter.getItemCount(), viewModel.userID.getValue()))
                    .observe(getViewLifecycleOwner(), graphies -> {
                        adapter.putList(graphies);
                        adapter.setOnLoading(false);
                    });
        });

        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            navController.navigate(ViewerFragmentDirections.actionGlobalNavigationViewer(viewModel.userID.getValue(), new ImgraphyType.ParcelableGraphy(graphy)));
        });
    }
}