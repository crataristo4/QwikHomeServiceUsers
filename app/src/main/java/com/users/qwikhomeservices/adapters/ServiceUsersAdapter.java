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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.serviceTypes.DetailsScrollingActivity;
import com.users.qwikhomeservices.databinding.LayoutListItemsBinding;
import com.users.qwikhomeservices.models.Users;
import com.users.qwikhomeservices.utils.DisplayViewUI;

//TODO change class name
public class ServiceUsersAdapter extends FirebaseRecyclerAdapter<Users,
        ServiceUsersAdapter.AllServicesViewHolder> {
    private Context mContext;

    public ServiceUsersAdapter(@NonNull FirebaseRecyclerOptions<Users> options, Context context) {
        super(options);
        mContext = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull AllServicesViewHolder allServicesViewHolder,
                                    int i, @NonNull Users servicePerson) {

        allServicesViewHolder.cardView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation));
        allServicesViewHolder.listItemsServicesBinding.setServiceType(servicePerson);

        if (servicePerson.getImage().isEmpty()) {
            Glide.with(allServicesViewHolder.itemView.getContext())
                    .load(mContext.getResources().getDrawable(R.drawable.photoe))
                    .into(allServicesViewHolder.listItemsServicesBinding.imgUserPhoto);
        } else if (!servicePerson.getImage().isEmpty()) {

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
                                allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.INVISIBLE);

                            }
                            allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (isFirstResource)
                                allServicesViewHolder.listItemsServicesBinding.pbLoading.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    }).transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(allServicesViewHolder.listItemsServicesBinding.imgUserPhoto);

        }

        //on item click listener
        allServicesViewHolder.listItemsServicesBinding.mMaterialCard.setOnClickListener(v -> {

            String position = getRef(i).getKey();
            Intent gotoDetailsIntent = new Intent(mContext,
                    DetailsScrollingActivity.class);
            gotoDetailsIntent.putExtra("position", position);
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
