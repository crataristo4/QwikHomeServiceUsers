package com.users.qwikhomeservices.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.databinding.LayoutCommentBinding;
import com.users.qwikhomeservices.models.Message;

import java.util.ArrayList;
import java.util.List;

public class CommentsAdatapter extends RecyclerView.Adapter<CommentsAdatapter.CommentsViewHolder> {

    private List<Message> commentList;

    public CommentsAdatapter(ArrayList<Message> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new CommentsViewHolder((DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.layout_comment, viewGroup, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        Message commentMsg = commentList.get(position);

        holder.layoutCommentBinding.setComments(commentMsg);

    }

    @Override
    public int getItemCount() {
        return commentList == null ? 0 : commentList.size();
    }


    static class CommentsViewHolder extends RecyclerView.ViewHolder {

        LayoutCommentBinding layoutCommentBinding;

        CommentsViewHolder(@NonNull LayoutCommentBinding layoutCommentBinding) {
            super(layoutCommentBinding.getRoot());
            this.layoutCommentBinding = layoutCommentBinding;

        }


    }
}
