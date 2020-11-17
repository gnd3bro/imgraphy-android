package com.teamig.imgraphy.ui.viewer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.TagListAdapter;
import com.teamig.imgraphy.service.ImgraphyType;
import com.teamig.imgraphy.tool.GlideApp;
import com.teamig.imgraphy.ui.graphy.GraphyFragmentDirections;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ViewerFragment extends Fragment {

    private ViewerViewModel viewModel;
    private View root;
    private NavController navController;

    private ImageView viewerImage;
    private TextView viewerShareCount;
    private TextView viewerFavCount;
    private ImageButton viewerLinkButton;
    private Button viewerShareButton;
    private ImageButton viewerFavButton;
    private TextView viewerLicense;
    private TextView viewerDate;
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
    public void onResume() {
        super.onResume();

        viewModel.userID.postValue(ViewerFragmentArgs.fromBundle(getArguments()).getUserID());
        viewModel.graphy.postValue(ViewerFragmentArgs.fromBundle(getArguments()).getParcelableGraphy().graphy);
    }

    private void initReferences() {
        viewerImage = (ImageView) root.findViewById(R.id.ViewerImage);
        viewerShareCount = (TextView) root.findViewById(R.id.ViewerShareCount);
        viewerFavCount = (TextView) root.findViewById(R.id.ViewerFavCount);
        viewerLinkButton = (ImageButton) root.findViewById(R.id.ViewerLinkButton);
        viewerShareButton = (Button) root.findViewById(R.id.ViewerShareButton);
        viewerFavButton = (ImageButton) root.findViewById(R.id.ViewerFavButton);
        viewerLicense = (TextView) root.findViewById(R.id.ViewerLicense);
        viewerDate = (TextView) root.findViewById(R.id.ViewerDate);
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
            if (viewModel.graphyUrl.getValue() == null) {
                viewModel.graphyUrl.postValue("https://api.novang.tk/imgraphy/files/img/"
                        .concat(graphy.uuid).concat("/").concat(graphy.uuid).concat(".").concat(graphy.ext));

                if (viewModel.userID.getValue().equals(viewModel.graphy.getValue().uploader)) {
                    viewerDeprecateButton.setVisibility(View.VISIBLE);
                }

                viewModel.parseTag(graphy.tag).observe(getViewLifecycleOwner(), strings -> {
                    tagListAdapter.updateList(strings);
                });

                viewerUserId.setText(graphy.uploader);

                viewModel.parseLicense(graphy.license).observe(getViewLifecycleOwner(), integer -> {
                    viewerLicense.setText(integer);
                });

                viewerDate.setText(new SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm", Locale.KOREA)
                        .format(new Date(graphy.date * 1000L)));

                viewModel.checkVoteGraphy().observe(getViewLifecycleOwner(), result -> {
                    viewModel.isVoted.postValue(Integer.parseInt(result.log) > 0);
                });
            }

            viewerShareCount.setText(String.valueOf(graphy.shrcnt));
            viewerFavCount.setText(String.valueOf(graphy.favcnt));
        });

        viewModel.graphyUrl.observe(getViewLifecycleOwner(), s -> {
            GlideApp.with(root)
                    .load(s)
                    .placeholder(R.drawable.ic_image)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .override(Target.SIZE_ORIGINAL)
                    .into(viewerImage);
        });

        viewModel.isVoted.observe(getViewLifecycleOwner(), b -> {
            if (b) {
                viewerFavButton.setColorFilter(getContext().getColor(R.color.neumorphism_Accent));
            } else {
                viewerFavButton.setColorFilter(getContext().getColor(R.color.neumorphism_Stroke));
            }
        });

        viewModel.keyword.observe(getViewLifecycleOwner(), s -> {
            navController.navigate(GraphyFragmentDirections.actionGlobalNavigationGraphy(viewModel.userID.getValue(), s));
        });
    }

    private void initEvents() {
        viewerLinkButton.setOnClickListener(v -> {
            viewModel.shareCount();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, viewModel.graphyUrl.getValue());
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, getString(R.string.ui_share)));
        });

        viewerShareButton.setOnClickListener(v -> {
            viewModel.shareCount();
            GlideApp.with(root)
                    .downloadOnly()
                    .load(viewModel.graphyUrl.getValue())
                    .into(new CustomTarget<File>() {
                        @Override
                        public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                            viewModel.copyCacheToFile(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), resource)
                                    .observe(getViewLifecycleOwner(), uri -> {
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                                        intent.setType("image/*");
                                        startActivity(Intent.createChooser(intent, getString(R.string.ui_share)));
                                    });
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) { }
                    });
        });

        viewerFavButton.setOnClickListener(v -> {
            ImgraphyType.Graphy graphy = viewModel.graphy.getValue();

            viewModel.voteGraphy().observe(getViewLifecycleOwner(), result -> {
                        Toast.makeText(this.getContext(), result.log, Toast.LENGTH_LONG).show();
                        viewModel.checkVoteGraphy().observe(getViewLifecycleOwner(), resultc -> {
                            viewModel.isVoted.postValue(Integer.parseInt(resultc.log) > 0);
                        });
                    });
        });

        viewerUserIdContainer.setOnClickListener(v -> {
            viewModel.keyword.postValue(viewModel.graphy.getValue().uploader);
        });

        viewerDeprecateButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());

            builder.setTitle(R.string.ui_image_delete_dialog_title);
            builder.setMessage(R.string.ui_image_delete_dialog_msg);

            builder.setPositiveButton(R.string.ui_image_delete_dialog_confirm, (dialog, which) -> {
                viewModel.deprecateGraphy(true, viewModel.graphy.getValue().uuid)
                        .observe(getViewLifecycleOwner(), result -> {
                            Toast.makeText(this.getContext(), result.log, Toast.LENGTH_LONG).show();
                            navController.popBackStack();
                });
            });

            builder.setNegativeButton(R.string.ui_image_delete_dialog_cancel, (dialog, which) -> {
            });

            builder.show();
        });

        tagListAdapter.setOnItemClickListener((v, s) -> {
            viewModel.keyword.postValue(s);
        });
    }

}