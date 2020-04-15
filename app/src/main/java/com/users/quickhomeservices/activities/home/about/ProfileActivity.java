package com.users.quickhomeservices.activities.home.about;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.home.MainActivity;
import com.users.quickhomeservices.activities.home.bottomsheets.EditItemBottomSheet;
import com.users.quickhomeservices.activities.home.bottomsheets.VerifyPhoneBottomSheet;
import com.users.quickhomeservices.databinding.ActivityProfileBinding;
import com.users.quickhomeservices.utils.DisplayViewUI;
import com.users.quickhomeservices.utils.MyConstants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    private Uri uri;
    private ActivityProfileBinding activityProfileBinding;
    private long mLastClickTime = 0;
    private StorageReference mStorageReference;
    private String uid, about, getImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile);


        MainActivity.retrieveSingleUserDetails(activityProfileBinding.txtUserName, activityProfileBinding.txtEmail, activityProfileBinding.imgUploadPhoto);
        mStorageReference = FirebaseStorage.getInstance().getReference("photos");


        activityProfileBinding.nameLayout.setOnClickListener(this::onClick);
        activityProfileBinding.aboutLayout.setOnClickListener(this::onClick);
        activityProfileBinding.editPhoneLayout.setOnClickListener(this::onClick);

        activityProfileBinding.fabUploadPhoto.setOnClickListener(this::openGallery);
        activityProfileBinding.imgUploadPhoto.setOnClickListener(this::openGallery);



    }


    private void openGallery(View view) {
        CropImage.activity()
                .setAspectRatio(16, 16)
                .start(Objects.requireNonNull(this));
    }

    public void onClick(View v) {
        Bundle bundle = new Bundle();
        EditItemBottomSheet editItemBottomSheet = new EditItemBottomSheet();
        VerifyPhoneBottomSheet verifyPhoneBottomSheet = new VerifyPhoneBottomSheet();

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }

        mLastClickTime = SystemClock.elapsedRealtime();

        if (v.getId() == R.id.nameLayout) {

            String getName = String.valueOf(activityProfileBinding.txtUserName.getText());
            bundle.putString(MyConstants.NAME, getName);
            editItemBottomSheet.setArguments(bundle);
            editItemBottomSheet.show(Objects.requireNonNull(ProfileActivity.this).getSupportFragmentManager(), MyConstants.NAME);


        } else if (v.getId() == R.id.editPhoneLayout) {

            verifyPhoneBottomSheet.setCancelable(false);
            verifyPhoneBottomSheet.show(Objects.requireNonNull(ProfileActivity.this).getSupportFragmentManager(), MyConstants.PHONE_NUMBER);
        }
    }


    private void uploadFile() {
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

            //                file path for the itemImage
            final StorageReference fileReference = mStorageReference.child(uid + "." + uri.getLastPathSegment());

            fileReference.putFile(uri).continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    //throw task.getException();
                    Log.d(TAG, "then: " + task.getException().getMessage());

                }
                return fileReference.getDownloadUrl();

            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Uri downLoadUri = task.getResult();
                    assert downLoadUri != null;
                    getImageUri = downLoadUri.toString();

                    Map<String, Object> uploadPhoto = new HashMap<>();
                    uploadPhoto.put("image", getImageUri);


                    MainActivity.usersAccountDbRef.updateChildren(uploadPhoto).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            progressDialog.dismiss();
                            DisplayViewUI.displayToast(this, "Photo uploaded");


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == Activity.RESULT_OK) {
                assert result != null;
                uri = result.getUri();

                Glide.with(ProfileActivity.this)
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(activityProfileBinding.imgUploadPhoto);

                uploadFile();

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
    }
}
