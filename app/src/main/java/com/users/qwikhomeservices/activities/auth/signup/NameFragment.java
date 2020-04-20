package com.users.qwikhomeservices.activities.auth.signup;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.databinding.FragmentNameBinding;
import com.users.qwikhomeservices.utils.DisplayViewUI;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class NameFragment extends Fragment {
    private FragmentNameBinding fragmentNameBinding;
    private DatabaseReference usersDbRef;
    private StorageReference mStorageReference;
    private String getImageUri, uid, mGetFirstName, mGetLatName, mGetFullName;
    private Uri uri;
    private CircleImageView profileImage;

    public NameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentNameBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_name, container, false);

        return fragmentNameBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentNameBinding.btnFinish.setOnClickListener(this::validateInput);
        profileImage = fragmentNameBinding.imgUploadPhoto;
        //service type database
        usersDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(uid);
        mStorageReference = FirebaseStorage.getInstance().getReference("photos");
        fragmentNameBinding.btnFinish.setOnClickListener(this::validateInput);
        fragmentNameBinding.fabUploadPhoto.setOnClickListener(v -> openGallery());
        profileImage.setOnClickListener(v -> openGallery());




    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 16)
                .start(Objects.requireNonNull(getContext()), this);
    }

    private void validateInput(View view) {

        if (uri == null) {
            DisplayViewUI.displayToast(getActivity(), "Please select a photo to upload");

        }

        TextInputLayout txtFirstName = fragmentNameBinding.txtFirstName;
        TextInputLayout txtLastName = fragmentNameBinding.txtLastName;

        mGetFirstName = Objects.requireNonNull(txtFirstName.getEditText()).getText().toString();
        mGetLatName = Objects.requireNonNull(txtLastName.getEditText()).getText().toString();
        String fullName = mGetFirstName.concat(" ").concat(mGetLatName);


        if (mGetFirstName.trim().isEmpty() || mGetFirstName.length() < 3) {
            txtFirstName.setErrorEnabled(true);
            txtFirstName.setError("first name required");
        } else {
            txtFirstName.setErrorEnabled(false);

        }
        if (mGetLatName.trim().isEmpty() || mGetLatName.length() < 3) {
            txtLastName.setErrorEnabled(true);
            txtLastName.setError("last name required");
        } else {
            txtLastName.setErrorEnabled(false);

        }

        if (!mGetFirstName.trim().isEmpty() && !mGetLatName.trim().isEmpty() && uri != null) {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                assert result != null;
                uri = result.getUri();
                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // progressDialog.dismiss();
                assert result != null;
                String error = result.getError().getMessage();
                DisplayViewUI.displayToast(getActivity(), error);
            }
        }

    }

}
