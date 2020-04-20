package com.users.qwikhomeservices.activities.home.about;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.users.qwikhomeservices.databinding.ActivityFinishAccountSetUpBinding;
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


public class FinishAccountSetUpActivity extends AppCompatActivity {

    private static int INTERVAL = 3000;
    private long mBackPressed;
    private static final String TAG = "FinishAccountSetUp";
    private DatabaseReference usersDbRef;
    private StorageReference mStorageReference;
    private ActivityFinishAccountSetUpBinding activityFinishAccountSetUpBinding;
    private String about, getImageUri, uid, mGetFirstName, mGetLatName, mGetFullName;
    private Uri uri;
    private CircleImageView profileImage;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFinishAccountSetUpBinding = DataBindingUtil.setContentView(this, R.layout.activity_finish_account_set_up);

        profileImage = activityFinishAccountSetUpBinding.imgUploadPhoto;
        usersDbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Users")
                .child(uid);
        mStorageReference = FirebaseStorage.getInstance().getReference("photos");
        activityFinishAccountSetUpBinding.btnFinish.setOnClickListener(this::onClick);
        activityFinishAccountSetUpBinding.fabUploadPhoto.setOnClickListener(v -> openGallery());
        profileImage.setOnClickListener(v -> openGallery());


    }

    private void openGallery() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(16, 16)
                .start(Objects.requireNonNull(FinishAccountSetUpActivity.this));
    }

    private void onClick(View view) {
        TextInputLayout txtFirstName = activityFinishAccountSetUpBinding.txtFirstName;
        TextInputLayout txtLastName = activityFinishAccountSetUpBinding.txtLastName;

        mGetFirstName = Objects.requireNonNull(txtFirstName.getEditText()).getText().toString();
        mGetLatName = Objects.requireNonNull(txtLastName.getEditText()).getText().toString();
        mGetFullName = mGetFirstName.concat(" ").concat(mGetLatName);

        if (uri == null) {
            DisplayViewUI.displayToast(this, "Please select a photo to upload");

        }


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
            updateAccount();

        }


    }

    private void updateAccount() {
        if (uri != null) {
            ProgressDialog progressDialog = DisplayViewUI.displayProgress(FinishAccountSetUpActivity.this, "please wait...");
            progressDialog.show();

            final File thumb_imageFile = new File(Objects.requireNonNull(uri.getPath()));

            try {
                Bitmap thumb_imageBitmap = new Compressor(Objects.requireNonNull(FinishAccountSetUpActivity.this))
                        .setMaxHeight(130)
                        .setMaxWidth(13)
                        .setQuality(100)
                        .compressToBitmap(thumb_imageFile);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

            } catch (IOException e) {
                e.printStackTrace();
            }

            //                file path for the itemImage
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

                    String mobileNumber = MainActivity.firebaseUser.getPhoneNumber();
                    String uid = MainActivity.uid;

                    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:MM a");
                    String dateJoined = dateFormat.format(Calendar.getInstance().getTime());

                   /* Map<String, Object> updateProfile = new HashMap<>();
                    updateProfile.put("image", getImageUri);
                    updateProfile.put("about", about);
                    updateProfile.put("firstName", mGetFirstName);
                    updateProfile.put("lastName", mGetLatName);
                    updateProfile.put("fullName", mGetFullName);
                    updateProfile.put("accountType", mGetAccountType);
                    updateProfile.put("mobileNumber",mobileNumber);
                    updateProfile.put("servicePersonId",uid);*/


                    Users users = new Users(uid,
                            mGetFirstName,
                            mGetLatName,
                            mGetFullName,
                            mobileNumber,
                            getImageUri, dateJoined);

                    usersDbRef.setValue(users).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(FinishAccountSetUpActivity.this, "Successfully updated");
                            finish();


                        } else {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(FinishAccountSetUpActivity.this, Objects.requireNonNull(task1.getException()).getMessage());

                        }

                    });

                } else {
                    progressDialog.dismiss();
                    DisplayViewUI.displayToast(FinishAccountSetUpActivity.this, Objects.requireNonNull(task.getException()).getMessage());

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
                Log.i(TAG, "URI: " + uri);
                Glide.with(Objects.requireNonNull(FinishAccountSetUpActivity.this))
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // progressDialog.dismiss();
                assert result != null;
                String error = result.getError().getMessage();
                DisplayViewUI.displayToast(FinishAccountSetUpActivity.this, error);
            }
        }

    }


    @Override
    public void onBackPressed() {

        if (mBackPressed + INTERVAL > System.currentTimeMillis()) {
            return;
        }

        else DisplayViewUI.displayToast(this, "Please complete your profile");
        mBackPressed = System.currentTimeMillis();
        // todo fix back pressed

    }
}
