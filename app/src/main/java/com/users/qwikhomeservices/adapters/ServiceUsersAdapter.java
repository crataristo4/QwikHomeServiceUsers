package com.users.qwikhomeservices.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.serviceTypes.DetailsScrollingActivity;
import com.users.qwikhomeservices.databinding.LayoutListItemsBinding;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.DisplayViewUI;

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

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(DisplayViewUI.getRandomDrawableColor());
        requestOptions.error(DisplayViewUI.getRandomDrawableColor());
        requestOptions.centerCrop();

        Glide.with(allServicesViewHolder.itemView.getContext())
                .load(servicePerson.image)
                .apply(requestOptions)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        if (isFirstResource) {
                            allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.VISIBLE);

                        }
                        allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.INVISIBLE);

                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.INVISIBLE);
                        return false;
                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .error(allServicesViewHolder.listItemsServicesBinding.getRoot().getContext().getResources().getDrawable(R.drawable.photoe))
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
