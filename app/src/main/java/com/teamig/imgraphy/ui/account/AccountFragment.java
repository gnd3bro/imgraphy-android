package com.teamig.imgraphy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import org.w3c.dom.Text;

public class AccountFragment extends Fragment {


    private AccountViewModel viewModel;
    private View root;
    private NavController navController;

    private Button myUploadListButton;
    private Button myLikedListButton;

    private TextView graphyListEmpty;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

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
        myUploadListButton = (Button) root.findViewById(R.id.MyUploadListButton);
        myLikedListButton = (Button) root.findViewById(R.id.MyLikedListButton);

        graphyListEmpty = (TextView) root.findViewById(R.id.GraphyListEmpty);

        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter(graphyListView);
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        graphyListLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);
    }

    private void initObservers() {
        viewModel.userID.observe(getViewLifecycleOwner(), s -> {
            myUploadListButton.callOnClick();
        });
    }

    private void initEvents() {
        myUploadListButton.setOnClickListener(v -> {
            myUploadListButton.setTextColor(getContext().getColor(R.color.neumorphism_Accent));
            myLikedListButton.setTextColor(getContext().getColor(R.color.neumorphism_Stroke));

            viewModel.getGraphy(new ImgraphyType.Options.List(30, 0, null, null))
                    .observe(getViewLifecycleOwner(), graphy -> {
                        if (graphy.size() > 0) {
                            graphyListAdapter.updateList(graphy);
                            graphyListView.scrollToPosition(0);
                            graphyListEmpty.setVisibility(View.GONE);
                            graphyListView.setVisibility(View.VISIBLE);
                        } else {
                            graphyListView.setVisibility(View.GONE);
                            graphyListEmpty.setVisibility(View.VISIBLE);
                        }
            });

            graphyListAdapter.setOnScrollLastItemListener(adapter -> {
                viewModel.getGraphy(new ImgraphyType.Options.List(30, adapter.getItemCount(), null, null))
                        .observe(getViewLifecycleOwner(), graphies -> {
                            adapter.putList(graphies);
                            adapter.setOnLoading(false);
                        });
            });
        });

        myLikedListButton.setOnClickListener(v -> {
            myLikedListButton.setTextColor(getContext().getColor(R.color.neumorphism_Accent));
            myUploadListButton.setTextColor(getContext().getColor(R.color.neumorphism_Stroke));

            viewModel.getLiked(new ImgraphyType.Options.List(30, 0, null, null))
                    .observe(getViewLifecycleOwner(), graphy -> {
                        if (graphy.size() > 0) {
                            graphyListAdapter.updateList(graphy);
                            graphyListView.scrollToPosition(0);
                            graphyListEmpty.setVisibility(View.GONE);
                            graphyListView.setVisibility(View.VISIBLE);
                        } else {
                            graphyListView.setVisibility(View.GONE);
                            graphyListEmpty.setVisibility(View.VISIBLE);
                        }
                    });

            graphyListAdapter.setOnScrollLastItemListener(adapter -> {
                viewModel.getLiked(new ImgraphyType.Options.List(30, adapter.getItemCount(), null, null))
                        .observe(getViewLifecycleOwner(), graphies -> {
                            adapter.putList(graphies);
                            adapter.setOnLoading(false);
                        });
            });
        });

        graphyListAdapter.setOnItemClickListener((v, graphy) -> {
            navController.navigate(ViewerFragmentDirections.actionGlobalNavigationViewer(viewModel.userID.getValue(), new ImgraphyType.ParcelableGraphy(graphy)));
        });
    }

}