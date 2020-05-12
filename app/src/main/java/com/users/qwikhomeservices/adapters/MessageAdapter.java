package com.users.qwikhomeservices.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.models.Message;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private static String UID;
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case Message.ITEM_TYPE_SENT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_sent, parent, false);

                break;
            case Message.ITEM_TYPE_RECEIVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_message_received, parent, false);

                break;
        }
        return new MessageViewHolder(Objects.requireNonNull(view));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message messages = messageList.get(position);
        holder.txtMsg.setText(messages.getMessage());
        holder.txtDateTime.setText(messages.getMessageDateTime());
        /*Glide.with(holder.itemView.getContext())
                .load(messages.getSenderPhoto())
                .error(holder.itemView.getResources().getDrawable(R.drawable.photoe))
                .into(holder.imgPhoto);*/

    }


    @Override
    public int getItemCount() {
        return messageList == null ? 0 : messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        UID = MainActivity.uid;

        if (messageList.get(position).getSenderId().equals(UID)) {
            return Message.ITEM_TYPE_SENT;
        } else {
            return Message.ITEM_TYPE_RECEIVED;
        }
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView txtMsg, txtDateTime;
        //private BubbleTextView txtMsg;
        // public CircleImageView imgPhoto;

        MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMsg = itemView.findViewById(R.id.txtMessage);
            txtDateTime = itemView.findViewById(R.id.txtDateTime);
            // imgPhoto = itemView.findViewById(R.id.imgPhoto);

        }
    }

}
