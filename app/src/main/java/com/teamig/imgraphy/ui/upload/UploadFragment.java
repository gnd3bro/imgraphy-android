package com.teamig.imgraphy.ui.upload;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.teamig.imgraphy.R;

public class UploadFragment extends Fragment {

    private UploadViewModel viewModel;
    private View root;
    private NavController navController;
    private ActivityResultLauncher<String> getContentActivityLauncher;

    private ImageView uploadPreview;
    private Button uploadSelectImage;
    private EditText uploadFormTag;
    private RadioGroup uploadFormLicense;
    private Button uploadFormButton;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel.userID.postValue(UploadFragmentArgs.fromBundle(getArguments()).getUserID());
    }

    private void initReferences() {
        getContentActivityLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), this::getContentResult);
        uploadPreview = (ImageView) root.findViewById(R.id.UploadPreview);
        uploadSelectImage = (Button) root.findViewById(R.id.UploadSelectImage);
        uploadFormTag = (EditText) root.findViewById(R.id.UploadFormTag);
        uploadFormLicense = (RadioGroup) root.findViewById(R.id.UploadFormLicense);
        uploadFormButton = (Button) root.findViewById(R.id.UploadFormButton);
        uploadFormButton.setVisibility(View.GONE);
    }

    private void initObservers() {
        viewModel.fileUri.observe(getViewLifecycleOwner(), uri -> {
            viewModel.fileToByteArray(uri);
        });

        viewModel.fileByteData.observe(getViewLifecycleOwner(), bytes -> {
            Glide.with(root)
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

        uploadFormButton.setOnClickListener(v -> {
            int indexOfRadio = uploadFormLicense.indexOfChild(root.findViewById(uploadFormLicense.getCheckedRadioButtonId()));

            viewModel.uploadFile(uploadFormTag.getText().toString(), indexOfRadio)
                    .observe(getViewLifecycleOwner(), result -> {
                        Toast.makeText(this.getContext(), result.code + ": " + result.log, Toast.LENGTH_LONG).show();
                        if (result.code.equals("success")) {
                            navController.navigate(UploadFragmentDirections.actionGlobalNavigationUpload(viewModel.userID.getValue()));
                        }
                    });
        });
    }

    private void getContentResult(Uri result) {
        if (result != null) {
            viewModel.fileUri.postValue(result);
        }
    }
}