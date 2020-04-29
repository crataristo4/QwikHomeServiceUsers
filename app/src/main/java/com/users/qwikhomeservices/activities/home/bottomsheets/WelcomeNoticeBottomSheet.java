package com.users.qwikhomeservices.activities.home.bottomsheets;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.about.ProfileActivity;
import com.users.qwikhomeservices.databinding.LayoutGotoEditProfileBinding;

import java.util.Objects;

public class WelcomeNoticeBottomSheet extends BottomSheetDialogFragment {

    private LayoutGotoEditProfileBinding layoutGotoEditProfileBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutGotoEditProfileBinding = DataBindingUtil.inflate(inflater, R.layout.layout_goto_edit_profile, container, false);
        return layoutGotoEditProfileBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layoutGotoEditProfileBinding.txtShowNotice.setText(Html.fromHtml(Objects.requireNonNull(getActivity()).getResources().getString(R.string.welcomeNote)));

        layoutGotoEditProfileBinding.btnSetUp.setOnClickListener(v -> {
            dismiss();
            Objects.requireNonNull(getActivity()).startActivity(new Intent(getContext(), ProfileActivity.class));
        });
    }
}
