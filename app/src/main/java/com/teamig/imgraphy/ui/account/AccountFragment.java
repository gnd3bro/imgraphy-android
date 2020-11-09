package com.teamig.imgraphy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.GraphyListAdapter;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentArgs;
import com.teamig.imgraphy.ui.viewer.ViewerFragmentDirections;

public class AccountFragment extends Fragment {

    private String userID;

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
        initEvents();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userID = AccountFragmentArgs.fromBundle(getArguments()).getUserID();
        imgraphyUserId.setText(userID);

        viewModel.getGraphy(new ImgraphyType.Options.List(50, 0, userID))
                .observe(getViewLifecycleOwner(), graphy -> {
                    graphyListAdapter.updateList(graphy);
                    graphyListAdapter.notifyDataSetChanged();
                    graphyListView.scrollToPosition(0);
                });
    }

    private void initReferences() {
        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter();
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);

        imgraphyUserId = (TextView) root.findViewById(R.id.ImgraphyUserId);
    }

    private void initEvents() {
        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            navController.navigate(ViewerFragmentDirections.actionGlobalNavigationViewer(userID, new ImgraphyType.ParcelableGraphy(graphy)));
        });
    }
}