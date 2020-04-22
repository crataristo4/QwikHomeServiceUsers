package com.users.qwikhomeservices.activities.home.fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.activities.home.bottomsheets.EditItemBottomSheet;
import com.users.qwikhomeservices.activities.home.bottomsheets.VerifyPhoneBottomSheet;
import com.users.qwikhomeservices.databinding.FragmentEditProfileBinding;
import com.users.qwikhomeservices.utils.DisplayViewUI;
import com.users.qwikhomeservices.utils.MyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding fragmentEditProfileBinding;
    private ProfilePhotoEditFragment profilePhotoEditFragment = new ProfilePhotoEditFragment();
    private Uri uri;
    private long mLastClickTime = 0;
    private CircleImageView profilePhoto;
    private OnFragmentInteractionListener mListener;
    private StorageReference mStorageReference;
    private DatabaseReference usersDbAccountDbRef;
    private String uid, getImageUri, name;

    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(getActivity()).setTitle("Profile");
        // Inflate the layout for this fragment
        fragmentEditProfileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false);
        return fragmentEditProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mStorageReference = FirebaseStorage.getInstance().getReference("photos");
        uid = MainActivity.uid;
        usersDbAccountDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(uid);


        fragmentEditProfileBinding
                .fabUploadPhoto.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fadein));
        profilePhoto = fragmentEditProfileBinding.imgUploadPhoto;
        profilePhoto.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_transition_animation));

        profilePhoto.setOnClickListener(v -> {
            FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fadein, R.anim.scale_out)
                    .replace(R.id.containerSettings, profilePhotoEditFragment)
                    .addToBackStack(null)
                    .commit();


        });

        fragmentEditProfileBinding.fabUploadPhoto.setOnClickListener(v -> openGallery());

        fragmentEditProfileBinding.txtFirstName.setText(MainActivity.firstName);
        fragmentEditProfileBinding.txtLastName.setText(MainActivity.lastName);
        fragmentEditProfileBinding.txtPhoneNumber.setText(MainActivity.mobileNumber);
        String imageUrl = MainActivity.imageUrl;
        if (imageUrl == null) {
            Glide.with(Objects.requireNonNull(getActivity())).load(getActivity().getResources().getDrawable(R.drawable.photoe)).into(profilePhoto);
        } else {
            Glide.with(Objects.requireNonNull(getActivity())).load(imageUrl).into(profilePhoto);
        }
        fragmentEditProfileBinding.nameLayout.setOnClickListener(//open bottom sheet to edit name
                this::onClick);

        fragmentEditProfileBinding.lastNameLayout.setOnClickListener(
                //open bottom sheet to edit about
                this::onClick);


        fragmentEditProfileBinding.editPhoneLayout.setOnClickListener(this::onClick);

    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 16)
                .start(Objects.requireNonNull(getContext()), this);
    }

    private void onClick(View v) {
        Bundle bundle = new Bundle();
        EditItemBottomSheet editItemBottomSheet = new EditItemBottomSheet();
        VerifyPhoneBottomSheet verifyPhoneBottomSheet = new VerifyPhoneBottomSheet();

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }

        mLastClickTime = SystemClock.elapsedRealtime();


        if (v.getId() == R.id.nameLayout) {

            name = String.valueOf(fragmentEditProfileBinding.txtFirstName.getText());
            bundle.putString(MyConstants.FIRST_NAME, name);
                editItemBottomSheet.setArguments(bundle);
            editItemBottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), MyConstants.FIRST_NAME);



        } else if (v.getId() == R.id.lastNameLayout) {
            name = String.valueOf(fragmentEditProfileBinding.txtLastName.getText());
            bundle.putString(MyConstants.LAST_NAME, name);
            editItemBottomSheet.setArguments(bundle);
            editItemBottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), MyConstants.LAST_NAME);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            getActivity();
            if (resultCode == Activity.RESULT_OK) {
                assert result != null;
                uri = result.getUri();

                Glide.with(Objects.requireNonNull(getActivity()))
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profilePhoto);

                uploadFile();

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // progressDialog.dismiss();
                assert result != null;
                String error = result.getError().getMessage();
                DisplayViewUI.displayToast(getActivity(), error);
            }
        }
    }

    private void uploadFile() {
        if (uri != null) {
            ProgressDialog progressDialog = DisplayViewUI.displayProgress(getActivity(), "updating profile picture please wait...");
            progressDialog.show();

            // final File thumb_imageFile = new File(Objects.requireNonNull(uri.getPath()));

          /*  try {
                Bitmap thumb_imageBitmap = new Compressor(Objects.requireNonNull(getActivity()))
                        .setMaxHeight(130)
                        .setMaxWidth(13)
                        .setQuality(100)
                        .compressToBitmap(thumb_imageFile);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }*/

            //  file path for the itemImage
            final StorageReference fileReference = mStorageReference.child(uid + "." + uri.getLastPathSegment());

            fileReference.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    DisplayViewUI.displayToast(getActivity(), Objects.requireNonNull(task.getException()).getMessage());

                }
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {

                    Uri downLoadUri = task.getResult();
                    assert downLoadUri != null;
                    getImageUri = downLoadUri.toString();

                    Map<String, Object> updatePhoto = new HashMap<>();
                    updatePhoto.put("image", getImageUri);

                    usersDbAccountDbRef.updateChildren(updatePhoto).addOnCompleteListener(task12 -> {
                        if (task12.isSuccessful()) {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(getActivity(), "Successful");
                        } else {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(getActivity(), Objects.requireNonNull(task12.getException()).getMessage());

                        }
                    });


                } else {
                    progressDialog.dismiss();
                    DisplayViewUI.displayToast(getActivity(), Objects.requireNonNull(task.getException()).getMessage());

                }

            });
        }
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
