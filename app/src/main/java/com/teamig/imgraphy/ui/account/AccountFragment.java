package com.teamig.imgraphy.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.GraphyListAdapter;
import com.teamig.imgraphy.service.ImgraphyType;

public class AccountFragment extends Fragment {

    private AccountViewModel viewModel;
    private View root;

    private RecyclerView graphyListView;
    private GraphyListAdapter graphyListAdapter;
    private StaggeredGridLayoutManager graphyListLayoutManager;

    private TextView imgraphyUserId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        root = inflater.inflate(R.layout.fragment_account, container, false);

        graphyListView = (RecyclerView) root.findViewById(R.id.GraphyListView);
        graphyListAdapter = new GraphyListAdapter();
        graphyListLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        graphyListView.setHasFixedSize(true);
        graphyListView.setAdapter(graphyListAdapter);
        graphyListView.setLayoutManager(graphyListLayoutManager);

        imgraphyUserId = (TextView) root.findViewById(R.id.ImgraphyUserId);

        viewModel.getUserID().observe(getViewLifecycleOwner(), s -> {
            imgraphyUserId.setText(s);

            viewModel.getGraphy(new ImgraphyType.Options.List(50, 0, s))
                    .observe(getViewLifecycleOwner(), graphy -> {
                        graphyListAdapter.updateList(graphy);
                        graphyListAdapter.notifyDataSetChanged();
                        graphyListView.scrollToPosition(0);
                    });
        });

        return root;
    }
}