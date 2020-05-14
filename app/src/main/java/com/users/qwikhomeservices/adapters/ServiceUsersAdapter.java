package com.users.qwikhomeservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.serviceTypes.DetailsScrollingActivity;
import com.users.qwikhomeservices.databinding.LayoutListItemsBinding;
import com.users.qwikhomeservices.models.Users;

import java.util.ArrayList;
import java.util.List;

public class ServiceUsersAdapter extends RecyclerView.Adapter<ServiceUsersAdapter.AllServicesViewHolder> {
    private Context mContext;
    private List<Users> artisanList;

    public ServiceUsersAdapter(ArrayList<Users> artisanList, Context context) {
        this.artisanList = artisanList;
        mContext = context;

    }

    @Override
    public void onBindViewHolder(@NonNull AllServicesViewHolder allServicesViewHolder, int position) {

        Users servicePerson = artisanList.get(position);
        allServicesViewHolder.cardView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        allServicesViewHolder.listItemsServicesBinding.setServiceType(servicePerson);

        Glide.with(mContext)
                .load(servicePerson.image)
                .thumbnail(0.5f)
                .error(mContext.getResources().getDrawable(R.drawable.photoe))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(allServicesViewHolder.listItemsServicesBinding.imgUserPhoto);

        //on item click listener
        allServicesViewHolder.listItemsServicesBinding.mMaterialCard.setOnClickListener(v -> {

            int adapterPosition = allServicesViewHolder.getAdapterPosition();
            Intent gotoDetailsIntent = new Intent(mContext,
                    DetailsScrollingActivity.class);
            gotoDetailsIntent.putExtra("position", adapterPosition);
            gotoDetailsIntent.putExtra("fullName", servicePerson.getFullName());
            gotoDetailsIntent.putExtra("about", servicePerson.getAbout());
            gotoDetailsIntent.putExtra("image", servicePerson.getImage());
            gotoDetailsIntent.putExtra("servicePersonId", servicePerson.getServicePersonId());
            gotoDetailsIntent.putExtra("mobileNumber", servicePerson.getMobileNumber());

            allServicesViewHolder.listItemsServicesBinding.getRoot().getContext().startActivity(gotoDetailsIntent);

        });


    }

    @NonNull
    @Override
    public AllServicesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutListItemsBinding listItemsServicesBinding = DataBindingUtil.inflate
                (LayoutInflater.from(viewGroup.getContext()),
                        R.layout.layout_list_items, viewGroup, false);

        return new AllServicesViewHolder(listItemsServicesBinding);
    }


    @Override
    public int getItemCount() {
        return artisanList == null ? 0 : artisanList.size();
    }

    static class AllServicesViewHolder extends RecyclerView.ViewHolder {

        LayoutListItemsBinding listItemsServicesBinding;
        CardView cardView;

        AllServicesViewHolder(@NonNull LayoutListItemsBinding listItemsServicesBinding) {
            super(listItemsServicesBinding.getRoot());

            this.listItemsServicesBinding = listItemsServicesBinding;
            cardView = listItemsServicesBinding.mMaterialCard;

        }

/*
        void showPresence(boolean online) {
            if (online) {
                isOnline.setVisibility(View.VISIBLE);
                isOnline.setImageResource(R.drawable.online);

            } else {
                isOnline.setVisibility(View.VISIBLE);
                isOnline.setImageResource(R.drawable.offline);

            }

        }
*/
    }


}
