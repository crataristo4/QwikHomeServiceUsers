package com.users.quickhomeservices.activities.home.bottomsheets;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.users.quickhomeservices.R;
import com.users.quickhomeservices.activities.home.MainActivity;
import com.users.quickhomeservices.databinding.LayoutEditItemBottomSheetBinding;
import com.users.quickhomeservices.utils.MyConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EditItemBottomSheet extends BottomSheetDialogFragment {
    private LayoutEditItemBottomSheetBinding layoutEditItemBottomSheetBinding;
    private TextInputLayout txtInputItem;
    private Bundle bundle;
    private Map<String, Object> updateItem = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutEditItemBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.layout_edit_item_bottom_sheet, container, false);

        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return layoutEditItemBottomSheetBinding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtInputItem = layoutEditItemBottomSheetBinding.textInputLayout;
        bundle = getArguments();
        if (bundle != null) {
            if (Objects.equals(bundle.getString(MyConstants.NAME), MainActivity.name)) {

                layoutEditItemBottomSheetBinding.textInputLayout.setHint("Edit name");
                Objects.requireNonNull(layoutEditItemBottomSheetBinding.textInputLayout.getEditText()).setSingleLine(true);
                Objects.requireNonNull(layoutEditItemBottomSheetBinding.textInputLayout
                        .getEditText()).setText(bundle.getString(MyConstants.NAME));

            }

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layoutEditItemBottomSheetBinding.txtCancel.setOnClickListener(v -> dismiss());
        layoutEditItemBottomSheetBinding.btnOk.setOnClickListener(this::onOkClicked);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onOkClicked(View view) {
        if (Objects.equals(bundle.getString(MyConstants.NAME), MainActivity.name)) {
            updateUserName();
        }

    }

    private void updateUserName() {
        String name = Objects.requireNonNull(layoutEditItemBottomSheetBinding.textInputLayout.getEditText()).getText().toString();
        if (name.trim().isEmpty()) {
            txtInputItem.setErrorEnabled(true);
            txtInputItem.setError("name field required");
        } else {
            txtInputItem.setErrorEnabled(false);
        }

        Objects.requireNonNull(getActivity()).runOnUiThread(() -> {
            if (!name.trim().isEmpty()) {
                updateItem.put("name", name);
                MainActivity.usersAccountDbRef.updateChildren(updateItem);
                dismiss();
            }

        });

    }
}
