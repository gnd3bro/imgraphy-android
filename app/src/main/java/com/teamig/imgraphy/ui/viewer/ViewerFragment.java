package com.teamig.imgraphy.ui.viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.TagListAdapter;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentDirections;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

public class ViewerFragment extends Fragment {

    private ViewerViewModel viewModel;
    private View root;
    private NavController navController;

    private ImageView viewerImage;
    private TextView viewerShareCount;
    private TextView viewerFavCount;
    private TextView viewerLicense;
    private FrameLayout viewerUserIdContainer;
    private TextView viewerUserId;
    private Button viewerDeprecateButton;

    private RecyclerView tagListView;
    private TagListAdapter tagListAdapter;
    private FlowLayoutManager tagListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ViewerViewModel.class);
        root = inflater.inflate(R.layout.fragment_viewer, container, false);
        navController = Navigation.findNavController(container);

        initReferences();
        initObservers();
        initEvents();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.userID.postValue(ViewerFragmentArgs.fromBundle(getArguments()).getUserID());
        viewModel.graphy.postValue(ViewerFragmentArgs.fromBundle(getArguments()).getParcelableGraphy().graphy);
    }

    private void initReferences() {
        viewerImage = (ImageView) root.findViewById(R.id.ViewerImage);
        viewerShareCount = (TextView) root.findViewById(R.id.ViewerShareCount);
        viewerFavCount = (TextView) root.findViewById(R.id.ViewerFavCount);
        viewerLicense = (TextView) root.findViewById(R.id.ViewerLicense);
        viewerUserIdContainer = (FrameLayout) root.findViewById(R.id.ViewerUserIdContainer) ;
        viewerUserId = (TextView) root.findViewById(R.id.ViewerUserId);
        viewerDeprecateButton = (Button) root.findViewById(R.id.ViewerDeprecateButton);

        tagListView = (RecyclerView) root.findViewById(R.id.TagListView);
        tagListAdapter = new TagListAdapter();
        tagListLayoutManager = new FlowLayoutManager();
        tagListLayoutManager.setAutoMeasureEnabled(true);
        tagListLayoutManager.setAlignment(Alignment.LEFT);

        tagListView.setHasFixedSize(true);
        tagListView.setAdapter(tagListAdapter);
        tagListView.setLayoutManager(tagListLayoutManager);
    }

    private void initObservers() {
        viewModel.graphy.observe(getViewLifecycleOwner(), graphy -> {
            Glide.with(root)
                    .load("https://api.novang.tk/imgraphy/files/img/" + graphy.uuid + "/" + graphy.uuid)
                    .placeholder(R.drawable.ic_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL)
                    .into(viewerImage);

            if (viewModel.userID.getValue().equals(viewModel.graphy.getValue().uploader)) {
                viewerDeprecateButton.setVisibility(View.VISIBLE);
            }

            viewModel.parseTag(graphy.tag).observe(getViewLifecycleOwner(), strings -> {
                tagListAdapter.updateList(strings);
            });

            viewerShareCount.setText(String.valueOf(graphy.shrcnt));
            viewerFavCount.setText(String.valueOf(graphy.favcnt));
            viewerUserId.setText(graphy.uploader);
            viewModel.parseLicense(graphy.license).observe(getViewLifecycleOwner(), integer -> {
                viewerLicense.setText(integer);
            });
        });

        viewModel.keyword.observe(getViewLifecycleOwner(), s -> {
            navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(viewModel.userID.getValue(), s));
        });
    }

    private void initEvents() {
        tagListAdapter.setOnItemClickListener((v, s) -> {
            viewModel.keyword.postValue(s);
        });

        viewerUserIdContainer.setOnClickListener(v -> {
            viewModel.keyword.postValue(viewModel.graphy.getValue().uploader);
        });

        viewerDeprecateButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());

            builder.setTitle("경고");
            builder.setMessage("이미지를 삭제하시겠습니까?");

            builder.setPositiveButton("삭제", (dialog, which) -> {
                viewModel.deprecateGraphy(true, viewModel.graphy.getValue().uuid)
                        .observe(getViewLifecycleOwner(), result -> {
                            Toast.makeText(this.getContext(), result.code + ": " + result.log, Toast.LENGTH_LONG).show();
                            navController.popBackStack();
                });
            });

            builder.setNegativeButton("취소", (dialog, which) -> {
            });

            builder.show();
        });
    }
}