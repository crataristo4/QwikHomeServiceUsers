package com.users.qwikhomeservices.activities.home.serviceTypes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.users.qwikhomeservices.R;
import com.users.qwikhomeservices.activities.home.MainActivity;
import com.users.qwikhomeservices.adapters.CommentsAdatapter;
import com.users.qwikhomeservices.models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String getPostId, getItemDescription, getItemImage;
    private LinearLayoutManager layoutManager;
    private CommentsAdatapter adapter;
    private DatabaseReference databaseReference;
    private EmojiconEditText emojiconEditText;
    private ArrayList<Message> commentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Toolbar commentToolBar = findViewById(R.id.commentsToolBar);
        setSupportActionBar(commentToolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initViews();
        loadData();
    }

    void initViews() {
        Intent getCommentsIntent = getIntent();
        if (getCommentsIntent != null) {

            getPostId = getCommentsIntent.getStringExtra("postId");
            getItemDescription = getCommentsIntent.getStringExtra("itemDescription");
            getItemImage = getCommentsIntent.getStringExtra("itemImage");
        }

        ConstraintLayout activity_comment = findViewById(R.id.activity_comment);
        ImageView emojiButton = findViewById(R.id.emoticonButton);
        emojiconEditText = findViewById(R.id.emoticonEditTxt);
        EmojIconActions emojIconActions = new EmojIconActions(getApplicationContext(), activity_comment, emojiButton, emojiconEditText);
        emojIconActions.ShowEmojicon();


        TextView txtItemDes = findViewById(R.id.txtItemDescription);
        ImageView imgItemImage = findViewById(R.id.imgItemImage);

        txtItemDes.setText(getItemDescription);
        Glide.with(this)
                .load(getItemImage).thumbnail(0.5f)
                .centerCrop()
                .into(imgItemImage);

        findViewById(R.id.btnComment).setOnClickListener(v -> {
            addComment();
        });

        databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("Comments").child(getPostId);
        databaseReference.keepSynced(true);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();

        adapter = new CommentsAdatapter(commentList);
        recyclerView.setAdapter(adapter);


    }

    private void loadData() {
        Query query = databaseReference.orderByChild("timeStamp");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Message commentsList = ds.getValue(Message.class);

                        commentList.add(commentsList);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void addComment() {
        String postComment = emojiconEditText.getText().toString();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM HH:mm", Locale.ENGLISH);
        String name = MainActivity.name;

        String dateTime = simpleDateFormat.format(calendar.getTime());
        if (!postComment.trim().isEmpty()) {
            HashMap<String, Object> comments = new HashMap<>();
            comments.put("message", postComment);
            comments.put("senderName", name);
            comments.put("messageDateTime", dateTime);

            String randomId = databaseReference.push().getKey();
            assert randomId != null;
            databaseReference.child(randomId).setValue(comments);

            emojiconEditText.getText().clear();

        } else if (postComment.trim().isEmpty()) {
            emojiconEditText.setError("Cannot send empty message");
        }


    }


}
