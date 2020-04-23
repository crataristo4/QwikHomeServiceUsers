package com.users.qwikhomeservices.activities.home.about;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.databinding.ActivityProfileBinding;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.DisplayViewUI;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private static final int DELAY = 2000;
    private static long backPressed;
    private Uri uri;
    private DatabaseReference usersDbRef;
    private ActivityProfileBinding activityProfileBinding;
    private StorageReference mStorageReference;
    private String getImageUri, uid, mGetFirstName, mGetLatName, mGetFullName, mMobileNumber;
    private CircleImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        mStorageReference = FirebaseStorage.getInstance().getReference("photos");

        usersDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users");

        profileImage = activityProfileBinding.imgUploadPhoto;


        activityProfileBinding.fabUploadPhoto.setOnClickListener(this::openGallery);
        profileImage.setOnClickListener(this::openGallery);

        activityProfileBinding.btnFinish.setOnClickListener(this::validateInput);


    }


    private void openGallery(View view) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 16)
                .start(Objects.requireNonNull(ProfileActivity.this));
    }

    private void validateInput(View view) {

        if (uri == null) {
            DisplayViewUI.displayToast(this, "Please select a photo to upload");

        }

        TextInputLayout txtFirstName = activityProfileBinding.txtFirstName;
        TextInputLayout txtLastName = activityProfileBinding.txtLastName;

        mGetFirstName = Objects.requireNonNull(txtFirstName.getEditText()).getText().toString();
        mGetLatName = Objects.requireNonNull(txtLastName.getEditText()).getText().toString();


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

            upDateAccount();
        }
    }


    private void upDateAccount() {
        if (uri != null) {
            ProgressDialog progressDialog = DisplayViewUI.displayProgress(this, "please wait...");
            progressDialog.show();

            final File thumb_imageFile = new File(Objects.requireNonNull(uri.getPath()));

            try {
                Bitmap thumb_imageBitmap = new Compressor(this)
                        .setMaxHeight(130)
                        .setMaxWidth(13)
                        .setQuality(100)
                        .compressToBitmap(thumb_imageFile);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //file path for the itemImage
            final StorageReference fileReference = mStorageReference.child(uid + "." + uri.getLastPathSegment());

            fileReference.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();

                    //throw task.getException();
                    Log.d(TAG, "then: " + Objects.requireNonNull(task.getException()).getMessage());

                }
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downLoadUri = task.getResult();
                    assert downLoadUri != null;
                    getImageUri = downLoadUri.toString();

                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:MM a");
                    String dateJoined = dateFormat.format(Calendar.getInstance().getTime());

                    uid = MainActivity.uid;
                    mGetFullName = mGetFirstName.concat(" ").concat(mGetLatName);
                    mMobileNumber = MainActivity.firebaseUser.getPhoneNumber();


                    Users users = new Users(uid, getImageUri, mGetFirstName, mGetLatName, mGetFullName, mMobileNumber, dateJoined);


                    usersDbRef.child(uid).setValue(users).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(this, "Profile updated");

                            finish();

                        } else {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(this, Objects.requireNonNull(task1.getException()).getMessage());

                        }

                    });

                } else {
                    progressDialog.dismiss();
                    DisplayViewUI.displayToast(this, Objects.requireNonNull(task.getException()).getMessage());

                }

            });
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
                Glide.with(Objects.requireNonNull(ProfileActivity.this))
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // progressDialog.dismiss();
                assert result != null;
                String error = result.getError().getMessage();
                DisplayViewUI.displayToast(ProfileActivity.this, error);
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (backPressed + DELAY > System.currentTimeMillis()) {

        } else {
            DisplayViewUI.displayToast(this, "please complete the registration process");
        }
        backPressed = System.currentTimeMillis();
    }
}
