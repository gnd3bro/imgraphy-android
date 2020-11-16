package com.teamig.imgraphy.ui.upload;

import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.R;
import com.teamig.imgraphy.adapter.TagListAdapter;
import com.teamig.imgraphy.tool.GlideApp;
import com.xiaofeng.flowlayoutmanager.Alignment;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import java.util.List;

public class UploadFragment extends Fragment {

    private UploadViewModel viewModel;
    private View root;
    private NavController navController;
    private ActivityResultLauncher<String> getContentActivityLauncher;

    private ImageView uploadPreview;
    private Button uploadSelectImage;
    private EditText uploadFormTagEdit;
    private ImageButton uploadFormTagAddButton;
    private RadioGroup uploadFormLicense;
    private Button uploadFormButton;

    private RecyclerView tagListView;
    private TagListAdapter tagListAdapter;
    private FlowLayoutManager tagListLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(UploadViewModel.class);
        root = inflater.inflate(R.layout.fragment_upload, container, false);
        navController = Navigation.findNavController(container);

        initReferences();
        initObservers();
        initEvents();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        viewModel.userID.postValue(UploadFragmentArgs.fromBundle(getArguments()).getUserID());
    }

    private void initReferences() {
        getContentActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::getContentResult);
        uploadPreview = (ImageView) root.findViewById(R.id.UploadPreview);
        uploadSelectImage = (Button) root.findViewById(R.id.UploadSelectImage);
        uploadFormTagEdit = (EditText) root.findViewById(R.id.UploadFormTagEdit);
        uploadFormTagAddButton = (ImageButton) root.findViewById(R.id.UploadFormTagAddButton);
        uploadFormLicense = (RadioGroup) root.findViewById(R.id.UploadFormLicense);
        uploadFormButton = (Button) root.findViewById(R.id.UploadFormButton);
        uploadFormButton.setVisibility(View.GONE);

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
        viewModel.inputTagList.observe(getViewLifecycleOwner(), strings -> {
            tagListAdapter.updateList(strings);
        });

        viewModel.fileUri.observe(getViewLifecycleOwner(), uri -> {
            viewModel.fileToByteArray(getActivity().getContentResolver(), uri);
        });

        viewModel.fileByteData.observe(getViewLifecycleOwner(), bytes -> {
            GlideApp.with(root)
                    .load(bytes)
                    .placeholder(R.drawable.ic_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL)
                    .into(uploadPreview);

            uploadFormButton.setVisibility(View.VISIBLE);
        });
    }

    private void initEvents() {
        uploadSelectImage.setOnClickListener(v -> {
            getContentActivityLauncher.launch("image/*");
        });

        uploadFormTagEdit.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP
                    && !((EditText) v).getText().toString().isEmpty()) {

                uploadFormTagAddButton.callOnClick();
            }

            return false;
        });

        uploadFormTagAddButton.setOnClickListener(v -> {
            List<String> tagList = viewModel.inputTagList.getValue();
            String inputTag = uploadFormTagEdit.getText().toString();

            if (!inputTag.isEmpty() && tagList.contains(inputTag)) {
                Toast.makeText(this.getContext(), R.string.ui_already_added_tag, Toast.LENGTH_SHORT).show();

                return ;
            }

            tagList.add(inputTag);
            uploadFormTagEdit.setText("");
            viewModel.inputTagList.postValue(tagList);
        });

        uploadFormButton.setOnClickListener(v -> {
            if(viewModel.inputTagList.getValue().isEmpty()) {
                Toast.makeText(this.getContext(), R.string.ui_alert_add_tag_more, Toast.LENGTH_SHORT).show();
                return ;
            }

            int indexOfRadio = uploadFormLicense.indexOfChild(root.findViewById(uploadFormLicense.getCheckedRadioButtonId()));

            viewModel.uploadFile(indexOfRadio)
                    .observe(getViewLifecycleOwner(), result -> {
                        Toast.makeText(this.getContext(), result.log, Toast.LENGTH_LONG).show();
                        if (result.code.equals("success")) {
                            navController.navigate(UploadFragmentDirections.actionGlobalNavigationUpload(viewModel.userID.getValue()));
                        }
                    });
        });

        tagListAdapter.setOnItemClickListener((v, s) -> {
            List<String> tagList = viewModel.inputTagList.getValue();
            tagList.remove(s);
            viewModel.inputTagList.postValue(tagList);
        });
    }

    private void getContentResult(Uri result) {
        if (result != null) {
            viewModel.fileUri.postValue(result);
        }
    }
}