package com.users.qwikhomeservices.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.databinding.ImageTypeBinding;
import com.users.qwikhomeservices.databinding.TextTypeBinding;
import com.users.qwikhomeservices.models.ActivityItemModel;
import com.users.qwikhomeservices.utils.DisplayViewUI;
import com.users.qwikhomeservices.utils.DoubleClickListener;
import com.users.qwikhomeservices.utils.GetTimeAgo;

import java.text.MessageFormat;
import java.util.ArrayList;


public class MultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "MultiViewTypeAdapter";
    public static onItemClickListener onItemClickListener;
    private ArrayList<ActivityItemModel> dataSet;
    private Context mContext;
    int numOfLikes = 0;
    private String uid = MainActivity.uid;

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int listPosition) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(DisplayViewUI.getRandomDrawableColor());
        requestOptions.error(DisplayViewUI.getRandomDrawableColor());
        requestOptions.centerCrop();

        ActivityItemModel object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case ActivityItemModel.TEXT_TYPE:

                    //bind data in xml
                    ((TextTypeViewHolder) holder).textTypeBinding.setTextType(object);
                    //show time
                    ((TextTypeViewHolder) holder).textTypeBinding.txtTime.setText(GetTimeAgo.getTimeAgo(object.getTimeStamp()));
                    //load users images into views
                    Glide.with(((TextTypeViewHolder) holder).textTypeBinding.getRoot().getContext())
                            .load(object.getUserPhoto())
                            .thumbnail(0.5f)
                            .error(((TextTypeViewHolder) holder).textTypeBinding.getRoot().getResources().getDrawable(R.drawable.photoe))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((TextTypeViewHolder) holder).textTypeBinding.imgUserPhoto);

                    break;
                case ActivityItemModel.IMAGE_TYPE:
                    //bind data in xml
                    ((ImageTypeViewHolder) holder).imageTypeBinding.setImageType(object);
                    ((ImageTypeViewHolder) holder).imageTypeBinding.txtTime.setText(GetTimeAgo.getTimeAgo(object.getTimeStamp()));
                    //load user photo
                    Glide.with(((ImageTypeViewHolder) holder).imageTypeBinding.getRoot().getContext())
                            .load(object.getUserPhoto())
                            .thumbnail(0.5f)
                            .error(((ImageTypeViewHolder) holder).imageTypeBinding.getRoot().getResources().getDrawable(R.drawable.photoe))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(((ImageTypeViewHolder) holder).imageTypeBinding.imgUserPhoto);

                    //load images
                    Glide.with(((ImageTypeViewHolder) holder).imageTypeBinding.getRoot().getContext())
                            .load(object.getItemImage())
                            .thumbnail(0.5f)
                            .apply(requestOptions)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                    if (isFirstResource) {
                                        ((ImageTypeViewHolder) holder).imageTypeBinding.progressBar.setVisibility(View.INVISIBLE);

                                    }
                                    ((ImageTypeViewHolder) holder).imageTypeBinding.progressBar.setVisibility(View.VISIBLE);

                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    ((ImageTypeViewHolder) holder).imageTypeBinding.progressBar.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            }).transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((((ImageTypeViewHolder) holder).imageTypeBinding.imgContentPhoto));

                    ((ImageTypeViewHolder) holder).isLiked(object.getId(), ((ImageTypeViewHolder) holder).imgBtnLike);
                    ((ImageTypeViewHolder) holder).numOfLikes(((ImageTypeViewHolder) holder).txtLikes, object.getId());

                    ((ImageTypeViewHolder) holder).imageView.setOnClickListener(new DoubleClickListener() {
                        @Override
                        public void onDoubleClick(View view) {
                            DatabaseReference dbRef = FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Likes")
                                    .child(object.getId())
                                    .child(uid);

                            if (((ImageTypeViewHolder) holder).imgBtnLike.getTag().equals("like")) {

                                dbRef.setValue(true);

                            } else {

                                dbRef.removeValue();
                            }
                        }
                    });


                    break;

            }
        }

    }


    public MultiViewTypeAdapter(ArrayList<ActivityItemModel> data, Context context) {
        this.dataSet = data;
        this.mContext = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType) {
            case ActivityItemModel.TEXT_TYPE:

                return new TextTypeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.text_type, parent, false));

            case ActivityItemModel.IMAGE_TYPE:

                return new ImageTypeViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.image_type, parent, false));

        }
        return null;


    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        MultiViewTypeAdapter.onItemClickListener = onItemClickListener;

    }

    @Override
    public int getItemViewType(int position) {

        switch (dataSet.get(position).type) {
            case 0:
                return ActivityItemModel.TEXT_TYPE;
            case 1:
                return ActivityItemModel.IMAGE_TYPE;
            case 2:
                return ActivityItemModel.AUDIO_TYPE;
            default:
                return -1;
        }


    }

    @Override
    public int getItemCount() {
        return dataSet == null ? 0 : dataSet.size();
    }

    public interface onItemClickListener {
        void onClick(View view, ActivityItemModel activityItemModel);
    }

    //view holder for text
    static class TextTypeViewHolder extends RecyclerView.ViewHolder {
        TextTypeBinding textTypeBinding;

        TextTypeViewHolder(@NonNull TextTypeBinding textTypeBinding) {
            super(textTypeBinding.getRoot());
            this.textTypeBinding = textTypeBinding;

        }

    }

    //view holder for images
    static class ImageTypeViewHolder extends RecyclerView.ViewHolder {
        ImageTypeBinding imageTypeBinding;
        ImageView imageView, imgBtnLike;
        TextView txtLikes;

        ImageTypeViewHolder(@NonNull ImageTypeBinding imageTypeBinding) {
            super(imageTypeBinding.getRoot());
            this.imageTypeBinding = imageTypeBinding;
            imageView = imageTypeBinding.imgContentPhoto;
            txtLikes = imageTypeBinding.txtLikes;
            imgBtnLike = imageTypeBinding.imgBtnLike;

        }

        private void isLiked(String postId, ImageView imgBtnLike) {
            String uid = MainActivity.uid;

            DatabaseReference likesDbRef = FirebaseDatabase.getInstance()
                    .getReference().child("Likes").child(postId);


            likesDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(uid).exists()) {

                        imgBtnLike.setImageResource(R.drawable.ic_favorite_red);
                        imgBtnLike.setTag("liked");
                    } else {
                        imgBtnLike.setImageResource(R.drawable.ic_favorite);
                        imgBtnLike.setTag("like");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        private void numOfLikes(TextView txtIsLiked, String postId) {
            DatabaseReference likesDbRef = FirebaseDatabase.getInstance()
                    .getReference().child("Likes").child(postId);

            likesDbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    txtIsLiked.setText(MessageFormat.format("{0} likes", dataSnapshot.getChildrenCount()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }


}
